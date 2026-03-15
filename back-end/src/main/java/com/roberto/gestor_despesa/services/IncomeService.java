package com.roberto.gestor_despesa.services;

import com.roberto.gestor_despesa.dtos.request.IncomeRequest;
import com.roberto.gestor_despesa.dtos.response.IncomeResponse;
import com.roberto.gestor_despesa.entities.Income;
import com.roberto.gestor_despesa.security.UserAuth;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface IncomeService {

    public Income createIncome(IncomeRequest request, Integer currentClient);

    public IncomeResponse updateIncome(IncomeRequest request, Integer idIncome, Integer idCurrentClient);

    public void deleteIncome(Integer currentClient, Integer id);

    public IncomeResponse findById(Integer currentClient, Integer id);

    public Page<IncomeResponse> findAll(Integer currentClient, BigDecimal valueStart, BigDecimal valueEnd, String description, LocalDate dateStart, LocalDate dateEnd, String category, Integer pageNumber, Integer pageSize);
}
