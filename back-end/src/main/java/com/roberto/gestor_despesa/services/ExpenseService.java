package com.roberto.gestor_despesa.services;

import com.roberto.gestor_despesa.dtos.request.BudgetRequest;
import com.roberto.gestor_despesa.dtos.request.ExpenseRequest;
import com.roberto.gestor_despesa.dtos.response.BudgetResponse;
import com.roberto.gestor_despesa.dtos.response.ExpenseResponse;
import com.roberto.gestor_despesa.entities.Budget;
import com.roberto.gestor_despesa.entities.Expense;

import java.util.List;

public interface ExpenseService {

    public Expense save(ExpenseRequest request, Integer idClient);

    public ExpenseResponse update(Integer id);

    public void delete(Integer id);

    public BudgetResponse findById(Integer id);

    public List<BudgetResponse> findAll();
}
