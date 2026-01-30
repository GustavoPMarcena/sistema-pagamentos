package com.team.charge_manager.service;

import com.team.charge_manager.api.dto.ChargeResponse;
import com.team.charge_manager.api.dto.CreateChargeRequest;
import com.team.charge_manager.domain.ChargeStatus;
import com.team.charge_manager.events.ChargeCreatedEvent;
import com.team.charge_manager.events.ChargesFetchedEvent;
import com.team.charge_manager.events.PaymentConfirmedEvent;
import com.team.charge_manager.integration.ProxyGateway;
import com.team.charge_manager.integration.dto.ProxyChargeRequest;
import com.team.charge_manager.integration.dto.ProxyChargeResponse;
import com.team.charge_manager.persistence.entity.ChargeEntity;
import com.team.charge_manager.persistence.entity.ClientEntity;
import com.team.charge_manager.persistence.repo.ChargeRepository;
import com.team.charge_manager.persistence.repo.ClientRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChargeAppService {

    private final ChargeRepository chargeRepository;
    private final ClientRepository clientRepository;
    private final ProxyGateway proxyGateway;
    private final ApplicationEventPublisher publisher;
    private final TransactionTemplate transactionTemplate;

    public ChargeAppService(ChargeRepository chargeRepository,
            ClientRepository clientRepository,
            ProxyGateway proxyGateway,
            ApplicationEventPublisher publisher,
            TransactionTemplate transactionTemplate) {
        this.chargeRepository = chargeRepository;
        this.clientRepository = clientRepository;
        this.proxyGateway = proxyGateway;
        this.publisher = publisher;
        this.transactionTemplate = transactionTemplate;
    }

    public ChargeResponse create(CreateChargeRequest request) {
        // [m2] Explicit transaction control using TransactionTemplate
        return transactionTemplate.execute(status -> {
            ClientEntity client = clientRepository.findById(request.getClientId())
                    .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

            if (client.getAsaasCustomerId() == null || client.getAsaasCustomerId().isBlank()) {
                throw new IllegalStateException("Cliente sem asaasCustomerId. Cadastre o cliente novamente.");
            }

            ChargeEntity charge = new ChargeEntity();
            charge.setClient(client);
            charge.setValue(request.getValue());
            charge.setBillingType(request.getBillingType());
            charge.setDueDate(request.getDueDate());
            charge.setStatus(ChargeStatus.PENDING);

            ChargeEntity saved = chargeRepository.save(charge);

            // Create charge in Proxy/ASAAS (or MOCK)
            ProxyChargeResponse proxyCharge = proxyGateway.createCharge(
                    new ProxyChargeRequest(client.getAsaasCustomerId(), request.getValue(), request.getBillingType(),
                            request.getDueDate()));
            if (proxyCharge != null) {
                saved.setAsaasPaymentId(proxyCharge.getId());
                saved.setStatus(ChargeStatus.REGISTERED);
            }

            ChargeEntity updated = chargeRepository.save(saved);

            publisher.publishEvent(new ChargeCreatedEvent(updated));

            return toResponse(updated);
        });
    }

    public ChargeResponse cancel(Long chargeId) {
        return transactionTemplate.execute(status -> {
            ChargeEntity c = chargeRepository.findById(chargeId)
                    .orElseThrow(() -> new IllegalArgumentException("Cobrança não encontrada"));

            if (c.getAsaasPaymentId() != null && !c.getAsaasPaymentId().isBlank()) {
                proxyGateway.cancelCharge(c.getAsaasPaymentId());
            }

            c.setStatus(ChargeStatus.CANCELED);
            ChargeEntity saved = chargeRepository.save(c);
            return toResponse(saved);
        });
    }

    public ChargeResponse getById(Long id) {
        return transactionTemplate.execute(status -> {
            return toResponse(chargeRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Cobrança não encontrada")));
        });
    }

    public List<ChargeResponse> findAll() {
        return transactionTemplate.execute(status -> {
            List<ChargeEntity> charges = chargeRepository.findAll();
            publisher.publishEvent(new ChargesFetchedEvent(charges));

            return charges.stream()
                    .map(this::toResponse)
                    .collect(Collectors.toList());
        });
    }

    public void onPaymentConfirmed(String asaasPaymentId, String status, Double value) {
        publisher.publishEvent(new PaymentConfirmedEvent(asaasPaymentId, status, value));
    }

    public void markPaidByAsaasId(String asaasPaymentId) {
        transactionTemplate.execute(status -> {
            ChargeEntity c = chargeRepository.findByAsaasPaymentId(asaasPaymentId)
                    .orElseThrow(() -> new IllegalArgumentException("Cobrança não encontrada pelo asaasPaymentId"));
            c.setStatus(ChargeStatus.PAID);
            chargeRepository.save(c);
            return null;
        });
    }

    private ChargeResponse toResponse(ChargeEntity c) {
        return new ChargeResponse(
                c.getId(),
                c.getClient().getId(),
                c.getValue(),
                c.getBillingType(),
                c.getStatus(),
                c.getDueDate(),
                c.getAsaasPaymentId());
    }
}
