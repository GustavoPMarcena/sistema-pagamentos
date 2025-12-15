package com.team.charge_proxy.web.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class AsaasChargeResponse {
    private String object;
    private String id;
    private LocalDate dateCreated;
    private String customer;
    private String subscription;
    private String installment;
    private String checkoutSession;
    private String paymentLink;
    private BigDecimal value;
}