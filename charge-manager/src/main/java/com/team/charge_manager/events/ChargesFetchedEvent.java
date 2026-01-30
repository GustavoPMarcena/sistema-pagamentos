package com.team.charge_manager.events;

import com.team.charge_manager.persistence.entity.ChargeEntity;
import java.util.List;

public class ChargesFetchedEvent {
    private final List<ChargeEntity> charges;

    public ChargesFetchedEvent(List<ChargeEntity> charges) {
        this.charges = charges;
    }

    public List<ChargeEntity> getCharges() {
        return charges;
    }
}
