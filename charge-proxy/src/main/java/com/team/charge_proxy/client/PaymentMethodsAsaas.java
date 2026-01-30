package com.team.charge_proxy.client;

import com.team.charge_proxy.config.AsaasFeignConfig;
import com.team.charge_proxy.web.dto.AsaasChargeDeleteResponse;
import com.team.charge_proxy.web.dto.AsaasChargeRequest;
import com.team.charge_proxy.web.dto.AsaasChargeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "asaas-payments", url = "https://api-sandbox.asaas.com/v3/payments", configuration = AsaasFeignConfig.class)
public interface PaymentMethodsAsaas {

    @PostMapping
    AsaasChargeResponse createCharge(@RequestHeader("access_token") String accessToken,
                                    @RequestBody AsaasChargeRequest request);

    @DeleteMapping("/{id}")
    AsaasChargeDeleteResponse deleteCharge(@RequestHeader("access_token") String accessToken,
                                          @PathVariable("id") String id);
}
