package com.team.charge_proxy.service;

import com.team.charge_proxy.web.dto.AsaasWebhookEvent;
import org.springframework.stereotype.Service;

/**
 * Serviço para notificar o Manager sobre eventos de pagamento.
 *
 * Comunicação via SOAP-RPC conforme o diagrama do projeto.
 * O Observer Pattern é tratado no Manager (via eventos internos).
 */
@Service
public class ManagerNotifier {

    private final ManagerPaymentSoapClient soapClient;

    public ManagerNotifier(ManagerPaymentSoapClientImpl soapClient) {
        this.soapClient = soapClient;
    }

    /**
     * Notifica o Manager sobre pagamento confirmado.
     */
    public void notifyPaymentConfirmed(AsaasWebhookEvent event) {
        if (event == null || event.getPayment() == null) return;

        String paymentId = event.getPayment().getId();
        String status = event.getPayment().getStatus();

        Double value = null;
        if (event.getPayment().getValue() != null) {
            value = event.getPayment().getValue().doubleValue();
        }

        soapClient.notifyPaymentConfirmed(paymentId, status, value);
    }
}
