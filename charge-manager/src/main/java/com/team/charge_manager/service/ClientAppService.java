package com.team.charge_manager.service;

import com.team.charge_manager.api.dto.ClientResponse;
import com.team.charge_manager.api.dto.CreateClientRequest;
import com.team.charge_manager.integration.ProxyGateway;
import com.team.charge_manager.integration.dto.ProxyCustomerRequest;
import com.team.charge_manager.integration.dto.ProxyCustomerResponse;
import com.team.charge_manager.events.ClientCreatedEvent;
import com.team.charge_manager.events.ClientsFetchedEvent;
import com.team.charge_manager.persistence.entity.ClientEntity;
import com.team.charge_manager.persistence.repo.ClientRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientAppService {

    private final ClientRepository clientRepository;
    private final ProxyGateway proxyGateway;
    private final ApplicationEventPublisher publisher;
    private final TransactionTemplate transactionTemplate;

    public ClientAppService(ClientRepository clientRepository, ProxyGateway proxyGateway,
            ApplicationEventPublisher publisher, TransactionTemplate transactionTemplate) {
        this.clientRepository = clientRepository;
        this.proxyGateway = proxyGateway;
        this.publisher = publisher;
        this.transactionTemplate = transactionTemplate;
    }

    public ClientResponse create(CreateClientRequest request) {
        // [m2] Explicit transaction control using TransactionTemplate
        return transactionTemplate.execute(status -> {
            clientRepository.findByEmail(request.getEmail()).ifPresent(c -> {
                throw new IllegalArgumentException("E-mail já cadastrado");
            });

            ClientEntity client = new ClientEntity();
            client.setName(request.getName());
            client.setEmail(request.getEmail());
            client.setCpf(request.getCpf());

            // Create customer in Proxy/ASAAS (or MOCK) and store the external id
            ProxyCustomerResponse proxyCustomer = proxyGateway.createCustomer(
                    new ProxyCustomerRequest(client.getName(), client.getEmail(), client.getCpf()));
            if (proxyCustomer != null) {
                client.setAsaasCustomerId(proxyCustomer.getId());
            }

            ClientEntity saved = clientRepository.save(client);
            publisher.publishEvent(new ClientCreatedEvent(saved));

            return new ClientResponse(saved.getId(), saved.getName(), saved.getEmail(), saved.getCpf(),
                    saved.getAsaasCustomerId());
        });
    }

    public ClientResponse getById(Long id) {
        // Read-only logic usually doesn't strictly need TransactionTemplate unless you
        // want to be 100% explicit
        return transactionTemplate.execute(status -> {
            ClientEntity c = clientRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
            return new ClientResponse(c.getId(), c.getName(), c.getEmail(), c.getCpf(), c.getAsaasCustomerId());
        });
    }

    public List<ClientResponse> findAll() {
        return transactionTemplate.execute(status -> {
            List<ClientEntity> clients = clientRepository.findAll();
            publisher.publishEvent(new ClientsFetchedEvent(clients));

            return clients.stream()
                    .map(c -> new ClientResponse(c.getId(), c.getName(), c.getEmail(), c.getCpf(),
                            c.getAsaasCustomerId()))
                    .collect(Collectors.toList());
        });
    }
}
