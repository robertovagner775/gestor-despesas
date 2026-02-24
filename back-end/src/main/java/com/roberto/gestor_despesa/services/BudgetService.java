package com.roberto.gestor_despesa.services;

import com.roberto.gestor_despesa.dtos.request.BudgetRequest;
import com.roberto.gestor_despesa.dtos.response.BudgetDetailResponse;
import com.roberto.gestor_despesa.dtos.response.BudgetResponse;
import com.roberto.gestor_despesa.entities.Budget;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface BudgetService {

    public Budget createBudget(BudgetRequest request, Long idClient);

    public Budget updateBudget(BudgetRequest request, Integer idBudget);

    public BudgetDetailResponse findBudgetByIdAndClient(Integer id, Integer idClient);

    public Page<BudgetResponse> searchBudgets(Integer idClient, String description, String status, LocalDate dateStart, LocalDate dateEnd, String category, Integer pageNumber, Integer pageSize);

} 
