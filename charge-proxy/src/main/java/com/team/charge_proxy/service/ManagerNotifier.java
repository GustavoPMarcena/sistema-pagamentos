package com.team.charge_proxy.service;

import com.team.charge_proxy.web.dto.AsaasWebhookEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class ManagerNotifier {

    private final RestTemplate restTemplate;
    private final String managerUrl;
    private final String webhookToken;

    public ManagerNotifier(RestTemplate restTemplate,
                           @Value("${manager.url}") String managerUrl,
                           @Value("${asaas.webhook.token}") String webhookToken) {
        this.restTemplate = restTemplate;
        this.managerUrl = managerUrl;
        this.webhookToken = webhookToken;
    }

    public void notifyPaymentConfirmed(AsaasWebhookEvent event) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("paymentId", event.getPayment().getId());
        payload.put("status", event.getPayment().getStatus());
        payload.put("value", event.getPayment().getValue());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + webhookToken);

        restTemplate.postForEntity(
                managerUrl + "/internal/events/payment-confirmed",
                new HttpEntity<>(payload, headers),
                Void.class
        );
    }
}
