package com.team.charge_manager.integration.soap;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;

@WebService(
        targetNamespace = "http://ifpb.com/sistema_pagamentos/proxy",
        name = "ProxyCustomerService"
)
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface ProxyCustomerSoapClient {

    @WebMethod
    String createCustomer(
            @WebParam(name = "name") String name,
            @WebParam(name = "email") String email,
            @WebParam(name = "cpf") String cpf
    );
}
