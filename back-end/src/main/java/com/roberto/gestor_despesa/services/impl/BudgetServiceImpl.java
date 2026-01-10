package com.roberto.gestor_despesa.services.impl;

import com.roberto.gestor_despesa.dtos.mapper.BudgetCategoryMapper;
import com.roberto.gestor_despesa.dtos.mapper.BudgetMapper;
import com.roberto.gestor_despesa.dtos.request.BudgetCategoryRequest;
import com.roberto.gestor_despesa.dtos.request.BudgetRequest;
import com.roberto.gestor_despesa.dtos.response.BudgetResponse;
import com.roberto.gestor_despesa.entities.*;
import com.roberto.gestor_despesa.entities.enums.Status;
import com.roberto.gestor_despesa.handler.exceptions.ConflictEntityException;
import com.roberto.gestor_despesa.handler.exceptions.NotFoundException;
import com.roberto.gestor_despesa.repository.BudgetCategoryRepository;
import com.roberto.gestor_despesa.repository.BudgetRepository;
import com.roberto.gestor_despesa.repository.CategoryRepository;
import com.roberto.gestor_despesa.repository.ClientRepository;
import com.roberto.gestor_despesa.repository.specifications.BudgetSpecification;
import com.roberto.gestor_despesa.services.BudgetService;
import com.roberto.gestor_despesa.utils.DateUtils;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.domain.PredicateSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;
    private final ClientRepository clientRepository;
    private final BudgetMapper budgetMapper;
    private final BudgetCategoryMapper budgetCategoryMapper;
    private final DateUtils dateUtils;
    private final BudgetCategoryRepository budgetCategoryRepository;

    public BudgetServiceImpl(BudgetRepository budgetRepository, CategoryRepository categoryRepository, ClientRepository clientRepository, BudgetMapper budgetMapper, DateUtils dateUtils, BudgetCategoryMapper budgetCategoryMapper, BudgetCategoryRepository budgetCategoryRepository) {
        this.budgetRepository = budgetRepository;
        this.categoryRepository = categoryRepository;
        this.clientRepository = clientRepository;
        this.budgetMapper = budgetMapper;
        this.dateUtils = dateUtils;
        this.budgetCategoryMapper = budgetCategoryMapper;
        this.budgetCategoryRepository = budgetCategoryRepository;
    }

    @Transactional
    @Override
    public Budget createTotalBudget(BudgetRequest request, Long idClient) {

        dateUtils.validateYearMonth(request.periodReference());

        Client client = Client.clientById(idClient.intValue());

        if(budgetRepository.existsByClientAndPeriodReferenceAndStatus(client, request.periodReference().atDay(1), Status.ACTIVE))
            throw new ConflictEntityException("Already exists budget active in " + request.periodType());

        BigDecimal totalPlanned = BigDecimal.ZERO;
        Budget budget = new Budget();

        budget.setClient(client);
        budget.setPeriodType(request.periodType());
        budget.setPeriodReference(request.periodReference().atDay(1));
        budget.setTotalPlanned(totalPlanned);
        budget.setTotalSpent(BigDecimal.ZERO);
        budget.setTotalRemaining(BigDecimal.ZERO);
        budget.setDescription(request.description());
        budget.setStatus(Status.ACTIVE);

        Budget budgetCreated = budgetRepository.save(budget);
        List<BudgetCategory> listBudgetCategory = new ArrayList<>();

        for(BudgetCategoryRequest budgetCategoryFor : request.budgetCategory()) {
            totalPlanned = totalPlanned.add(budgetCategoryFor.plannedValue());
            Category category = categoryRepository.findById(budgetCategoryFor.category_id()).orElseThrow(() -> new NotFoundException(budgetCategoryFor.category_id()));

            BudgetCategory budgetCategory =  new BudgetCategory(BigDecimal.ZERO , BigDecimal.ZERO, budgetCategoryFor.plannedValue(), category, budgetCreated);
            listBudgetCategory.add(budgetCategory);
        }
        budget.setTotalPlanned(totalPlanned);
        budget.getCategories().addAll(listBudgetCategory);

        return budgetRepository.save(budget);
    }

    @Override
    public Budget updateBudget(BudgetRequest request, Integer idBudget) {
        BigDecimal totalPlanned = BigDecimal.ZERO;
        Budget budget = budgetRepository.findById(idBudget).orElseThrow(() -> new NotFoundException(idBudget));
        budget.setPeriodType(request.periodType());
        budget.setDescription(request.description());

        budget.getCategories().clear();

        for(BudgetCategoryRequest requestCategory : request.budgetCategory()) {
            BudgetCategoryId embeddedId = new BudgetCategoryId(idBudget, requestCategory.category_id());
            BudgetCategory budgetCategory = budgetCategoryRepository.findById(embeddedId).orElseThrow(() -> new NotFoundException(requestCategory.category_id()));
            budgetCategory.setPlannedValue(requestCategory.plannedValue());
            totalPlanned = totalPlanned.add(budgetCategory.getPlannedValue());
            budget.getCategories().add(budgetCategory);
        }
        budget.setTotalPlanned(totalPlanned);
        return budgetRepository.save(budget);
    }

    @Override
    public BudgetResponse findBudgetByIdAndClient(Integer id, Integer idClient) {
        Budget budget = budgetRepository.findByIdAndClient_Id(id, idClient).orElseThrow(
                () -> new NotFoundException(id)
        );

        return budgetMapper.map(budget);
    }

    @Override
    public List<BudgetResponse> searchBudgets(Integer idCurrentClient, String description, String status, LocalDate dateStart, LocalDate dateEnd, String category) {

        Specification<Budget> specs = Specification.where(BudgetSpecification.equalClient(idCurrentClient));

        if(description != null && !description.isEmpty()) {
            specs = specs.and(BudgetSpecification.descriptionLike(description));
        }
        if(category != null && !category.isEmpty()) {
            specs = specs.and(BudgetSpecification.likeCategory(category));
        }
        if(status != null) {
            specs = specs.and(BudgetSpecification.equalsStatus(status));
        }
        if (dateStart != null && dateEnd != null) {
            specs = specs.and(BudgetSpecification.dateBetween(dateStart, dateEnd));
        }
        return budgetRepository.findAll(specs).stream().map(budgetMapper::map).toList();
    }


}
