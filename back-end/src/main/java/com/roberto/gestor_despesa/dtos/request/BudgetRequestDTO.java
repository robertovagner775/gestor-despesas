package com.roberto.gestor_despesa.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Schema(name = "BudgetRequest")
public record BudgetRequestDTO(

        @NotNull
        @NotBlank
        String description,

        @NotNull
        LocalDate dateStart,

        @NotNull
        LocalDate dateEnd,

        @Valid
        List<BudgetCategoryRequestDTO> budgetCategory
) {
    
}
