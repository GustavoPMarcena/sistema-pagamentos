package com.team.charge_manager.events;

import com.team.charge_manager.domain.ChargeStatus;

/**
 * Evento de domínio disparado sempre que uma cobrança muda de status.
 *
 * Requisito do professor: notificar recebimento de "hook-event" usando Observer Pattern.
 * Aqui usamos o mecanismo de eventos do Spring (ApplicationEventPublisher + @EventListener).
 */
public class ChargeStatusChangedEvent {

    private final Long chargeId;
    private final ChargeStatus newStatus;

    public ChargeStatusChangedEvent(Long chargeId, ChargeStatus newStatus) {
        this.chargeId = chargeId;
        this.newStatus = newStatus;
    }

    public Long getChargeId() {
        return chargeId;
    }

    public ChargeStatus getNewStatus() {
        return newStatus;
    }
}
