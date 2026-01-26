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
import com.roberto.gestor_despesa.services.ExpenseService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    public BudgetResponse findById(Integer id) {
        return null;
    }

    @Override
    public List<BudgetResponse> findAll() {
        return List.of();
    }
}
