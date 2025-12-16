package com.team.charge_proxy.service;

import com.team.charge_proxy.web.dto.AsaasCustomerRequest;
import com.team.charge_proxy.web.dto.AsaasCustomerResponse;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;

@WebService(
        targetNamespace = "http://ifpb.com/sistema_pagamentos/clientes",
        name = "ClientService"
)
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface ClientService {

    @WebMethod
    @WebResult(name = "cliente")
    AsaasCustomerResponse createClient(@WebParam(name = "request") AsaasCustomerRequest request);

    @WebMethod
    @WebResult(name = "cliente")
    AsaasCustomerResponse getClientById(@WebParam(name = "clientId") String clientId);
}
