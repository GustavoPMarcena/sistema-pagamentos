package com.team.charge_manager.api.dto;

import com.team.charge_manager.domain.BillingType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CreateChargeRequest {
    @NotNull
    private Long clientId;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal value;

    @NotNull
    private BillingType billingType;

    private LocalDate dueDate;

    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }
    public BigDecimal getValue() { return value; }
    public void setValue(BigDecimal value) { this.value = value; }
    public BillingType getBillingType() { return billingType; }
    public void setBillingType(BillingType billingType) { this.billingType = billingType; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
}
