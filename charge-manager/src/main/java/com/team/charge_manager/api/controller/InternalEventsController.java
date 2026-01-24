package com.team.charge_manager.api.controller;

import com.team.charge_manager.service.ChargeAppService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/events")
public class InternalEventsController {

    private final ChargeAppService chargeService;
    private final String webhookToken;

    public InternalEventsController(ChargeAppService chargeService, @Value("${webhook.token}") String webhookToken) {
        this.chargeService = chargeService;
        this.webhookToken = webhookToken;
    }

    @PostMapping("/payment-confirmed")
    public ResponseEntity<Void> paymentConfirmed(
            @RequestHeader(value = "Authorization", required = false) String auth,
            @Valid @RequestBody PaymentConfirmedPayload payload
    ) {
        if (!isValidToken(auth)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        chargeService.onPaymentConfirmed(payload.getPaymentId(), payload.getStatus(), payload.getValue());
        return ResponseEntity.ok().build();
    }

    private boolean isValidToken(String auth) {
        if (auth == null) return false;
        String prefix = "Bearer ";
        if (!auth.startsWith(prefix)) return false;
        String token = auth.substring(prefix.length()).trim();
        return token.equals(webhookToken);
    }

    public static class PaymentConfirmedPayload {
        @NotBlank
        private String paymentId;
        @NotBlank
        private String status;
        private Double value;

        public String getPaymentId() { return paymentId; }
        public void setPaymentId(String paymentId) { this.paymentId = paymentId; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public Double getValue() { return value; }
        public void setValue(Double value) { this.value = value; }
    }
}
