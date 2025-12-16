package com.team.charge_proxy.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhook/asaas")
public class AsaasWebhookController {

    @PostMapping
    public ResponseEntity<Void> receive(@RequestBody AsaasWebhookEvent event) {
        // valida evento
        // converte para modelo interno
        // notifica o Charge Manager
        return ResponseEntity.ok().build();
    }
}
