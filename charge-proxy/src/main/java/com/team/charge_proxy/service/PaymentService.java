package com.team.charge_proxy.service;

import com.team.charge_proxy.web.dto.AsaasChargeDeleteResponse;
import com.team.charge_proxy.web.dto.AsaasChargeRequest;
import com.team.charge_proxy.web.dto.AsaasChargeResponse;
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
public interface PaymentService {

    @WebMethod
    @WebResult(name = "pagamento")
    AsaasChargeResponse createCharge(@WebParam(name = "request") AsaasChargeRequest request);

    @WebMethod
    @WebResult(name = "pagamento")
    AsaasChargeDeleteResponse deleteCharge(@WebParam(name = "chargeId") String chargeId);
}
