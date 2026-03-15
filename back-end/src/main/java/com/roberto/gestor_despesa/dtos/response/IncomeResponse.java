package com.roberto.gestor_despesa.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record IncomeResponse(
        Integer id,
        String description,
        LocalDate receivedDate,
        BigDecimal value,
        CategoryResponse category
) {
}
