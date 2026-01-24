package com.team.charge_manager.api.dto;

public class ClientResponse {
    private Long id;
    private String name;
    private String email;
    private String cpf;
    private String asaasCustomerId;

    public ClientResponse() {}
    public ClientResponse(Long id, String name, String email, String cpf, String asaasCustomerId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.cpf = cpf;
        this.asaasCustomerId = asaasCustomerId;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getCpf() { return cpf; }
    public String getAsaasCustomerId() { return asaasCustomerId; }
}
