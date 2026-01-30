package com.team.charge_proxy.service;

import com.team.charge_proxy.web.dto.AsaasWebhookEvent;
import org.springframework.stereotype.Service;

@Service
public class PaymentNotificationService {

    private final ManagerNotifier managerNotifier;

    public PaymentNotificationService(ManagerNotifier managerNotifier) {
        this.managerNotifier = managerNotifier;
    }

    public void notifyManager(AsaasWebhookEvent event) {
        if (event == null || event.getPayment() == null)
            return;
        String eventType = event.getEvent();
        if ("PAYMENT_CONFIRMED".equalsIgnoreCase(eventType) || "PAYMENT_RECEIVED".equalsIgnoreCase(eventType)) {
            managerNotifier.notifyPaymentConfirmed(event);
        }
    }
}
