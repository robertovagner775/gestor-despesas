package com.roberto.gestor_despesa.services;

import com.roberto.gestor_despesa.dtos.request.LoginRequestDTO;
import com.roberto.gestor_despesa.dtos.response.LoginResponseDTO;

public interface AuthService {

    public LoginResponseDTO login(LoginRequestDTO request);    
} 
