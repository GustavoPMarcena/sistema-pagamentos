package com.team.charge_manager.integration.soap;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;

/**
 * Contrato SOAP para o Proxy notificar o Manager sobre eventos de pagamento.
 *
 * Observação: o Proxy já possui um client usando este contrato (mesmo namespace e nomes).
 */
@WebService(
        targetNamespace = "http://ifpb.com/sistema_pagamentos/manager",
        name = "PaymentNotificationService"
)
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface PaymentNotificationSoapService {

    @WebMethod
    void notifyPaymentConfirmed(
            @WebParam(name = "paymentId") String paymentId,
            @WebParam(name = "status") String status,
            @WebParam(name = "value") Double value
    );
}
