package com.team.charge_proxy.service;


import jakarta.xml.ws.Service;

import javax.xml.namespace.QName;
import java.net.URL;

@org.springframework.stereotype.Service
public class ManagerPaymentSoapClientImpl implements ManagerPaymentSoapClient {

    private ManagerPaymentSoapClient soapClient;

    private ManagerPaymentSoapClient getClient() {
        if (soapClient == null) {
            try {
                URL wsdlUrl = new URL(
                        "http://localhost:8081/soap/PaymentNotificationService?wsdl"
                );

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