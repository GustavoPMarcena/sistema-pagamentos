package com.team.charge_manager.integration.dto;

import com.team.charge_manager.domain.BillingType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProxyChargeRequest {
    private String customer;
    private BigDecimal value;
    private BillingType billingType;
    private LocalDate dueDate;

    public ProxyChargeRequest() {}
    public ProxyChargeRequest(String customer, BigDecimal value, BillingType billingType, LocalDate dueDate) {
        this.customer = customer;
        this.value = value;
        this.billingType = billingType;
        this.dueDate = dueDate;
    }

    public String getCustomer() { return customer; }
    public BigDecimal getValue() { return value; }
    public BillingType getBillingType() { return billingType; }
    public LocalDate getDueDate() { return dueDate; }
}
