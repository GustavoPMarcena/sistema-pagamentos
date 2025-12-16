package com.team.charge_proxy.service;

import com.team.charge_proxy.web.dto.AsaasWebhookEvent;
import org.springframework.stereotype.Service;

@Service
public class PaymentNotificationService {

    private final ManagerPaymentSoapClient managerPaymentSoapClient;

    public PaymentNotificationService(ManagerPaymentSoapClient managerPaymentSoapClient) {
        this.managerPaymentSoapClient = managerPaymentSoapClient;
    }

    public void notifyManager(AsaasWebhookEvent event) {

        if ("PAYMENT_CONFIRMED".equals(event.getEvent())) {
            managerPaymentSoapClient.notifyPaymentConfirmed(
                    event.getPayment().getId(),
                    event.getPayment().getStatus(),
                    event.getPayment().getValue()
            );
        }
    }
}
