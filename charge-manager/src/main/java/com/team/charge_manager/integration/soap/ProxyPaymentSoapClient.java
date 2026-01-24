package com.team.charge_manager.integration.soap;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;

@WebService(
        targetNamespace = "http://ifpb.com/sistema_pagamentos/proxy",
        name = "ProxyPaymentService"
)
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface ProxyPaymentSoapClient {

    @WebMethod
    String createCharge(
            @WebParam(name = "customerId") String customerId,
            @WebParam(name = "value") Double value,
            @WebParam(name = "billingType") String billingType,
            @WebParam(name = "dueDate") String dueDate
    );

    @WebMethod
    boolean cancelCharge(
            @WebParam(name = "paymentId") String paymentId
    );
}
