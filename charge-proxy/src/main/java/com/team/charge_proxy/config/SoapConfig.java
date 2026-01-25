package com.team.charge_proxy.config;

import com.team.charge_proxy.soap.ProxyCustomerSoapServiceImpl;
import com.team.charge_proxy.soap.ProxyPaymentSoapServiceImpl;
import jakarta.xml.ws.Endpoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SoapConfig {

    /**
     * Publishes SOAP/RPC endpoints required by the professor diagram:
     * Charge Manager -> Charge Proxy using JAX-WS (SOAP-RPC).
     *
     * NOTE: 0.0.0.0 is required to expose the endpoint inside Docker containers.
     */
    @Bean
    public Endpoint proxyCustomerSoapEndpoint(
            ProxyCustomerSoapServiceImpl customerSoap,
            @Value("${proxy.soap.bind-host:0.0.0.0}") String bindHost,
            @Value("${proxy.soap.port:9090}") int port
    ) {
        return Endpoint.publish(
                "http://" + bindHost + ":" + port + "/soap/ProxyCustomerService",
                customerSoap
        );
    }

    @Bean
    public Endpoint proxyPaymentSoapEndpoint(
            ProxyPaymentSoapServiceImpl paymentSoap,
            @Value("${proxy.soap.bind-host:0.0.0.0}") String bindHost,
            @Value("${proxy.soap.port:9090}") int port
    ) {
        return Endpoint.publish(
                "http://" + bindHost + ":" + port + "/soap/ProxyPaymentService",
                paymentSoap
        );
    }
}
