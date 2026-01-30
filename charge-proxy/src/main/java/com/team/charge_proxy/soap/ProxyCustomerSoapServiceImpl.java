package com.team.charge_proxy.soap;

import com.team.charge_proxy.web.dto.AsaasCustomerRequest;
import com.team.charge_proxy.web.dto.AsaasCustomerResponse;
import com.team.charge_proxy.service.ClientService;
import jakarta.jws.WebService;
import org.springframework.stereotype.Service;

@Service
@WebService(
        endpointInterface = "com.team.charge_proxy.soap.ProxyCustomerSoapService",
        targetNamespace = "http://ifpb.com/sistema_pagamentos/proxy",
        serviceName = "ProxyCustomerService"
)
public class ProxyCustomerSoapServiceImpl implements ProxyCustomerSoapService {

    private final ClientService clientService;

    public ProxyCustomerSoapServiceImpl(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public String createCustomer(String name, String email, String cpf) {
        AsaasCustomerRequest req = new AsaasCustomerRequest();
        req.setName(name);
        req.setEmail(email);
        req.setCpfCnpj(cpf);

        // No proxy, o serviço de cliente expõe o método createClient (REST/HTTP)
        AsaasCustomerResponse resp = clientService.createClient(req);
        return resp != null ? resp.getId() : null;
    }
}
