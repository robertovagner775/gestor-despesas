package com.roberto.gestor_despesa.services.impl;

import com.roberto.gestor_despesa.dtos.mapper.ExpenseMapper;
import com.roberto.gestor_despesa.dtos.request.ExpenseRequest;
import com.roberto.gestor_despesa.dtos.response.ExpenseResponse;
import com.roberto.gestor_despesa.entities.*;
import com.roberto.gestor_despesa.entities.enums.Status;
import com.roberto.gestor_despesa.handler.exceptions.BudgetException;
import com.roberto.gestor_despesa.handler.exceptions.NotFoundException;
import com.roberto.gestor_despesa.repository.BudgetRepository;
import com.roberto.gestor_despesa.repository.CategoryRepository;
import com.roberto.gestor_despesa.repository.ClientRepository;
import com.roberto.gestor_despesa.repository.ExpenseRepository;
import com.roberto.gestor_despesa.repository.specifications.ExpenseSpecification;
import com.roberto.gestor_despesa.services.ExpenseService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;

@Slf4j
@RequiredArgsConstructor
@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final BudgetRepository budgetRepository;
    private final ClientRepository clientRepository;
    private final CategoryRepository categoryRepository;
    private final ExpenseMapper expenseMapper;

    @Transactional
    @Override
    public Expense save(ExpenseRequest request, Integer idClient) {
        Client currentClient = clientRepository.getReferenceById(idClient);
        Category category = categoryRepository.getReferenceById(request.category());

        Budget budget = budgetRepository
                .findByClientAndPeriodReferenceAndStatus(
                        currentClient,
                        request.paidDate().withDayOfMonth(1),
                        Status.ACTIVE
                )
                .orElseThrow(() -> new BudgetException("There is no budget registered for the month in which the expense was created.")
                );

        Expense expense = expenseMapper.map(request, currentClient);
        expense.setCategory(category);
        expense.setBudget(budget);
        return expenseRepository.save(expense);
    }

    @Override
    public ExpenseResponse update(ExpenseRequest request, Integer id) {
        Expense expense = expenseRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
        Category category = categoryRepository.getReferenceById(request.category());

        expense.setDescription(request.description());
        expense.setPaidDate(request.paidDate());
        expense.setValue(request.value());
        expense.setCategory(category);

        Expense expenseUpdated = expenseRepository.save(expense);

        return expenseMapper.map(expenseUpdated);
    }

    @Override
    public void delete(Integer id) {
        Expense expense = expenseRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
        expenseRepository.delete(expense);
    }

    @Override
    public ExpenseResponse findById(Integer id) {
        Expense expense = expenseRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
        return expenseMapper.map(expense);
    }

    @Override
    public Page<ExpenseResponse> findAll(Integer pageNumber, Integer pageSize, String category, String description, YearMonth date, BigDecimal valueStart, BigDecimal valueEnd, Integer currentClient) {
        Specification<Expense> specs = Specification.anyOf(ExpenseSpecification.equalClient(currentClient));
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
