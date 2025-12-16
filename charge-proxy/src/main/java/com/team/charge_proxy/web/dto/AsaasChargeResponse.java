package com.team.charge_proxy.web.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@XmlRootElement(name = "AsaasCustomerRequest")
@XmlAccessorType(XmlAccessType.FIELD)
@NoArgsConstructor @Getter @Setter
public class AsaasChargeResponse {
    private String object;
    private String id;
    private String dateCreated;
    private String customer;
    private String subscription;
    private String installment;
    private String checkoutSession;
    private String paymentLink;
    private BigDecimal value;
}