package com.team.charge_proxy.client;

import com.team.charge_proxy.config.AsaasFeignConfig;
import com.team.charge_proxy.web.dto.AsaasCustomerRequest;
import com.team.charge_proxy.web.dto.AsaasCustomerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "asaas-customers", url = "https://api-sandbox.asaas.com/v3/customers", configuration = AsaasFeignConfig.class)
public interface ClientMethodsAsaas {

    @PostMapping
    AsaasCustomerResponse createCustomer(@RequestHeader("Authorization") String authorization,
                                        @RequestBody AsaasCustomerRequest request);

    @GetMapping("/{id}")
    AsaasCustomerResponse getCustomer(@RequestHeader("Authorization") String authorization,
                                     @PathVariable("id") String id);
}
