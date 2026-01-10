package com.roberto.gestor_despesa.dtos.request;

import com.roberto.gestor_despesa.entities.enums.PeriodType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.YearMonth;
import java.util.List;

@Schema(name = "BudgetRequest")
public record BudgetRequest(

        @NotNull
        @NotBlank
        String description,

        @NotNull
        YearMonth periodReference,

        @NotNull
        PeriodType periodType,

        @Valid
        List<BudgetCategoryRequest> budgetCategory
) {
    
}
