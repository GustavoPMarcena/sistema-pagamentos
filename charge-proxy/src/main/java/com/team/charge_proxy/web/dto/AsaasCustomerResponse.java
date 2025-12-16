package com.team.charge_proxy.web.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@XmlRootElement(name = "AsaasCustomerResponse")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter @Setter @NoArgsConstructor
public class AsaasCustomerResponse {

    private String id;
    private String name;
    private String email;
    private String cpfCnpj;
}
