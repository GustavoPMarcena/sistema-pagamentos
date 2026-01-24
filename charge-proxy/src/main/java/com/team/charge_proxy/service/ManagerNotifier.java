package com.team.charge_proxy.service;

import com.team.charge_proxy.web.dto.AsaasWebhookEvent;
import org.springframework.stereotype.Service;

/**
 * Serviço para notificar o Manager sobre eventos de pagamento.
 * 
 * Usa comunicação SOAP-RPC conforme o diagrama do projeto.
 * O Observer Pattern é implementado no Manager através de eventos Spring.
 */
@Service
public class ManagerNotifier {

    private final ManagerPaymentSoapClient soapClient;

    public ManagerNotifier(ManagerPaymentSoapClientImpl soapClient) {
        this.soapClient = soapClient;
    }

    /**
     * Notifica o Manager sobre um pagamento confirmado via SOAP.
     * 
     * O Manager recebe a notificação e dispara eventos usando Observer Pattern.
     */
    public void notifyPaymentConfirmed(AsaasWebhookEvent event) {
        if (event == null || event.getPayment() == null) {
            return;
        }

        String paymentId = event.getPayment().getId();
        String status = event.getPayment().getStatus();
        Double value = event.getPayment().getValue() != null 
                ? event.getPayment().getValue().doubleValue() 
                : null;

        // Notifica via SOAP-RPC (conforme diagrama)
        soapClient.notifyPaymentConfirmed(paymentId, status, value);
    }
}
