package com.roberto.gestor_despesa.dtos.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record IncomeRequest(
                            @NotNull
                            @NotBlank
                            String description,

                            LocalDate receivedDate,

                            @NotNull(message = "Valor e obrigatorio")
                            @Positive(message = "O valor deve ser positivo")
                            @Digits(
                                    integer = 10,
                                    fraction = 2,
                                    message = "O valor deve ter no máximo 2 casas decimais"
                            )
                            BigDecimal value,

                            @NotNull
                            Integer category) {
}
