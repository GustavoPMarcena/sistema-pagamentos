package com.team.charge_proxy.soap;

import com.team.charge_proxy.service.PaymentService;
import com.team.charge_proxy.web.dto.AsaasChargeDeleteResponse;
import com.team.charge_proxy.web.dto.AsaasChargeRequest;
import com.team.charge_proxy.web.dto.AsaasChargeResponse;
import com.team.charge_proxy.web.dto.BillingType;
import jakarta.jws.WebService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@WebService(
        endpointInterface = "com.team.charge_proxy.soap.ProxyPaymentSoapService",
        targetNamespace = "http://ifpb.com/sistema_pagamentos/proxy",
        serviceName = "ProxyPaymentService"
)
public class ProxyPaymentSoapServiceImpl implements ProxyPaymentSoapService {

    private final PaymentService paymentService;

    public ProxyPaymentSoapServiceImpl(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    public String createCharge(String customerId, Double value, String billingType, String dueDate) {
        AsaasChargeRequest req = new AsaasChargeRequest();
        req.setCustomer(customerId);
        // DTO interno usa BigDecimal e enum BillingType
        req.setValue(value == null ? null : BigDecimal.valueOf(value));
        req.setBillingType(toBillingType(billingType));
        req.setDueDate(dueDate);

        AsaasChargeResponse resp = paymentService.createCharge(req);
        return resp != null ? resp.getId() : null;
    }

    @Override
    public boolean cancelCharge(String paymentId) {
        AsaasChargeDeleteResponse resp = paymentService.deleteCharge(paymentId);
        return resp != null && resp.isDeleted();
    }

    private BillingType toBillingType(String raw) {
        if (raw == null || raw.isBlank()) {
            return BillingType.UNDEFINED;
        }

        String normalized = raw.trim().toUpperCase();
        // Aceitar variações comuns vindas do Manager
        if (normalized.equals("CARTAO") || normalized.equals("CARTAO_CREDITO") || normalized.equals("CARTAO_CREDITO")) {
            return BillingType.CREDIT_CARD;
        }
        if (normalized.equals("CREDIT") || normalized.equals("CARD")) {
            return BillingType.CREDIT_CARD;
        }
        if (normalized.equals("BOLETO")) {
            return BillingType.BOLETO;
        }
        if (normalized.equals("PIX")) {
            return BillingType.PIX;
        }

        try {
            return BillingType.valueOf(normalized);
        } catch (Exception ignored) {
            return BillingType.UNDEFINED;
        }
    }
}
