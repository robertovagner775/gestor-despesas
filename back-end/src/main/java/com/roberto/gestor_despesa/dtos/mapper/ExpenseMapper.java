package com.roberto.gestor_despesa.dtos.mapper;


import com.roberto.gestor_despesa.dtos.request.ExpenseRequest;
import com.roberto.gestor_despesa.dtos.response.ExpenseResponse;
import com.roberto.gestor_despesa.entities.Client;
import com.roberto.gestor_despesa.entities.Expense;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR, uses = {
        CategoryMapper.class
})
public interface ExpenseMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "budget", ignore = true)
    @Mapping(source = "request.category", target = "category.id")
    @Mapping(target = "client", source = "client")
    Expense map(ExpenseRequest request, Client client);

    ExpenseResponse map(Expense entity);
}
