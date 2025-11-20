package com.roberto.gestor_despesa.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(name = "BudgetCategoryRequest")
public record BudgetCategoryRequestDTO(

        @NotNull
        Integer category_id,

        @NotNull
        @Positive
        BigDecimal value
) {
}
