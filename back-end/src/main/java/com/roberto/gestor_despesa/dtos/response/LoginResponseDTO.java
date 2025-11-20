package com.roberto.gestor_despesa.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "LoginResponse")
public record LoginResponseDTO(String token)  {
    
}
