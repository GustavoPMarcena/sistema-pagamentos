package com.team.charge_manager.api.dto;

import com.team.charge_manager.domain.BillingType;
import com.team.charge_manager.domain.ChargeStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ChargeResponse {
    private Long id;
    private Long clientId;
    private BigDecimal value;
    private BillingType billingType;
    private ChargeStatus status;
    private LocalDate dueDate;
    private String asaasPaymentId;

    public ChargeResponse() {}
    public ChargeResponse(Long id, Long clientId, BigDecimal value, BillingType billingType, ChargeStatus status, LocalDate dueDate, String asaasPaymentId) {
        this.id = id;
        this.clientId = clientId;
        this.value = value;
        this.billingType = billingType;
        this.status = status;
        this.dueDate = dueDate;
        this.asaasPaymentId = asaasPaymentId;
    }

    public Long getId() { return id; }
    public Long getClientId() { return clientId; }
    public BigDecimal getValue() { return value; }
    public BillingType getBillingType() { return billingType; }
    public ChargeStatus getStatus() { return status; }
    public LocalDate getDueDate() { return dueDate; }
    public String getAsaasPaymentId() { return asaasPaymentId; }
}
