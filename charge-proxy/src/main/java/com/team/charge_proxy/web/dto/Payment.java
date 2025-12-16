package com.team.charge_proxy.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Payment {
    private String id;
    private String customer;
    private Double value;
    private String status;
}
