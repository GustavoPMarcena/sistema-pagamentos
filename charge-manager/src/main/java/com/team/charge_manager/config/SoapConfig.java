package com.team.charge_manager.config;

import com.team.charge_manager.integration.soap.PaymentNotificationServiceImpl;
import jakarta.xml.ws.Endpoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Publica o serviço SOAP do Charge Manager.
 *
 * O Proxy usa esse endpoint para notificar o Manager quando uma cobrança for confirmada
 * (evento vindo do webhook do ASAAS). Isso mantém a comunicação Manager <-> Proxy via
 * SOAP-RPC conforme o escopo do projeto.
 */
@Configuration
public class SoapConfig {

    /**
     * Host/porta em que o serviço SOAP será publicado.
     *
     * Por padrão usamos uma porta separada do Tomcat (REST), pois o publish do JAX-WS RI
     * abre seu próprio servidor HTTP.
     */
    @Bean
    public Endpoint paymentNotificationServiceEndpoint(
            PaymentNotificationServiceImpl implementor,
            @Value("${manager.soap.bind-host:0.0.0.0}") String bindHost,
            @Value("${manager.soap.port:9091}") int port
    ) {
        // Ex.: http://0.0.0.0:9091/soap/PaymentNotificationService
        return Endpoint.publish(
                "http://" + bindHost + ":" + port + "/soap/PaymentNotificationService",
                implementor
        );
    }
}
