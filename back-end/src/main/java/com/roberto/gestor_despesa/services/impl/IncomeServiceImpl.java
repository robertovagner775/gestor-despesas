package com.roberto.gestor_despesa.services.impl;

import com.roberto.gestor_despesa.dtos.mapper.IncomeMapper;
import com.roberto.gestor_despesa.dtos.request.IncomeRequest;
import com.roberto.gestor_despesa.dtos.response.IncomeResponse;
import com.roberto.gestor_despesa.entities.Category;
import com.roberto.gestor_despesa.entities.Client;
import com.roberto.gestor_despesa.entities.Expense;
import com.roberto.gestor_despesa.entities.Income;
import com.roberto.gestor_despesa.handler.exceptions.NotFoundException;
import com.roberto.gestor_despesa.repository.CategoryRepository;
import com.roberto.gestor_despesa.repository.ClientRepository;
import com.roberto.gestor_despesa.repository.IncomeRepository;
import com.roberto.gestor_despesa.repository.specifications.ExpenseSpecification;
import com.roberto.gestor_despesa.repository.specifications.IncomeSpecification;
import com.roberto.gestor_despesa.security.UserAuth;
import com.roberto.gestor_despesa.services.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class IncomeServiceImpl implements IncomeService {

    private final IncomeRepository incomeRepository;
    private final CategoryRepository categoryRepository;
    private final ClientRepository clientRepository;
    private final IncomeMapper incomeMapper;

    @Override
    public Income createIncome(IncomeRequest request, Integer currentClient) {
        Client client = clientRepository.getReferenceById(currentClient);
        Category category = categoryRepository.getReferenceById(request.category());
       Income income = incomeMapper.toEntity(request);
       income.setClient(client);
       income.setCategory(category);
       return incomeRepository.save(income);
    }

    @Override
    public IncomeResponse updateIncome(IncomeRequest request, Integer id, Integer idCurrentClient) {

        Income income = incomeRepository.findByClient_IdAndId(idCurrentClient, id).orElseThrow(() -> new NotFoundException(id));
        Category category = categoryRepository.findById(request.category()).orElseThrow(() -> new NotFoundException(request.category()));

        income.setDescription(request.description());
        income.setReceivedDate(request.receivedDate());
        income.setCategory(category);
        income.setValue(request.value());

        Income updatedIncome = incomeRepository.save(income);
        return incomeMapper.toResponse(updatedIncome);
    }

    @Override
    public void deleteIncome(Integer idCurrentClient, Integer id) {
        Income income = incomeRepository.findByClient_IdAndId(idCurrentClient, id).orElseThrow(() -> new NotFoundException(id));
        incomeRepository.delete(income);
    }

    @Override
    public IncomeResponse findById(Integer idCurrentClient, Integer id) {
        Income income = incomeRepository.findByClient_IdAndId(idCurrentClient, id).orElseThrow(() -> new NotFoundException(id));
        return incomeMapper.toResponse(income);
    }

    @Override
    public Page<IncomeResponse> findAll(Integer idCurrentClient, BigDecimal valueStart, BigDecimal valueEnd, String description, LocalDate dateStart, LocalDate dateEnd, String category, Integer pageNumber, Integer pageSize) {
        Specification<Income> specs = Specification.anyOf(IncomeSpecification.equalClient(idCurrentClient));
        if(category != null && !category.isBlank()) {
            specs = specs.and(IncomeSpecification.likeCategory(category));
        }
        if(description != null && !description.isBlank()) {
            specs = specs.and(IncomeSpecification.descriptionLike(description));
        }
        if((dateStart != null && dateEnd != null) && dateStart.isBefore(dateEnd)) {
            specs = specs.and(IncomeSpecification.dateBetween(dateStart, dateEnd));
        }
        if( (valueStart != null && valueEnd != null) && valueStart.compareTo(valueEnd) < 0) {
            specs = specs.and(IncomeSpecification.valueBetween(valueStart, valueEnd));
        }
        Pageable pageRequest = PageRequest.of(pageNumber, pageSize);
        return incomeRepository.findAll(specs, pageRequest).map(incomeMapper::toResponse);
    }
}
