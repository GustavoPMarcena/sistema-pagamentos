package com.team.charge_manager.integration.dto;

public class ProxyCancelResponse {
    private String id;
    private boolean deleted;

    public ProxyCancelResponse() {}
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean deleted) { this.deleted = deleted; }
}
