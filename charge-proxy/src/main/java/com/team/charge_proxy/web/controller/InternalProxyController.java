package com.team.charge_proxy.web.controller;

import com.team.charge_proxy.service.ClientService;
import com.team.charge_proxy.service.PaymentService;
import com.team.charge_proxy.web.dto.AsaasChargeRequest;
import com.team.charge_proxy.web.dto.AsaasChargeResponse;
import com.team.charge_proxy.web.dto.AsaasCustomerRequest;
import com.team.charge_proxy.web.dto.AsaasCustomerResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal")
public class InternalProxyController {

    private final ClientService clientService;
    private final PaymentService paymentService;

    public InternalProxyController(ClientService clientService, PaymentService paymentService) {
        this.clientService = clientService;
        this.paymentService = paymentService;
    }

    @PostMapping("/customers")
    public ResponseEntity<CustomerIdResponse> createCustomer(@RequestBody CustomerCreatePayload payload) {
        AsaasCustomerRequest req = new AsaasCustomerRequest();
        req.setName(payload.getName());
        req.setEmail(payload.getEmail());
        req.setCpfCnpj(payload.getCpf());

        AsaasCustomerResponse created = clientService.createClient(req);
        return ResponseEntity.ok(new CustomerIdResponse(created.getId()));
    }

    @PostMapping("/charges")
    public ResponseEntity<ChargeIdResponse> createCharge(@RequestBody ChargeCreatePayload payload) {
        AsaasChargeRequest req = new AsaasChargeRequest();
        req.setCustomer(payload.getCustomer());
        req.setBillingType(payload.getBillingType());
        req.setValue(payload.getValue());
        if (payload.getDueDate() != null) {
            req.setDueDate(payload.getDueDate().toString());
        }
        req.setDescription(payload.getDescription());

        AsaasChargeResponse created = paymentService.createCharge(req);
        return ResponseEntity.ok(new ChargeIdResponse(created.getId()));
    }

    @DeleteMapping("/charges/{id}")
    public ResponseEntity<Void> deleteCharge(@PathVariable("id") String id) {
        paymentService.deleteCharge(id);
        return ResponseEntity.ok().build();
    }

    // --- simple payload/response DTOs ---

    public static class CustomerCreatePayload {
        private String name;
        private String email;
        private String cpf;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getCpf() { return cpf; }
        public void setCpf(String cpf) { this.cpf = cpf; }
    }

    public static class CustomerIdResponse {
        private String id;
        public CustomerIdResponse() {}
        public CustomerIdResponse(String id) { this.id = id; }
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
    }

    public static class ChargeCreatePayload {
        private String customer;
        private com.team.charge_proxy.web.dto.BillingType billingType;
        private java.math.BigDecimal value;
        private java.time.LocalDate dueDate;
        private String description;

        public String getCustomer() { return customer; }
        public void setCustomer(String customer) { this.customer = customer; }
        public com.team.charge_proxy.web.dto.BillingType getBillingType() { return billingType; }
        public void setBillingType(com.team.charge_proxy.web.dto.BillingType billingType) { this.billingType = billingType; }
        public java.math.BigDecimal getValue() { return value; }
        public void setValue(java.math.BigDecimal value) { this.value = value; }
        public java.time.LocalDate getDueDate() { return dueDate; }
        public void setDueDate(java.time.LocalDate dueDate) { this.dueDate = dueDate; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    public static class ChargeIdResponse {
        private String id;
        public ChargeIdResponse() {}
        public ChargeIdResponse(String id) { this.id = id; }
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
    }
}
