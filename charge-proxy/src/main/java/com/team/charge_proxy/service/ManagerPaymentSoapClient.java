package com.team.charge_proxy.service;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;

@WebService(
        targetNamespace = "http://ifpb.com/sistema_pagamentos/manager",
        name = "PaymentNotificationService"
)
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface ManagerPaymentSoapClient {

    @WebMethod
    void notifyPaymentConfirmed(
            @WebParam(name = "paymentId") String paymentId,
            @WebParam(name = "status") String status,
            @WebParam(name = "value") Double value
    );
}
