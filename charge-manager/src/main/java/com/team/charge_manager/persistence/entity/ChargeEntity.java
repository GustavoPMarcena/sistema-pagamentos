package com.team.charge_manager.persistence.entity;

import com.team.charge_manager.domain.BillingType;
import com.team.charge_manager.domain.ChargeStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

/**
 * Entidade de persistência (JDBC) para a tabela charges.
 * Não usa JPA; os valores são mapeados manualmente no repositório.
 */
public class ChargeEntity {

    private Long id;
    private ClientEntity client;

    private BigDecimal value;
    private BillingType billingType;
    private ChargeStatus status;
    private LocalDate dueDate;

    private String asaasPaymentId;

    private Instant createdAt;
    private Instant updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public ClientEntity getClient() { return client; }
    public void setClient(ClientEntity client) { this.client = client; }

    public BigDecimal getValue() { return value; }
    public void setValue(BigDecimal value) { this.value = value; }

    public BillingType getBillingType() { return billingType; }
    public void setBillingType(BillingType billingType) { this.billingType = billingType; }

    public ChargeStatus getStatus() { return status; }
    public void setStatus(ChargeStatus status) { this.status = status; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public String getAsaasPaymentId() { return asaasPaymentId; }
    public void setAsaasPaymentId(String asaasPaymentId) { this.asaasPaymentId = asaasPaymentId; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
