package com.roberto.gestor_despesa.dtos.response;

import com.roberto.gestor_despesa.entities.BudgetCategory;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Schema(name = "BudgetResponse")
public record BudgetResponseDTO(

        Integer id,
        String description,
        BigDecimal totalValue,
        LocalDate dateStart,
        LocalDate dateEnd,
        List<BudgetCategoryResponseDTO> categories
) {
}
