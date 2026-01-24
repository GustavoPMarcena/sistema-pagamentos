package com.team.charge_proxy.service;

import com.team.charge_proxy.client.ClientMethodsAsaas;
import com.team.charge_proxy.web.dto.AsaasCustomerRequest;
import com.team.charge_proxy.web.dto.AsaasCustomerResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientMethodsAsaas clientMethodsAsaas;

    @Value("${asaas.api.key:MOCK}")
    private String apiKey;

    public ClientServiceImpl(ClientMethodsAsaas clientMethodsAsaas) {
        this.clientMethodsAsaas = clientMethodsAsaas;
    }

    @Override
    public AsaasCustomerResponse createClient(AsaasCustomerRequest request) {
        if (isMock()) {
            AsaasCustomerResponse r = new AsaasCustomerResponse();
            r.setId("mock_customer_" + UUID.randomUUID());
            r.setName(request.getName());
            r.setEmail(request.getEmail());
            r.setCpfCnpj(request.getCpfCnpj());
            return r;
        }
        return clientMethodsAsaas.createCustomer(bearer(), request);
    }

    @Override
    public AsaasCustomerResponse getClientById(String clientId) {
        if (isMock()) {
            AsaasCustomerResponse r = new AsaasCustomerResponse();
            r.setId(clientId);
            r.setName("Mock Customer");
            r.setEmail("mock@example.com");
            r.setCpfCnpj("00000000000");
            return r;
        }
        return clientMethodsAsaas.getCustomer(bearer(), clientId);
    }

    private boolean isMock() {
        return apiKey == null || apiKey.isBlank() || "MOCK".equalsIgnoreCase(apiKey);
    }

    private String bearer() {
        return "Bearer " + apiKey;
    }
}
