package com.team.charge_proxy.client;

import com.team.charge_proxy.config.AsaasFeignConfig;
import com.team.charge_proxy.web.dto.AsaasCustomerRequest;
import com.team.charge_proxy.web.dto.AsaasCustomerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "client-asaas", url = "https://api-sandbox.asaas.com/v3/customers",
        configuration = AsaasFeignConfig.class)
public interface ClientMethodsAsaas {
    @PostMapping
    AsaasCustomerResponse createCustomer(@RequestBody AsaasCustomerRequest request);

    @GetMapping("/{userId}")
    AsaasCustomerResponse getCustomer(@RequestParam String userId);
}
