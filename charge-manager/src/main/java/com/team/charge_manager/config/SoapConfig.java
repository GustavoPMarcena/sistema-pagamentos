package com.team.charge_manager.config;

import com.team.charge_manager.integration.soap.PaymentNotificationServiceImpl;
import jakarta.xml.ws.Endpoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração dos endpoints SOAP do Manager.
 * 
 * Expõe o serviço PaymentNotificationService para receber notificações do Proxy via SOAP-RPC.
 * Conforme o diagrama: comunicação SOAP-RPC entre Manager e Proxy.
 */
@Configuration
public class SoapConfig {

    @Value("${server.port:8080}")
    private int serverPort;

    @Bean
    public Endpoint paymentNotificationServiceEndpoint(PaymentNotificationServiceImpl paymentNotificationService) {
        // Publica o endpoint SOAP na mesma porta do servidor (8080)
        // O Proxy vai conectar em: http://payments-manager:8080/soap/PaymentNotificationService?wsdl
        // Usa 0.0.0.0 para aceitar conexões de qualquer interface de rede (importante no Docker)
        String url = String.format("http://0.0.0.0:%d/soap/PaymentNotificationService", serverPort);
        Endpoint endpoint = Endpoint.publish(url, paymentNotificationService);
        System.out.println("SOAP Endpoint publicado em: " + url);
        return endpoint;
    }
}
