package com.roberto.gestor_despesa.services;

import com.roberto.gestor_despesa.dtos.request.LoginRequest;
import com.roberto.gestor_despesa.dtos.response.LoginResponse;
import com.roberto.gestor_despesa.entities.Client;

public interface AuthService {

    public LoginResponse login(LoginRequest request);

    public void confirmAccount(String token);

    public String generateAccountConfirmationLink(Client client);
} 
