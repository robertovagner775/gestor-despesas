package com.roberto.gestor_despesa.services;

import com.roberto.gestor_despesa.entities.Client;
import jakarta.mail.MessagingException;

public interface EmailService {

    public void sendEmailConfirmation(Client client, String link) throws MessagingException;
}
