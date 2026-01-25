package com.team.charge_manager.integration.dto;

public class ProxyCustomerResponse {
    private String id;

    public ProxyCustomerResponse() {}
    public ProxyCustomerResponse(String id) { this.id = id; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
}
