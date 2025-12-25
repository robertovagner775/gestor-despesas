package com.roberto.gestor_despesa.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ClientResponse")
public record ClientResponse(
        Integer id,
        String birthDate,
        String username,
        String name,
        String email
) {
}
