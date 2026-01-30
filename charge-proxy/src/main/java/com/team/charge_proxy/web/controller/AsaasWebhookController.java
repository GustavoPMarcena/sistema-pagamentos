package com.team.charge_proxy.web.controller;

import com.team.charge_proxy.service.PaymentNotificationService;
import com.team.charge_proxy.web.dto.AsaasWebhookEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook/asaas")
public class AsaasWebhookController {

    private final PaymentNotificationService paymentNotificationService;
    private final String webhookToken;

    public AsaasWebhookController(PaymentNotificationService paymentNotificationService,
            @Value("${asaas.webhook.token}") String webhookToken) {
        this.paymentNotificationService = paymentNotificationService;
        this.webhookToken = webhookToken != null ? webhookToken.trim() : null;
    }

    @PostMapping
    public ResponseEntity<Void> receive(
            @RequestHeader(value = "asaas-access-token", required = false) String asaasToken,
            @RequestHeader(value = "Authorization", required = false) String auth,
            @RequestBody AsaasWebhookEvent event) {
        System.out.println("DEBUG WEBHOOK -> Recebido asaas-access-token: " + asaasToken);
        System.out.println("DEBUG WEBHOOK -> Recebido Authorization: " + auth);
        System.out.println("DEBUG WEBHOOK -> Token esperado (config): [" + webhookToken + "]");

        if (!isValidToken(asaasToken) && !isValidToken(extractBearer(auth))) {
            System.out.println("DEBUG WEBHOOK -> FALHA DE AUTENTICAÇÃO!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        System.out.println("Evento recebido -> " + (event != null ? event.getEvent() : null));
        paymentNotificationService.notifyManager(event);
        return ResponseEntity.ok().build();
    }

    private boolean isValidToken(String token) {
        if (token == null)
            return false;
        return token.trim().equals(webhookToken);
    }

    private String extractBearer(String auth) {
        if (auth == null || !auth.startsWith("Bearer "))
            return null;
        return auth.substring(7);
    }
}
