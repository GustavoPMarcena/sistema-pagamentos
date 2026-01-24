package com.team.charge_manager.integration.soap;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;

/**
 * Serviço SOAP para receber notificações de pagamento do Proxy.
 * 
 * Conforme o diagrama: comunicação SOAP-RPC entre Manager e Proxy.
 */
@WebService(
        targetNamespace = "http://ifpb.com/sistema_pagamentos/manager",
        name = "PaymentNotificationService"
)
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface PaymentNotificationService {

    @WebMethod
    void notifyPaymentConfirmed(
            @WebParam(name = "paymentId") String paymentId,
            @WebParam(name = "status") String status,
            @WebParam(name = "value") Double value
    );
}
