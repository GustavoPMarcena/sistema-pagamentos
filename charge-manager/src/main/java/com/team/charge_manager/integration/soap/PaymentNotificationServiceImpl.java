package com.team.charge_manager.integration.soap;

import com.team.charge_manager.service.ChargeAppService;
import org.springframework.stereotype.Service;

/**
 * Implementação do serviço SOAP para receber notificações de pagamento do Proxy.
 * 
 * Este serviço recebe notificações via SOAP e delega para o ChargeAppService,
 * que por sua vez dispara eventos usando o Observer Pattern.
 */
@Service
public class PaymentNotificationServiceImpl implements PaymentNotificationService {

    private final ChargeAppService chargeAppService;

    public PaymentNotificationServiceImpl(ChargeAppService chargeAppService) {
        this.chargeAppService = chargeAppService;
    }

    @Override
    public void notifyPaymentConfirmed(String paymentId, String status, Double value) {
        // Delega para o serviço de aplicação que dispara eventos (Observer Pattern)
        chargeAppService.onPaymentConfirmed(paymentId, status, value);
    }
}
