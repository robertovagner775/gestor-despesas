package com.roberto.gestor_despesa.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(name = "BudgetCategoryResponse")
public record BudgetCategoryResponse(
        Integer idCategory,
        String title,
        BigDecimal plannedValue,
        BigDecimal spentValue,
        BigDecimal remainingValue) {
}
