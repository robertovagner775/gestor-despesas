package com.roberto.gestor_despesa.dtos.mapper;

import com.roberto.gestor_despesa.dtos.response.BudgetDetailResponse;
import com.roberto.gestor_despesa.dtos.response.BudgetResponse;
import com.roberto.gestor_despesa.entities.Budget;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = { BudgetCategoryMapper.class }
)
public interface BudgetMapper {

    BudgetResponse toResponse(Budget entity);

    BudgetDetailResponse toResponseDetail(Budget entity);

    List<BudgetResponse> toResponse(List<Budget> entities);
}
