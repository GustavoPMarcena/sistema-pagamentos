package com.team.charge_proxy.client;

import com.team.charge_proxy.config.AsaasFeignConfig;
import com.team.charge_proxy.web.dto.AsaasChargeDeleteResponse;
import com.team.charge_proxy.web.dto.AsaasChargeRequest;
import com.team.charge_proxy.web.dto.AsaasChargeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "asaas-payments", url = "${asaas.base.url}/payments", configuration = AsaasFeignConfig.class)
public interface PaymentMethodsAsaas {

    @PostMapping
    AsaasChargeResponse createCharge(@RequestHeader("Authorization") String authorization,
                                    @RequestBody AsaasChargeRequest request);

    @DeleteMapping("/{id}")
    AsaasChargeDeleteResponse deleteCharge(@RequestHeader("Authorization") String authorization,
                                          @PathVariable("id") String id);
}
