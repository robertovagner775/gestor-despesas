package com.roberto.gestor_despesa.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Schema(name = "ClientRequest")
public record ClientRequestDTO(

        @NotNull
        @Size(min = 4, max = 50)
        String username,

        @NotNull
        LocalDate birthDate,

        @NotNull
        @Size(min = 3, max = 15)
        String telephone,

        @NotNull
        @Size(min = 4, max = 80)
        String name,

        @NotNull
        @Email
        String email,

        @NotNull
        @Size(min = 6, max = 25)
        String password
) {
}
