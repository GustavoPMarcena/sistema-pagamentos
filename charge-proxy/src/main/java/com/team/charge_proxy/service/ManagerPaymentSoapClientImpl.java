package com.team.charge_proxy.service;

import jakarta.xml.ws.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import java.net.URL;

/**
 * Cliente SOAP para comunicação com o Manager.
 * 
 * Conforme o diagrama: comunicação SOAP-RPC entre Proxy e Manager.
 */
@Component
public class ManagerPaymentSoapClientImpl implements ManagerPaymentSoapClient {

    private final String managerUrl;
    private ManagerPaymentSoapClient soapClient;

    public ManagerPaymentSoapClientImpl(@Value("${manager.url:http://payments-manager:8080}") String managerUrl) {
        this.managerUrl = managerUrl;
    }

    private ManagerPaymentSoapClient getClient() {
        if (soapClient == null) {
            try {
                // URL do WSDL do Manager: http://payments-manager:8080/soap/PaymentNotificationService?wsdl
                URL wsdlUrl = new URL(managerUrl + "/soap/PaymentNotificationService?wsdl");

                QName qName = new QName(
                        "http://ifpb.com/sistema_pagamentos/manager",
                        "PaymentNotificationService"
                );

                Service service = Service.create(wsdlUrl, qName);
                soapClient = service.getPort(ManagerPaymentSoapClient.class);

            } catch (Exception e) {
                throw new RuntimeException("Charge Manager indisponível: " + managerUrl, e);
            }
        }
        return soapClient;
    }

    @Override
    public void notifyPaymentConfirmed(String paymentId, String status, Double value) {
        getClient().notifyPaymentConfirmed(paymentId, status, value);
    }
}