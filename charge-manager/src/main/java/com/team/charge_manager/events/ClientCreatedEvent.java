package com.team.charge_manager.events;

import com.team.charge_manager.persistence.entity.ClientEntity;

public class ClientCreatedEvent {
    private final ClientEntity client;

    public ClientCreatedEvent(ClientEntity client) {
        this.client = client;
    }

    public ClientEntity getClient() {
        return client;
    }
}
