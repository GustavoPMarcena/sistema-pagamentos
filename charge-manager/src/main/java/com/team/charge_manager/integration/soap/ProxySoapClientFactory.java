package com.team.charge_manager.integration.soap;

import jakarta.xml.ws.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import java.net.URL;

/**
 * Small factory to create JAX-WS (SOAP/RPC) clients for Charge Proxy.
 * Uses WSDL URLs published by the proxy container (port 9090 by default).
 */
@Component
public class ProxySoapClientFactory {

    private final String proxySoapBaseUrl;

    public ProxySoapClientFactory(@Value("${proxy.soap.base-url:}") String proxySoapBaseUrl) {
        this.proxySoapBaseUrl = proxySoapBaseUrl;
    }

    public boolean isConfigured() {
        return proxySoapBaseUrl != null && !proxySoapBaseUrl.isBlank();
    }

    public ProxyCustomerSoapClient customerClient() {
        try {
            URL wsdlUrl = new URL(proxySoapBaseUrl + "/ProxyCustomerService?wsdl");
            QName qName = new QName("http://ifpb.com/sistema_pagamentos/proxy", "ProxyCustomerService");
            Service service = Service.create(wsdlUrl, qName);
            return service.getPort(ProxyCustomerSoapClient.class);
        } catch (Exception e) {
            throw new RuntimeException("Charge Proxy SOAP (customer) indisponível em " + proxySoapBaseUrl, e);
        }
    }

    public ProxyPaymentSoapClient paymentClient() {
        try {
            URL wsdlUrl = new URL(proxySoapBaseUrl + "/ProxyPaymentService?wsdl");
            QName qName = new QName("http://ifpb.com/sistema_pagamentos/proxy", "ProxyPaymentService");
            Service service = Service.create(wsdlUrl, qName);
            return service.getPort(ProxyPaymentSoapClient.class);
        } catch (Exception e) {
            throw new RuntimeException("Charge Proxy SOAP (payment) indisponível em " + proxySoapBaseUrl, e);
        }
    }
}
