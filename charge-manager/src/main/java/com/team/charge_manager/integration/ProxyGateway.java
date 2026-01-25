package com.team.charge_manager.integration;

import com.team.charge_manager.integration.dto.ProxyChargeRequest;
import com.team.charge_manager.integration.dto.ProxyChargeResponse;
import com.team.charge_manager.integration.dto.ProxyCustomerRequest;
import com.team.charge_manager.integration.dto.ProxyCustomerResponse;
import com.team.charge_manager.integration.soap.ProxySoapClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Integration gateway for Charge Proxy.
 *
 * Default: REST (/internal/*)
 * Optional: SOAP/RPC (required by the professor diagram) when proxy.soap.enabled=true
 * and proxy.soap.base-url is configured.
 */
@Component
public class ProxyGateway {

    private final RestTemplate restTemplate;
    private final String proxyBaseUrl;
    private final ProxySoapClientFactory soapFactory;
    private final boolean soapEnabled;

    public ProxyGateway(
            RestTemplate restTemplate,
            @Value("${proxy.url}") String proxyBaseUrl,
            ProxySoapClientFactory soapFactory,
            @Value("${proxy.soap.enabled:false}") boolean soapEnabled
    ) {
        this.restTemplate = restTemplate;
        this.proxyBaseUrl = proxyBaseUrl;
        this.soapFactory = soapFactory;
        this.soapEnabled = soapEnabled;
    }

    public ProxyCustomerResponse createCustomer(ProxyCustomerRequest request) {
        if (soapEnabled && soapFactory.isConfigured()) {
            String id = soapFactory.customerClient().createCustomer(
                    request.getName(),
                    request.getEmail(),
                    request.getCpf()
            );
            return new ProxyCustomerResponse(id);
        }

        ResponseEntity<ProxyCustomerResponse> resp = restTemplate.postForEntity(
                proxyBaseUrl + "/internal/customers",
                request,
                ProxyCustomerResponse.class
        );
        return resp.getBody();
    }

    public ProxyChargeResponse createCharge(ProxyChargeRequest request) {
        if (soapEnabled && soapFactory.isConfigured()) {
            String id = soapFactory.paymentClient().createCharge(
                    request.getCustomer(),
                    request.getValue() != null ? request.getValue().doubleValue() : null,
                    request.getBillingType() != null ? request.getBillingType().name() : null,
                    request.getDueDate() != null ? request.getDueDate().toString() : null
            );
            return new ProxyChargeResponse(id);
        }

        ResponseEntity<ProxyChargeResponse> resp = restTemplate.postForEntity(
                proxyBaseUrl + "/internal/charges",
                request,
                ProxyChargeResponse.class
        );
        return resp.getBody();
    }

    public void cancelCharge(String asaasPaymentId) {
        if (soapEnabled && soapFactory.isConfigured()) {
            soapFactory.paymentClient().cancelCharge(asaasPaymentId);
            return;
        }
        restTemplate.delete(proxyBaseUrl + "/internal/charges/" + asaasPaymentId);
    }
}
