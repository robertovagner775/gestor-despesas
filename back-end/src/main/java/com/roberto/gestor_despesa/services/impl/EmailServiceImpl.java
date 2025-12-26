package com.roberto.gestor_despesa.services.impl;

import com.roberto.gestor_despesa.entities.Client;
import com.roberto.gestor_despesa.services.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    private final TemplateEngine engine;

    public EmailServiceImpl(JavaMailSender mailSender, TemplateEngine engine) {
        this.mailSender = mailSender;
        this.engine = engine;
    }

    @Async
    @Override
    public void sendEmailConfirmation(Client client, String link) throws MessagingException {
        Context context = new Context();
        context.setVariable("nome", client.getName());
        context.setVariable("link", link);

        String html = engine.process("confirm-account-email", context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(client.getEmail());
        helper.setSubject("Confirme sua conta");
        helper.setText(html, true);

        mailSender.send(message);
    }
}
