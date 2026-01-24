package com.team.charge_manager.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final JavaMailSender javaMailSender;
    private final String from;
    private final boolean enabled;

    public MailService(JavaMailSender javaMailSender,
                       @Value("${spring.mail.username:}") String from,
                       @Value("${spring.mail.host:}") String host) {
        this.javaMailSender = javaMailSender;
        this.from = from;
        this.enabled = host != null && !host.isBlank();
    }

    public void send(String to, String subject, String body) {
        if (!enabled) {
            System.out.println("EMAIL-MOCK -> to=" + to + " | subject=" + subject + " | body=" + body);
            return;
        }

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        if (from != null && !from.isBlank()) msg.setFrom(from);
        msg.setSubject(subject);
        msg.setText(body);
        javaMailSender.send(msg);
    }
}
