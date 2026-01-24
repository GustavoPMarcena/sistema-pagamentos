package com.team.charge_proxy.service;

import com.team.charge_proxy.client.PaymentMethodsAsaas;
import com.team.charge_proxy.web.dto.AsaasChargeDeleteResponse;
import com.team.charge_proxy.web.dto.AsaasChargeRequest;
import com.team.charge_proxy.web.dto.AsaasChargeResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentMethodsAsaas paymentMethodsAsaas;

    @Value("${asaas.api.key:MOCK}")
    private String apiKey;

    public PaymentServiceImpl(PaymentMethodsAsaas paymentMethodsAsaas) {
        this.paymentMethodsAsaas = paymentMethodsAsaas;
    }

    @Override
    public AsaasChargeResponse createCharge(AsaasChargeRequest request) {
        if (isMock()) {
            AsaasChargeResponse r = new AsaasChargeResponse();
            r.setId("mock_payment_" + UUID.randomUUID());
            r.setCustomer(request.getCustomer());
            r.setValue(request.getValue());
            return r;
        }
        return paymentMethodsAsaas.createCharge(bearer(), request);
    }

    @Override
    public AsaasChargeDeleteResponse deleteCharge(String chargeId) {
        if (isMock()) {
            AsaasChargeDeleteResponse r = new AsaasChargeDeleteResponse();
            r.setDeleted(true);
            r.setId(chargeId);
            return r;
        }
        return paymentMethodsAsaas.deleteCharge(bearer(), chargeId);
    }

    private boolean isMock() {
        return apiKey == null || apiKey.isBlank() || "MOCK".equalsIgnoreCase(apiKey);
    }

    private String bearer() {
        return "Bearer " + apiKey;
    }
}
