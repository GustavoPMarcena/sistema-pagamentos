package com.team.charge_manager.persistence.entity;

import java.time.Instant;

/**
 * Entidade de persistência (JDBC) para a tabela clients.
 * Não usa JPA; os valores são mapeados manualmente no repositório.
 */
public class ClientEntity {

    private Long id;
    private String name;
    private String email;
    private String cpf;
    private String asaasCustomerId;
    private Instant createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getAsaasCustomerId() { return asaasCustomerId; }
    public void setAsaasCustomerId(String asaasCustomerId) { this.asaasCustomerId = asaasCustomerId; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
