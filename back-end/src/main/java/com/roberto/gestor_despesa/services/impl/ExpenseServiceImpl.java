package com.roberto.gestor_despesa.services.impl;

import com.roberto.gestor_despesa.dtos.mapper.ExpenseMapper;
import com.roberto.gestor_despesa.dtos.request.ExpenseRequest;
import com.roberto.gestor_despesa.dtos.response.BudgetResponse;
import com.roberto.gestor_despesa.dtos.response.ExpenseResponse;
import com.roberto.gestor_despesa.entities.*;
import com.roberto.gestor_despesa.entities.enums.Status;
import com.roberto.gestor_despesa.handler.exceptions.BudgetException;
import com.roberto.gestor_despesa.handler.exceptions.NotFoundException;
import com.roberto.gestor_despesa.repository.BudgetRepository;
import com.roberto.gestor_despesa.repository.CategoryRepository;
import com.roberto.gestor_despesa.repository.ClientRepository;
import com.roberto.gestor_despesa.repository.ExpenseRepository;
import com.roberto.gestor_despesa.repository.specifications.BudgetSpecification;
import com.roberto.gestor_despesa.repository.specifications.ExpenseSpecification;
import com.roberto.gestor_despesa.services.ExpenseService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.PredicateSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final BudgetRepository budgetRepository;
    private final ClientRepository clientRepository;
    private final CategoryRepository categoryRepository;
    private final ExpenseMapper expenseMapper;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository, BudgetRepository budgetRepository, ExpenseMapper expenseMapper, ClientRepository clientRepository, CategoryRepository categoryRepository) {
        this.budgetRepository = budgetRepository;
        this.expenseRepository = expenseRepository;
        this.expenseMapper = expenseMapper;
        this.clientRepository = clientRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    @Override
    public Expense save(ExpenseRequest request, Integer idClient) {
        Client currentClient = clientRepository.getReferenceById(idClient);
        Category category = categoryRepository.getReferenceById(request.category());

        Budget budget = budgetRepository
                .findByClientAndPeriodReferenceAndStatus(
                        currentClient,
                        request.date().withDayOfMonth(1),
                        Status.ACTIVE
                )
                .orElseThrow(() ->
                        new BudgetException("There is no budget registered for the month in which the expense was created.")
                );

        budget.getCategories().stream()
                .filter(bc -> Objects.equals(bc.getCategory().getId(), request.category()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(request.category()))
                .addSpentValue(request.value());

        budget.setTotalSpent(budget.getTotalSpent().add(request.value()));
        Expense expense = expenseMapper.map(request, currentClient);
        expense.setCategory(category);
        return expenseRepository.save(expense);
    }

    @Override
    public ExpenseResponse update(Integer id) {
        return null;
    }

    @Override
    public void delete(Integer id) {}

    @Override
    public ExpenseResponse findById(Integer id) {
        Expense expense = expenseRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
        return expenseMapper.map(expense);
    }

    @Override
    public Page<ExpenseResponse> findAll(Integer pageNumber, Integer pageSize, String category, String description, YearMonth date, BigDecimal valueStart, BigDecimal valueEnd, Integer currentClient) {
        Specification<Expense> specs = Specification.where(ExpenseSpecification.equalClient(currentClient));
        if(category != null && !category.isBlank()) {
           specs = specs.and(ExpenseSpecification.likeCategory(category));
        }
        if(description != null && !description.isBlank()) {
            specs = specs.and(ExpenseSpecification.descriptionLike(description));
        }
        if(date != null) {
            specs = specs.and(ExpenseSpecification.dateBetween(date));
        }
        if( (valueStart != null && valueEnd != null) && valueStart.compareTo(valueEnd) < 0) {
            specs = specs.and(ExpenseSpecification.valueBetween(valueStart, valueEnd));
        }
        Pageable pageRequest = PageRequest.of(pageNumber, pageSize);
        return expenseRepository.findAll(specs, pageRequest).map(expenseMapper::map);
    }
}
