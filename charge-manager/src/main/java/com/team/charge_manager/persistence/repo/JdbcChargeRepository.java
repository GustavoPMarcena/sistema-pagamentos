package com.team.charge_manager.persistence.repo;

import com.team.charge_manager.domain.BillingType;
import com.team.charge_manager.domain.ChargeStatus;
import com.team.charge_manager.persistence.entity.ChargeEntity;
import com.team.charge_manager.persistence.entity.ClientEntity;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

@Repository
public class JdbcChargeRepository implements ChargeRepository {

    private final NamedParameterJdbcTemplate jdbc;

    public JdbcChargeRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private static final RowMapper<ChargeEntity> CHARGE_WITH_CLIENT_MAPPER = new RowMapper<>() {
        @Override
        public ChargeEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            ClientEntity client = new ClientEntity();
            client.setId(rs.getLong("c_id"));
            client.setName(rs.getString("c_name"));
            client.setEmail(rs.getString("c_email"));
            client.setCpf(rs.getString("c_cpf"));
            client.setAsaasCustomerId(rs.getString("c_asaas_customer_id"));
            Timestamp cCreated = rs.getTimestamp("c_created_at");
            if (cCreated != null)
                client.setCreatedAt(cCreated.toInstant());

            ChargeEntity charge = new ChargeEntity();
            charge.setId(rs.getLong("ch_id"));
            charge.setClient(client);
            charge.setValue(rs.getBigDecimal("ch_value"));
            charge.setBillingType(BillingType.valueOf(rs.getString("ch_billing_type")));
            charge.setStatus(ChargeStatus.valueOf(rs.getString("ch_status")));
            java.sql.Date due = rs.getDate("ch_due_date");
            if (due != null)
                charge.setDueDate(due.toLocalDate());
            charge.setAsaasPaymentId(rs.getString("ch_asaas_payment_id"));

            Timestamp created = rs.getTimestamp("ch_created_at");
            Timestamp updated = rs.getTimestamp("ch_updated_at");
            if (created != null)
                charge.setCreatedAt(created.toInstant());
            if (updated != null)
                charge.setUpdatedAt(updated.toInstant());
            return charge;
        }
    };

    @Override
    public ChargeEntity save(ChargeEntity charge) {
        if (charge.getClient() == null || charge.getClient().getId() == null) {
            throw new IllegalArgumentException("ChargeEntity.client.id é obrigatório");
        }

        if (charge.getId() == null) {
            String sql = """
                        INSERT INTO charges (client_id, value, billing_type, status, due_date, asaas_payment_id, created_at, updated_at)
                        VALUES (:client_id, :value, :billing_type, :status, :due_date, :asaas_payment_id, NOW(), NOW())
                    """;
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("client_id", charge.getClient().getId())
                    .addValue("value", charge.getValue())
                    .addValue("billing_type", charge.getBillingType().name())
                    .addValue("status", charge.getStatus().name())
                    .addValue("due_date", charge.getDueDate())
                    .addValue("asaas_payment_id", charge.getAsaasPaymentId());

            KeyHolder kh = new GeneratedKeyHolder();
            jdbc.update(sql, params, kh, new String[] { "id" });
            Number id = kh.getKey();
            if (id != null)
                charge.setId(id.longValue());
        } else {
            String sql = """
                        UPDATE charges SET
                            client_id = :client_id,
                            value = :value,
                            billing_type = :billing_type,
                            status = :status,
                            due_date = :due_date,
                            asaas_payment_id = :asaas_payment_id,
                            updated_at = NOW()
                        WHERE id = :id
                    """;
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("id", charge.getId())
                    .addValue("client_id", charge.getClient().getId())
                    .addValue("value", charge.getValue())
                    .addValue("billing_type", charge.getBillingType().name())
                    .addValue("status", charge.getStatus().name())
                    .addValue("due_date", charge.getDueDate())
                    .addValue("asaas_payment_id", charge.getAsaasPaymentId());
            jdbc.update(sql, params);
        }

        // Recarrega para preencher timestamps e client completo
        return findById(charge.getId()).orElse(charge);
    }

    @Override
    public Optional<ChargeEntity> findById(Long id) {
        String sql = """
                    SELECT
                      ch.id as ch_id, ch.value as ch_value, ch.billing_type as ch_billing_type, ch.status as ch_status,
                      ch.due_date as ch_due_date, ch.asaas_payment_id as ch_asaas_payment_id, ch.created_at as ch_created_at, ch.updated_at as ch_updated_at,
                      c.id as c_id, c.name as c_name, c.email as c_email, c.cpf as c_cpf, c.asaas_customer_id as c_asaas_customer_id, c.created_at as c_created_at
                    FROM charges ch
                    JOIN clients c ON c.id = ch.client_id
                    WHERE ch.id = :id
                """;
        var rows = jdbc.query(sql, new MapSqlParameterSource("id", id), CHARGE_WITH_CLIENT_MAPPER);
        return rows.stream().findFirst();
    }

    @Override
    public Optional<ChargeEntity> findByAsaasPaymentId(String asaasPaymentId) {
        String sql = """
                    SELECT
                      ch.id as ch_id, ch.value as ch_value, ch.billing_type as ch_billing_type, ch.status as ch_status,
                      ch.due_date as ch_due_date, ch.asaas_payment_id as ch_asaas_payment_id, ch.created_at as ch_created_at, ch.updated_at as ch_updated_at,
                      c.id as c_id, c.name as c_name, c.email as c_email, c.cpf as c_cpf, c.asaas_customer_id as c_asaas_customer_id, c.created_at as c_created_at
                    FROM charges ch
                    JOIN clients c ON c.id = ch.client_id
                    WHERE ch.asaas_payment_id = :pid
                """;
        var rows = jdbc.query(sql, new MapSqlParameterSource("pid", asaasPaymentId), CHARGE_WITH_CLIENT_MAPPER);
        return rows.stream().findFirst();
    }

    @Override
    public java.util.List<ChargeEntity> findAll() {
        String sql = """
                    SELECT
                      ch.id as ch_id, ch.value as ch_value, ch.billing_type as ch_billing_type, ch.status as ch_status,
                      ch.due_date as ch_due_date, ch.asaas_payment_id as ch_asaas_payment_id, ch.created_at as ch_created_at, ch.updated_at as ch_updated_at,
                      c.id as c_id, c.name as c_name, c.email as c_email, c.cpf as c_cpf, c.asaas_customer_id as c_asaas_customer_id, c.created_at as c_created_at
                    FROM charges ch
                    JOIN clients c ON c.id = ch.client_id
                """;
        return jdbc.query(sql, CHARGE_WITH_CLIENT_MAPPER);
    }
}
