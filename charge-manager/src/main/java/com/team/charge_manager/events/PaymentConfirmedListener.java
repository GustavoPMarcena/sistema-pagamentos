package com.team.charge_manager.events;

import com.team.charge_manager.domain.ChargeStatus;
import com.team.charge_manager.persistence.entity.ChargeEntity;
import com.team.charge_manager.persistence.repo.ChargeRepository;
import com.team.charge_manager.service.MailService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PaymentConfirmedListener {

    private final ChargeRepository chargeRepository;
    private final MailService mailService;

    public PaymentConfirmedListener(ChargeRepository chargeRepository, MailService mailService) {
        this.chargeRepository = chargeRepository;
        this.mailService = mailService;
    }

    @EventListener
    @Transactional
    public void handle(PaymentConfirmedEvent event) {
        ChargeEntity charge = chargeRepository.findByAsaasPaymentId(event.getAsaasPaymentId())
                .orElseThrow(() -> new IllegalArgumentException("Cobrança não encontrada pelo asaasPaymentId"));

        charge.setStatus(ChargeStatus.PAID);
        chargeRepository.save(charge);

        String email = charge.getClient().getEmail();
        String body = "Seu pagamento foi confirmado. Cobrança ID=" + charge.getId()
                + " | asaasPaymentId=" + event.getAsaasPaymentId();

        mailService.send(email, "Pagamento confirmado", body);
    }
}
