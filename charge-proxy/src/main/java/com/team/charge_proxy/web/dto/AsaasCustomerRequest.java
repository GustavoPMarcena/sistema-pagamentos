package com.team.charge_proxy.web.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@XmlRootElement(name = "AsaasCustomerRequest")
@XmlAccessorType(XmlAccessType.FIELD)
@NoArgsConstructor @Getter @Setter
public class AsaasCustomerRequest {
    private String name;
    private String cpfCnpj;
    private String email;
}
