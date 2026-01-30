package com.team.charge_manager.integration.soap;

import com.team.charge_manager.service.ChargeAppService;
import jakarta.jws.WebService;
import org.springframework.stereotype.Component;

/**
 * Implementação SOAP publicada pelo Manager.
 *
 * O Proxy chama este serviço quando recebe um webhook de pagamento confirmado do ASAAS.
 * A implementação delega para o fluxo interno do Manager (ChargeAppService), que por sua vez
 * publica o evento Spring (Observer Pattern) para atualizar status, persistir no BD e notificar por e-mail.
 */
@Component
@WebService(
        serviceName = "PaymentNotificationService",
        portName = "PaymentNotificationServicePort",
        targetNamespace = "http://ifpb.com/sistema_pagamentos/manager",
        endpointInterface = "com.team.charge_manager.integration.soap.PaymentNotificationSoapService"
)
public class PaymentNotificationServiceImpl implements PaymentNotificationSoapService {

    private final ChargeAppService chargeService;

    public PaymentNotificationServiceImpl(ChargeAppService chargeService) {
        this.chargeService = chargeService;
    }

    @Override
    public void notifyPaymentConfirmed(String paymentId, String status, Double value) {
        // Reaproveita o fluxo já existente (usado também pelo endpoint REST interno).
        chargeService.onPaymentConfirmed(paymentId, status, value);
    }
}
