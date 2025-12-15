package com.team.charge_proxy.client;

import com.team.charge_proxy.config.AsaasFeignConfig;
import com.team.charge_proxy.web.dto.AsaasChargeDeleteResponse;
import com.team.charge_proxy.web.dto.AsaasChargeRequest;
import com.team.charge_proxy.web.dto.AsaasChargeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "payment-asaas", url = "https://api-sandbox.asaas.com/v3/payments",
        configuration = AsaasFeignConfig.class)
public interface PaymentMethodsAsaas {

    @PostMapping
    AsaasChargeResponse createCharge(@RequestBody AsaasChargeRequest request);

    @DeleteMapping("/{id}")
    AsaasChargeDeleteResponse deleteCharge(@RequestParam String id);


}
