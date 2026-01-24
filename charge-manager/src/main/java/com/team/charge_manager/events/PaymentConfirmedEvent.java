package com.team.charge_manager.events;

public class PaymentConfirmedEvent {
    private final String asaasPaymentId;
    private final String status;
    private final Double value;

    public PaymentConfirmedEvent(String asaasPaymentId, String status, Double value) {
        this.asaasPaymentId = asaasPaymentId;
        this.status = status;
        this.value = value;
    }

    public String getAsaasPaymentId() { return asaasPaymentId; }
    public String getStatus() { return status; }
    public Double getValue() { return value; }
}
