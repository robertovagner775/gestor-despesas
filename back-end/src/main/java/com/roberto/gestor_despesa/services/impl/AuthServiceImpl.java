package com.roberto.gestor_despesa.services.impl;

import com.roberto.gestor_despesa.entities.Client;
import com.roberto.gestor_despesa.entities.ConfirmAccountToken;
import com.roberto.gestor_despesa.repository.ClientRepository;
import com.roberto.gestor_despesa.repository.ConfirmAccountTokenRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.roberto.gestor_despesa.dtos.request.LoginRequest;
import com.roberto.gestor_despesa.dtos.response.LoginResponse;
import com.roberto.gestor_despesa.security.JwtService;
import com.roberto.gestor_despesa.services.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final AuthenticationManager manager;
    private final ClientRepository clientRepository;
    private final ConfirmAccountTokenRepository confirmAccountTokenRepository;

    public AuthServiceImpl(AuthenticationManager manager, JwtService jwtService, ClientRepository clientRepository, ConfirmAccountTokenRepository confirmAccountTokenRepository) {
        this.manager = manager;
        this.jwtService = jwtService;
        this.clientRepository = clientRepository;
        this.confirmAccountTokenRepository = confirmAccountTokenRepository;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(request.email(), request.password());

        Client client = clientRepository.findByEmail(request.email()).get();

        Authentication authResult = manager.authenticate(authentication);

        String token = jwtService.generateToken(authResult, client);

        return new LoginResponse(token);

    }

    @Override
    public void confirmAccount(String token) {
        ConfirmAccountToken confirmAccount = this.confirmAccountTokenRepository.findByToken(token);
        Client client = confirmAccount.getClient();
        client.setEnabled(true);
        clientRepository.save(client);
    }

    @Override
    public String generateAccountConfirmationLink(Client client) {
        ConfirmAccountToken confirmToken = new ConfirmAccountToken(client);
        confirmAccountTokenRepository.save(confirmToken);
        String link = "http://localhost:8080/api/auth/confirm?token=" + confirmToken.getToken();
        return link;
    }

}
