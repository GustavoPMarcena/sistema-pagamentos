package com.team.charge_manager.persistence.repo;

import com.team.charge_manager.persistence.entity.ClientEntity;

import java.util.Optional;

/**
 * Repositório JDBC para clientes.
 * Implementação: {@link JdbcClientRepository}
 */
public interface ClientRepository {
    ClientEntity save(ClientEntity client);

    Optional<ClientEntity> findById(Long id);

    Optional<ClientEntity> findByEmail(String email);

    java.util.List<ClientEntity> findAll();
}
