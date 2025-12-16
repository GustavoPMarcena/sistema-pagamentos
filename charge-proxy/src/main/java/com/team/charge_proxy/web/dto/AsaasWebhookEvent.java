package com.team.charge_proxy.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AsaasWebhookEvent {
    private String event;
    private Payment payment;
}
