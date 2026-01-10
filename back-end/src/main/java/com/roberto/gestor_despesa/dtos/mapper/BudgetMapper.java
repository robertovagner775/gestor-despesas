package com.roberto.gestor_despesa.dtos.mapper;

import com.roberto.gestor_despesa.dtos.response.BudgetResponse;
import com.roberto.gestor_despesa.entities.Budget;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = { BudgetCategoryMapper.class }
)
public interface BudgetMapper {

    BudgetResponse map(Budget entity);

    List<BudgetResponse> map(List<Budget> entities);
}
