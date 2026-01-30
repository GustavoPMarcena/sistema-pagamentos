package com.team.charge_proxy.service;


import jakarta.xml.ws.Service;

import javax.xml.namespace.QName;
import java.net.URL;

@org.springframework.stereotype.Service
public class ManagerPaymentSoapClientImpl implements ManagerPaymentSoapClient {

    private ManagerPaymentSoapClient soapClient;

    /**
     * URL do WSDL do Manager dentro do Swarm.
     *
     * Importante: dentro do container, "localhost" aponta para o próprio Proxy.
     * Por isso o default deve apontar para o serviço do Manager na rede do Swarm.
     */
    private final String wsdlUrl;

    public ManagerPaymentSoapClientImpl(
            @org.springframework.beans.factory.annotation.Value(
                    "${manager.soap.wsdl-url:http://payments-manager:9091/soap/PaymentNotificationService?wsdl}"
            ) String wsdlUrl
    ) {
        this.wsdlUrl = wsdlUrl;
    }

    private ManagerPaymentSoapClient getClient() {
        if (soapClient == null) {
            try {
                URL wsdlUrl = new URL(this.wsdlUrl);

                QName qName = new QName(
                        "http://ifpb.com/sistema_pagamentos/manager",
                        "PaymentNotificationService"
                );

                Service service = Service.create(wsdlUrl, qName);
                soapClient = service.getPort(ManagerPaymentSoapClient.class);

            } catch (Exception e) {
                throw new RuntimeException("Charge Manager indisponível", e);
            }
        }
        return soapClient;
    }

    @Override
    public void notifyPaymentConfirmed(String paymentId, String status, Double value) {
        getClient().notifyPaymentConfirmed(paymentId, status, value);
    }
}