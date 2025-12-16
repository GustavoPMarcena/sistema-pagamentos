package com.team.charge_proxy.service;

import com.team.charge_proxy.client.PaymentMethodsAsaas;
import com.team.charge_proxy.web.dto.AsaasChargeDeleteResponse;
import com.team.charge_proxy.web.dto.AsaasChargeRequest;
import com.team.charge_proxy.web.dto.AsaasChargeResponse;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@WebService(
        endpointInterface = "com.team.charge_proxy.service.PaymentService",
        targetNamespace = "http://ifpb.com/sistema_pagamentos/clientes",
        serviceName = "ClientService"
)
@SOAPBinding(style = SOAPBinding.Style.RPC)
@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentMethodsAsaas paymentMethodsAsaas;
    @Value("${asaas.api.key}")
    private String apiKey;

    public PaymentServiceImpl(PaymentMethodsAsaas paymentMethodsAsaas) {
        this.paymentMethodsAsaas = paymentMethodsAsaas;
    }
    @Override
    public AsaasChargeResponse createCharge(AsaasChargeRequest request) {
        return paymentMethodsAsaas.createCharge(apiKey, request);
    }

    @Override
    public AsaasChargeDeleteResponse deleteCharge(String chargeId) {
        return paymentMethodsAsaas.deleteCharge(apiKey, chargeId);
    }
}
