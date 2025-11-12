package com.roberto.gestor_despesa.dtos.request;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BudgetRequestDTO(String description, BigDecimal totalValue, LocalDate dateStart, LocalDate dateEnd) {
    
}
