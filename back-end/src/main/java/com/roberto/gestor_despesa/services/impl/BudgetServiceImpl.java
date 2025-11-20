package com.roberto.gestor_despesa.services.impl;

import com.roberto.gestor_despesa.dtos.mapper.BudgetMapper;
import com.roberto.gestor_despesa.dtos.request.BudgetCategoryRequestDTO;
import com.roberto.gestor_despesa.dtos.request.BudgetRequestDTO;
import com.roberto.gestor_despesa.dtos.response.BudgetResponseDTO;
import com.roberto.gestor_despesa.entities.*;
import com.roberto.gestor_despesa.handler.exceptions.NotFoundException;
import com.roberto.gestor_despesa.repository.BudgetRepository;
import com.roberto.gestor_despesa.repository.CategoryRepository;
import com.roberto.gestor_despesa.repository.ClientRepository;
import com.roberto.gestor_despesa.services.BudgetService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;
    private final ClientRepository clientRepository;
    private final BudgetMapper budgetMapper;

    public BudgetServiceImpl(BudgetRepository budgetRepository, CategoryRepository categoryRepository, ClientRepository clientRepository, BudgetMapper budgetMapper) {
        this.budgetRepository = budgetRepository;
        this.categoryRepository = categoryRepository;
        this.clientRepository = clientRepository;
        this.budgetMapper = budgetMapper;
    }

    @Transactional
    @Override
    public Budget createTotalBudget(BudgetRequestDTO request, Long idClient) {

        Client client = clientRepository.findById(idClient.intValue()).orElseThrow(() -> new NotFoundException(idClient.intValue()));
        BigDecimal totalValue = BigDecimal.ZERO;
        Budget budget = new Budget();

        budget.setClient(client);
        budget.setTotalValue(totalValue);
        budget.setDateStart(request.dateStart());
        budget.setDateEnd(request.dateEnd());
        budget.setDescription(request.description());

        Budget budgetCreated = budgetRepository.save(budget);
        List<BudgetCategory> listbudgetCategory = new ArrayList<>();

        for(BudgetCategoryRequestDTO budgetCategoryFor : request.budgetCategory()) {
            totalValue = totalValue.add(budgetCategoryFor.value());
            Category category = categoryRepository.findById(budgetCategoryFor.category_id()).orElseThrow(() -> new NotFoundException(budgetCategoryFor.category_id()));
            BudgetCategory budgetCategory =  new BudgetCategory(category, budgetCategoryFor.value(), budgetCreated);
            listbudgetCategory.add(budgetCategory);
        }

        budget.setTotalValue(totalValue);
        budget.getCategories().addAll(listbudgetCategory);

        return budgetRepository.save(budget);
    }

    @Override
    public BudgetResponseDTO findBudgetbyId(Integer id) {

        Budget budget = budgetRepository.findById(id).orElseThrow(() -> new NotFoundException(id));

        BudgetResponseDTO response = budgetMapper.map(budget);

        return response;
    }


}
