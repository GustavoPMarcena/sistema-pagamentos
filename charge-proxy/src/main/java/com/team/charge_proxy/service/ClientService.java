package com.team.charge_proxy.service;

import com.team.charge_proxy.web.dto.AsaasCustomerRequest;
import com.team.charge_proxy.web.dto.AsaasCustomerResponse;

public interface ClientService {
    AsaasCustomerResponse createClient(AsaasCustomerRequest request);
    AsaasCustomerResponse getClientById(String clientId);
}
