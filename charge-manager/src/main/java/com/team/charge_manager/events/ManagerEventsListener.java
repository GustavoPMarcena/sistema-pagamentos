package com.team.charge_manager.events;

import com.team.charge_manager.service.MailService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ManagerEventsListener {

    private final MailService mailService;

    public ManagerEventsListener(MailService mailService) {
        this.mailService = mailService;
    }

    @EventListener
    public void onClientCreated(ClientCreatedEvent event) {
        System.out.println("MANAGER-EVENT -> Cliente Criado: " + event.getClient().getName() + " (Email: "
                + event.getClient().getEmail() + ")");
    }

    @EventListener
    public void onClientsFetched(ClientsFetchedEvent event) {
        System.out.println("MANAGER-EVENT -> Listagem de Clientes Solicitada. Total: " + event.getClients().size());
    }

    @EventListener
    public void onChargeCreated(ChargeCreatedEvent event) {
        System.out.println("MANAGER-EVENT -> Cobrança Criada: ID Local=" + event.getCharge().getId() +
                ", Valor=" + event.getCharge().getValue() + ", Cliente=" + event.getCharge().getClient().getName());

        // F5 Requirement: Send email on status change (Created/Registered)
        String clientEmail = event.getCharge().getClient().getEmail();
        String subject = "Nova cobrança gerada";
        String body = String.format("Olá %s, uma nova cobrança de R$ %.2f foi gerada com o vencimento em %s.",
                event.getCharge().getClient().getName(),
                event.getCharge().getValue(),
                event.getCharge().getDueDate());

        mailService.send(clientEmail, subject, body);
    }

    @EventListener
    public void onChargesFetched(ChargesFetchedEvent event) {
        System.out.println("MANAGER-EVENT -> Listagem de Cobranças Solicitada. Total: " + event.getCharges().size());
    }
}
