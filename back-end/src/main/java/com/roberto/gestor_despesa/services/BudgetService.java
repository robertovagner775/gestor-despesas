package com.roberto.gestor_despesa.services;

import com.roberto.gestor_despesa.dtos.request.BudgetRequestDTO;
import com.roberto.gestor_despesa.dtos.response.BudgetResponseDTO;
import com.roberto.gestor_despesa.entities.Budget;

public interface BudgetService {

    public Budget createTotalBudget(BudgetRequestDTO request, Long idClient);

    public BudgetResponseDTO findBudgetbyId(Integer id);
    
} 
