package com.roberto.gestor_despesa.dtos.response;

import com.roberto.gestor_despesa.entities.enums.PeriodType;
import com.roberto.gestor_despesa.entities.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Schema(name = "BudgetResponse")
public record BudgetResponse(

        Integer id,
        String description,
        BigDecimal totalPlanned,
        BigDecimal totalSpent,
        BigDecimal totalRemaining,
        LocalDate periodReference,
        PeriodType periodType,
        Status status,
        List<BudgetCategoryResponse> categories
) {
}
