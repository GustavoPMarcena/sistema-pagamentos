package com.team.charge_manager.events;

import com.team.charge_manager.domain.ChargeStatus;
import com.team.charge_manager.persistence.entity.ChargeEntity;
import com.team.charge_manager.persistence.repo.ChargeRepository;
import com.team.charge_manager.service.MailService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Observador que envia e-mail ao cliente quando o status da cobrança muda.
 *
 * Requisito funcional [F5]: "Enviar email para o cliente notificando as alterações dos status".
 */
@Component
public class ChargeStatusChangedListener {

    private final ChargeRepository chargeRepository;
    private final MailService mailService;

    public ChargeStatusChangedListener(ChargeRepository chargeRepository, MailService mailService) {
        this.chargeRepository = chargeRepository;
        this.mailService = mailService;
    }

    @EventListener
    @Transactional(readOnly = true)
    public void handle(ChargeStatusChangedEvent event) {
        // O status PAID já é tratado no PaymentConfirmedListener para manter a mensagem mais específica.
        if (event.getNewStatus() == ChargeStatus.PAID) {
            return;
        }

        ChargeEntity charge = chargeRepository.findById(event.getChargeId())
                .orElseThrow(() -> new IllegalArgumentException("Cobrança não encontrada"));

        String email = charge.getClient().getEmail();
        String subject = "Atualização de cobrança";
        String body = "Sua cobrança foi atualizada para o status: " + event.getNewStatus()
                + "\n\n" +
                "Cobrança ID=" + charge.getId()
                + " | Tipo=" + charge.getBillingType()
                + " | Vencimento=" + charge.getDueDate()
                + " | Valor=" + charge.getValue()
                + (charge.getAsaasPaymentId() != null ? " | asaasPaymentId=" + charge.getAsaasPaymentId() : "");

        mailService.send(email, subject, body);
    }
}
