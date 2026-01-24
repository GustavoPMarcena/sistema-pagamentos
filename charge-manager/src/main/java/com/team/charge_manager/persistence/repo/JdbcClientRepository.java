package com.team.charge_manager.persistence.repo;

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
import java.util.Optional;

@Repository
public class JdbcClientRepository implements ClientRepository {

    private final NamedParameterJdbcTemplate jdbc;

    public JdbcClientRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private static final RowMapper<ClientEntity> CLIENT_MAPPER = new RowMapper<>() {
        @Override
        public ClientEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            ClientEntity c = new ClientEntity();
            c.setId(rs.getLong("id"));
            c.setName(rs.getString("name"));
            c.setEmail(rs.getString("email"));
            c.setCpf(rs.getString("cpf"));
            c.setAsaasCustomerId(rs.getString("asaas_customer_id"));
            Timestamp created = rs.getTimestamp("created_at");
            if (created != null) c.setCreatedAt(created.toInstant());
            return c;
        }
    };

    @Override
    public ClientEntity save(ClientEntity client) {
        if (client.getId() == null) {
            String sql = """ 
                INSERT INTO clients (name, email, cpf, asaas_customer_id, created_at)
                VALUES (:name, :email, :cpf, :asaas_customer_id, NOW())
            """;
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("name", client.getName())
                    .addValue("email", client.getEmail())
                    .addValue("cpf", client.getCpf())
                    .addValue("asaas_customer_id", client.getAsaasCustomerId());
            KeyHolder kh = new GeneratedKeyHolder();
            jdbc.update(sql, params, kh, new String[]{"id"});
            Number id = kh.getKey();
            if (id != null) client.setId(id.longValue());
        } else {
            String sql = """ 
                UPDATE clients SET
                  name = :name,
                  email = :email,
                  cpf = :cpf,
                  asaas_customer_id = :asaas_customer_id
                WHERE id = :id
            """;
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("id", client.getId())
                    .addValue("name", client.getName())
                    .addValue("email", client.getEmail())
                    .addValue("cpf", client.getCpf())
                    .addValue("asaas_customer_id", client.getAsaasCustomerId());
            jdbc.update(sql, params);
        }
        return findById(client.getId()).orElse(client);
    }

    @Override
    public Optional<ClientEntity> findById(Long id) {
        String sql = "SELECT * FROM clients WHERE id = :id";
        var rows = jdbc.query(sql, new MapSqlParameterSource("id", id), CLIENT_MAPPER);
        return rows.stream().findFirst();
    }

    @Override
    public Optional<ClientEntity> findByEmail(String email) {
        String sql = "SELECT * FROM clients WHERE email = :email";
        var rows = jdbc.query(sql, new MapSqlParameterSource("email", email), CLIENT_MAPPER);
        return rows.stream().findFirst();
    }
}
