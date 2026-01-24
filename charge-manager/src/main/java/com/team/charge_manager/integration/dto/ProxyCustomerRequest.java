package com.team.charge_manager.integration.dto;

public class ProxyCustomerRequest {
    private String name;
    private String email;
    private String cpf;

    public ProxyCustomerRequest() {}
    public ProxyCustomerRequest(String name, String email, String cpf) {
        this.name = name;
        this.email = email;
        this.cpf = cpf;
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getCpf() { return cpf; }
}
