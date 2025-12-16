package com.team.charge_proxy.web.controller;

import com.team.charge_proxy.service.PaymentNotificationService;
import com.team.charge_proxy.web.dto.AsaasWebhookEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhook/asaas")
public class AsaasWebhookController {

    private final PaymentNotificationService paymentNotificationService;

    public AsaasWebhookController(PaymentNotificationService paymentNotificationService) {
        this.paymentNotificationService = paymentNotificationService;
    }

    @PostMapping
    public ResponseEntity<Void> receive(@RequestBody AsaasWebhookEvent event) {
        System.out.println("Evento recebido -> " + event.getEvent());
        paymentNotificationService.notifyManager(event);
        return ResponseEntity.ok().build();
    }
}
