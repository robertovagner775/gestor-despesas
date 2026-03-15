package com.roberto.gestor_despesa.dtos.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.aspectj.weaver.ast.Not;

import java.math.BigDecimal;
import java.time.LocalDate;

public record
    ExpenseRequest(
            @NotBlank
            String description,

            String paymentMethod,

            LocalDate paidDate,

            @NotNull(message = "Valor e obrigatorio")
            @Positive(message = "O valor deve ser positivo")
            @Digits(
                    integer = 10,
                    fraction = 2,
                    message = "O valor deve ter no máximo 2 casas decimais"
            )
            BigDecimal value,

            @NotNull
            Integer category
) {
}
