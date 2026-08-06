package com.roberto.gestor_despesa.dtos.request;

import jakarta.validation.constraints.*;

public record CategoryRequest(
        @NotBlank
        @Size(min = 3, max = 150)
        String title,

        @NotBlank
        @Size(min = 10, max = 200)
        String description,

        @Pattern(
                regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$",
                message = "Informe uma cor hexadecimal válida no formato #FFFFFF."
        )
        String color,

        @NotNull
        @Min(1)
        Integer categoryTypeId
) {
}
