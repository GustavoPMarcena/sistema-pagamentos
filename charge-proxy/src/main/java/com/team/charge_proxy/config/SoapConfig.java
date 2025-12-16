package com.team.charge_proxy.config;
import com.team.charge_proxy.service.ClientServiceImpl;
import com.team.charge_proxy.service.PaymentServiceImpl;
import jakarta.xml.ws.Endpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SoapConfig {
    @Bean
    public Endpoint clientServiceEndpoint(ClientServiceImpl clientService) {
        return Endpoint.publish(
                "http://localhost:9090/soap/ClientService",
                clientService
        );
    }

    @Bean
    public Endpoint paymentServiceEndpoint(PaymentServiceImpl paymentService) {
        return Endpoint.publish(
                    "http://localhost:9090/soap/PaymentService",
                paymentService
        );
    }
}
