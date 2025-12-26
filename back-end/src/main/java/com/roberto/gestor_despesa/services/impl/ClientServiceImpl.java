package com.roberto.gestor_despesa.services.impl;

import com.roberto.gestor_despesa.dtos.mapper.ClientMapper;
import com.roberto.gestor_despesa.dtos.request.ClientRequest;
import com.roberto.gestor_despesa.dtos.response.ClientResponse;
import com.roberto.gestor_despesa.entities.Client;
import com.roberto.gestor_despesa.entities.ConfirmAccountToken;
import com.roberto.gestor_despesa.handler.exceptions.ConflictEntityException;
import com.roberto.gestor_despesa.handler.exceptions.NotFoundException;
import com.roberto.gestor_despesa.repository.ClientRepository;
import com.roberto.gestor_despesa.repository.ConfirmAccountTokenRepository;
import com.roberto.gestor_despesa.services.AuthService;
import com.roberto.gestor_despesa.services.ClientService;

import com.roberto.gestor_despesa.services.EmailService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository repository;
    private final ClientMapper mapper;
    private final PasswordEncoder encoder;
    private final EmailService emailService;
    private final AuthService authService;

    public ClientServiceImpl(ClientRepository repository, ClientMapper mapper, PasswordEncoder encoder, EmailService emailService, AuthService authService) {
        this.repository = repository;
        this.mapper = mapper;
        this.encoder = encoder;
        this.emailService = emailService;
        this.authService = authService;
    }

    @Transactional
    @Override
    public Client save(ClientRequest request) throws MessagingException {
        if(repository.existsByEmail(request.email()))
            throw new ConflictEntityException("This email: "+ request.email() +" already exists");

        Client client = mapper.map(request);
        client.setPassword(encoder.encode(client.getPassword()));
        Client createdClient = this.repository.save(client);

        String link = this.authService.generateAccountConfirmationLink(createdClient);

        this.emailService.sendEmailConfirmation(createdClient, link);
        return createdClient;
    }

    @Override
    public Client update(ClientRequest request, Integer id) {
        return null;
    }

    @Override
    public List<Client> findAll() {
        return List.of();
    }

    @Override
    public ClientResponse findClientById(Integer id) {
        return mapper.map(repository.findById(id).orElseThrow(() -> new NotFoundException(id)));
    }

}
