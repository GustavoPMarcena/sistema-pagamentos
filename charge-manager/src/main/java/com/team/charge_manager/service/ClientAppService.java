package com.team.charge_manager.service;

import com.team.charge_manager.api.dto.ClientResponse;
import com.team.charge_manager.api.dto.CreateClientRequest;
import com.team.charge_manager.integration.ProxyGateway;
import com.team.charge_manager.integration.dto.ProxyCustomerRequest;
import com.team.charge_manager.integration.dto.ProxyCustomerResponse;
import com.team.charge_manager.persistence.entity.ClientEntity;
import com.team.charge_manager.persistence.repo.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientAppService {

    private final ClientRepository clientRepository;
    private final ProxyGateway proxyGateway;

    public ClientAppService(ClientRepository clientRepository, ProxyGateway proxyGateway) {
        this.clientRepository = clientRepository;
        this.proxyGateway = proxyGateway;
    }

    @Transactional
    public ClientResponse create(CreateClientRequest request) {
        clientRepository.findByEmail(request.getEmail()).ifPresent(c -> {
            throw new IllegalArgumentException("E-mail já cadastrado");
        });

        ClientEntity client = new ClientEntity();
        client.setName(request.getName());
        client.setEmail(request.getEmail());
        client.setCpf(request.getCpf());

        // Create customer in Proxy/ASAAS (or MOCK) and store the external id
        ProxyCustomerResponse proxyCustomer = proxyGateway.createCustomer(
                new ProxyCustomerRequest(client.getName(), client.getEmail(), client.getCpf())
        );
        if (proxyCustomer != null) {
            client.setAsaasCustomerId(proxyCustomer.getId());
        }

        ClientEntity saved = clientRepository.save(client);
        return new ClientResponse(saved.getId(), saved.getName(), saved.getEmail(), saved.getCpf(), saved.getAsaasCustomerId());
    }

    @Transactional(readOnly = true)
    public ClientResponse getById(Long id) {
        ClientEntity c = clientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
        return new ClientResponse(c.getId(), c.getName(), c.getEmail(), c.getCpf(), c.getAsaasCustomerId());
    }
}
