package com.team.charge_proxy.service;

import com.team.charge_proxy.client.ClientMethodsAsaas;
import com.team.charge_proxy.web.dto.AsaasCustomerRequest;
import com.team.charge_proxy.web.dto.AsaasCustomerResponse;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@WebService(
        endpointInterface = "com.team.charge_proxy.service.ClientService",
        targetNamespace = "http://ifpb.com/sistema_pagamentos/clientes",
        serviceName = "ClientService"
)
@SOAPBinding(style = SOAPBinding.Style.RPC)
@Service
public class ClientServiceImpl implements  ClientService {
    private final ClientMethodsAsaas clientMethodsAsaas;
    @Value("${asaas.api.key}")
    private String apiKey;

    public ClientServiceImpl(ClientMethodsAsaas clientMethodsAsaas) {
        this.clientMethodsAsaas = clientMethodsAsaas;
    }

    @Override
    public AsaasCustomerResponse createClient(AsaasCustomerRequest request) {
        return clientMethodsAsaas.createCustomer(apiKey, request);
    }

    @Override
    public AsaasCustomerResponse getClientById(String clientId) {
        return clientMethodsAsaas.getCustomer(apiKey, clientId);
    }
}
