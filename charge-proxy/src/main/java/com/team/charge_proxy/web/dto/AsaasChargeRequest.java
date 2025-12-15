package com.team.charge_proxy.web.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class AsaasChargeRequest {
    private String customer;
    private BillingType billingType;
    private BigDecimal value;
    private LocalDate dueDate;
    private String description;
}