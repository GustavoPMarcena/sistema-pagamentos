package com.team.charge_proxy.service;

import com.team.charge_proxy.web.dto.AsaasChargeDeleteResponse;
import com.team.charge_proxy.web.dto.AsaasChargeRequest;
import com.team.charge_proxy.web.dto.AsaasChargeResponse;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import org.springframework.stereotype.Service;

@WebService(
        endpointInterface = "com.team.charge_proxy.service.PaymentService",
        targetNamespace = "http://ifpb.com/sistema_pagamentos/clientes",
        serviceName = "ClientService"
)
@SOAPBinding(style = SOAPBinding.Style.RPC)
@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentService paymentService;

    public PaymentServiceImpl(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    public AsaasChargeResponse createCharge(AsaasChargeRequest request) {
        return paymentService.createCharge(request);
    }

    @Override
    public AsaasChargeDeleteResponse deleteCharge(String chargeId) {
        return paymentService.deleteCharge(chargeId);
    }
}
