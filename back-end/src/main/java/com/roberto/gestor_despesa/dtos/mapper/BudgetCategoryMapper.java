package com.roberto.gestor_despesa.dtos.mapper;

import com.roberto.gestor_despesa.dtos.response.BudgetCategoryResponseDTO;
import com.roberto.gestor_despesa.dtos.response.BudgetResponseDTO;
import com.roberto.gestor_despesa.entities.Budget;
import com.roberto.gestor_despesa.entities.BudgetCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface BudgetCategoryMapper {

    @Mapping(source = "category.id", target = "idCategory")
    @Mapping(source = "category.title", target = "title")
    @Mapping(source = "value", target = "value")
    BudgetCategoryResponseDTO map(BudgetCategory entity);

    List<BudgetCategoryResponseDTO> map(List<BudgetCategory> entities);
}
