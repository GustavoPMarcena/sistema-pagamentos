package com.team.charge_manager.integration.dto;

public class ProxyChargeResponse {
    private String id;

    public ProxyChargeResponse() {}
    public ProxyChargeResponse(String id) { this.id = id; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
}
