package com.roberto.gestor_despesa.dtos.request;

import java.time.LocalDate;

public record ClientRequestDTO(

        String username,
        LocalDate birthDate,
        String telephone,
        String name,
        String email,
        String password
) {
}
