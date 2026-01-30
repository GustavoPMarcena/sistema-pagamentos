package com.team.charge_manager.events;

import com.team.charge_manager.persistence.entity.ClientEntity;
import java.util.List;

public class ClientsFetchedEvent {
    private final List<ClientEntity> clients;

    public ClientsFetchedEvent(List<ClientEntity> clients) {
        this.clients = clients;
    }

    public List<ClientEntity> getClients() {
        return clients;
    }
}
