package com.roberto.gestor_despesa.services.impl;

import com.roberto.gestor_despesa.entities.Client;
import com.roberto.gestor_despesa.repository.ClientRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.roberto.gestor_despesa.dtos.request.LoginRequestDTO;
import com.roberto.gestor_despesa.dtos.response.LoginResponseDTO;
import com.roberto.gestor_despesa.security.JwtService;
import com.roberto.gestor_despesa.security.UserDetailsServiceImpl;
import com.roberto.gestor_despesa.services.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final AuthenticationManager manager;
    private final ClientRepository repository;

    public AuthServiceImpl(AuthenticationManager manager, JwtService jwtService, ClientRepository repository) {
        this.manager = manager;
        this.jwtService = jwtService;
        this.repository = repository;
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO request) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(request.email(), request.password());

        Client client = repository.findByEmail(request.email()).get();

        Authentication authResult = manager.authenticate(authentication);

        String token = jwtService.generateToken(authResult, client);

        return new LoginResponseDTO(token); 

    }
    
}
