package com.team.charge_manager.integration;

import com.team.charge_manager.integration.dto.ProxyChargeRequest;
import com.team.charge_manager.integration.dto.ProxyChargeResponse;
import com.team.charge_manager.integration.dto.ProxyCustomerRequest;
import com.team.charge_manager.integration.dto.ProxyCustomerResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ProxyGateway {

    private final RestTemplate restTemplate;
    private final String proxyBaseUrl;

    public ProxyGateway(RestTemplate restTemplate, @Value("${proxy.url}") String proxyBaseUrl) {
        this.restTemplate = restTemplate;
        this.proxyBaseUrl = proxyBaseUrl;
    }

    public ProxyCustomerResponse createCustomer(ProxyCustomerRequest request) {
        ResponseEntity<ProxyCustomerResponse> resp = restTemplate.postForEntity(
                proxyBaseUrl + "/internal/customers",
                request,
                ProxyCustomerResponse.class
        );
        return resp.getBody();
    }

    public ProxyChargeResponse createCharge(ProxyChargeRequest request) {
        ResponseEntity<ProxyChargeResponse> resp = restTemplate.postForEntity(
                proxyBaseUrl + "/internal/charges",
                request,
                ProxyChargeResponse.class
        );
        return resp.getBody();
    }

    public void cancelCharge(String asaasPaymentId) {
        restTemplate.delete(proxyBaseUrl + "/internal/charges/" + asaasPaymentId);
    }
}
