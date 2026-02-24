package com.roberto.gestor_despesa.services.impl;

import com.roberto.gestor_despesa.dtos.request.BudgetCategoryRequest;
import com.roberto.gestor_despesa.dtos.request.BudgetRequest;
import com.roberto.gestor_despesa.dtos.response.BudgetCategoryResponse;
import com.roberto.gestor_despesa.dtos.response.BudgetDetailResponse;
import com.roberto.gestor_despesa.dtos.response.BudgetResponse;
import com.roberto.gestor_despesa.entities.*;
import com.roberto.gestor_despesa.entities.enums.Status;
import com.roberto.gestor_despesa.handler.exceptions.ConflictEntityException;
import com.roberto.gestor_despesa.handler.exceptions.NotFoundException;
import com.roberto.gestor_despesa.repository.BudgetCategoryRepository;
import com.roberto.gestor_despesa.repository.BudgetRepository;
import com.roberto.gestor_despesa.repository.CategoryRepository;
import com.roberto.gestor_despesa.repository.ClientRepository;
import com.roberto.gestor_despesa.services.BudgetService;
import com.roberto.gestor_despesa.utils.DateUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;
    private final ClientRepository clientRepository;
    private final DateUtils dateUtils;
    private final BudgetCategoryRepository budgetCategoryRepository;


    @Transactional
    @Override
    public Budget createBudget(BudgetRequest request, Long idClient) {

        dateUtils.validateYearMonth(request.periodReference());
        Client client = clientRepository.getReferenceById(idClient.intValue());

        if(budgetRepository.existsByClientAndPeriodReferenceAndStatus(client, request.periodReference().atDay(1), Status.ACTIVE))
            throw new ConflictEntityException("Already exists budget active in " + request.periodType());

        Budget budget = Budget.builder()
                .id(null)
                .client(client)
                .periodReference(request.periodReference().atDay(1))
                .periodType(request.periodType())
                .totalPlanned(BigDecimal.ZERO)
                .description(request.description())
                .status(Status.ACTIVE)
                .categories(new ArrayList<BudgetCategory>())
                .build();

        for(BudgetCategoryRequest budgetCategoryRequest : request.budgetCategory()) {
            Category category = categoryRepository.findById(budgetCategoryRequest.category_id()).orElseThrow(() -> new NotFoundException(budgetCategoryRequest.category_id()));
            BudgetCategoryId id = new BudgetCategoryId(budget.getId(), category.getId());
            BudgetCategory budgetCategory =  new BudgetCategory(id, budget, category, BigDecimal.ZERO);
            budgetCategory.sumPlannedValue(budgetCategoryRequest.plannedValue());
            budget.getCategories().add(budgetCategory);
        }
        budget.recalculateTotalPlanned();
        return budgetRepository.save(budget);
    }

    @Override
    public Budget updateBudget(BudgetRequest request, Integer idBudget) {
        Budget budget = budgetRepository.findById(idBudget).orElseThrow(() -> new NotFoundException(idBudget));
        budget.setPeriodType(request.periodType());
        budget.setDescription(request.description());
        budget.getCategories().clear();

        for(BudgetCategoryRequest budgetCategoryRequest : request.budgetCategory()) {
            BudgetCategoryId embeddedId = new BudgetCategoryId(idBudget, budgetCategoryRequest.category_id());
            BudgetCategory budgetCategory = budgetCategoryRepository.findById(embeddedId).orElseThrow(() -> new NotFoundException(budgetCategoryRequest.category_id()));
            budgetCategory.setPlannedValue(budgetCategoryRequest.plannedValue());
            budget.getCategories().add(budgetCategory);
        }
        budget.recalculateTotalPlanned();
        return budgetRepository.save(budget);
    }

    @Override
    public BudgetDetailResponse findBudgetByIdAndClient(Integer id, Integer idClient) {
        Budget budget = budgetRepository.findByIdAndClient_Id(id, idClient).orElseThrow(
                () -> new NotFoundException(id)
        );

       BigDecimal totalSpent = budget.getTotalSpent();
       BigDecimal totalRemaining = budget.getTotalPlanned().subtract(totalSpent);

        List<BudgetCategoryResponse> budgetCategoryResponse = budgetCategoryRepository.findAllBudgetCategoryTotal(budget.getId());

        return  new BudgetDetailResponse(
                       budget.getId(), budget.getDescription(), budget.getTotalPlanned(), totalSpent, totalRemaining, budget.getPeriodReference(), budget.getPeriodType(), budget.getStatus(), budgetCategoryResponse);
    }

    @Override
    public Page<BudgetResponse> searchBudgets(Integer idCurrentClient, String description, String status, LocalDate dateStart, LocalDate dateEnd, String category, Integer pageNumber, Integer pageSize) {
        Pageable pageRequest = PageRequest.of(pageNumber, pageSize);
        Status statusEnum = status != null ? Status.valueOf(status) : null;
        return  budgetRepository.searchBudgets(idCurrentClient, description, statusEnum , dateStart, dateEnd, pageRequest);
    }
}
