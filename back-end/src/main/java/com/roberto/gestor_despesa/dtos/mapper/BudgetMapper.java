package com.roberto.gestor_despesa.dtos.mapper;

import com.roberto.gestor_despesa.dtos.response.BudgetResponseDTO;
import com.roberto.gestor_despesa.entities.Budget;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = { BudgetCategoryMapper.class }
)
public interface BudgetMapper {

    BudgetResponseDTO map(Budget entity);

    List<BudgetResponseDTO> map(List<Budget> entities);
}
