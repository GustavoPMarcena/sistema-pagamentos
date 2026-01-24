package com.team.charge_proxy.service;

import com.team.charge_proxy.web.dto.AsaasChargeDeleteResponse;
import com.team.charge_proxy.web.dto.AsaasChargeRequest;
import com.team.charge_proxy.web.dto.AsaasChargeResponse;

public interface PaymentService {
    AsaasChargeResponse createCharge(AsaasChargeRequest request);
    AsaasChargeDeleteResponse deleteCharge(String chargeId);
}
