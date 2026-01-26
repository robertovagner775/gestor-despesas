package com.roberto.gestor_despesa.dtos.request;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseRequest(String description, LocalDate date, BigDecimal value, Integer category) {
}
