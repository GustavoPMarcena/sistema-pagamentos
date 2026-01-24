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
        this.webhookToken = webhookToken;
    }

    @PostMapping
    public ResponseEntity<Void> receive(
            @RequestHeader(value = "Authorization", required = false) String auth,
            @RequestBody AsaasWebhookEvent event
    ) {
        if (!isValidToken(auth)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        System.out.println("Evento recebido -> " + (event != null ? event.getEvent() : null));
        paymentNotificationService.notifyManager(event);
        return ResponseEntity.ok().build();
    }

    private boolean isValidToken(String auth) {
        if (auth == null) return false;
        String prefix = "Bearer ";
        if (!auth.startsWith(prefix)) return false;
        String token = auth.substring(prefix.length()).trim();
        return token.equals(webhookToken);
    }
}
