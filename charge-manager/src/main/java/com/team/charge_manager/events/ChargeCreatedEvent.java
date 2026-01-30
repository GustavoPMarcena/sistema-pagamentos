package com.team.charge_manager.events;

import com.team.charge_manager.persistence.entity.ChargeEntity;

public class ChargeCreatedEvent {
    private final ChargeEntity charge;

    public ChargeCreatedEvent(ChargeEntity charge) {
        this.charge = charge;
    }

    public ChargeEntity getCharge() {
        return charge;
    }
}
