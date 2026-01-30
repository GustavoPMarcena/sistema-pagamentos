package com.team.charge_manager.persistence.repo;

import com.team.charge_manager.persistence.entity.ChargeEntity;

import java.util.Optional;

/**
 * Repositório JDBC para cobranças.
 * Implementação: {@link JdbcChargeRepository}
 */
public interface ChargeRepository {
    ChargeEntity save(ChargeEntity charge);

    Optional<ChargeEntity> findById(Long id);

    Optional<ChargeEntity> findByAsaasPaymentId(String asaasPaymentId);

    java.util.List<ChargeEntity> findAll();
}
