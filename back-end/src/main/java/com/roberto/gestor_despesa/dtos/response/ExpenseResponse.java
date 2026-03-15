package com.roberto.gestor_despesa.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseResponse(Integer id, String description, LocalDate paidDate, BigDecimal value, CategoryResponse category) {
}
