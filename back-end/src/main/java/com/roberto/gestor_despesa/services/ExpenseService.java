package com.roberto.gestor_despesa.services;

import com.roberto.gestor_despesa.dtos.request.BudgetRequest;
import com.roberto.gestor_despesa.dtos.request.ExpenseRequest;
import com.roberto.gestor_despesa.dtos.response.BudgetResponse;
import com.roberto.gestor_despesa.dtos.response.ExpenseResponse;
import com.roberto.gestor_despesa.entities.Budget;
import com.roberto.gestor_despesa.entities.Expense;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

public interface ExpenseService {

    public Expense save(ExpenseRequest request, Integer idCurrentClient);

    public ExpenseResponse update(ExpenseRequest request, Integer id);

    public void delete(Integer id);

    public ExpenseResponse findById(Integer id);

    public Page<ExpenseResponse> findAll(Integer pageNumber, Integer pageSize, String category, String description, YearMonth date, BigDecimal valueStart, BigDecimal valueEnd, Integer currentClient);
}
