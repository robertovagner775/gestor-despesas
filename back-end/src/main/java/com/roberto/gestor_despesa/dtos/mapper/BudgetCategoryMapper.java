package com.roberto.gestor_despesa.dtos.mapper;

import com.roberto.gestor_despesa.dtos.response.BudgetCategoryResponse;
import com.roberto.gestor_despesa.entities.BudgetCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface BudgetCategoryMapper {


}
