package com.roberto.gestor_despesa.services;

import com.roberto.gestor_despesa.dtos.request.BudgetRequest;
import com.roberto.gestor_despesa.dtos.response.BudgetResponse;
import com.roberto.gestor_despesa.entities.Budget;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface BudgetService {

    public Budget createTotalBudget(BudgetRequest request, Long idClient);

    public Budget updateBudget(BudgetRequest request, Integer idBudget);

    public BudgetResponse findBudgetByIdAndClient(Integer id, Integer idClient);

    public Page<BudgetResponse> searchBudgets(Integer idClient, String description, String status, LocalDate dateStart, LocalDate dateEnd, String category, Integer pageNumber, Integer pageSize);

} 
