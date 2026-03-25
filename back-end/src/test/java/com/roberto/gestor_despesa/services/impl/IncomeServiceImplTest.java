package com.roberto.gestor_despesa.services.impl;

import com.roberto.gestor_despesa.dtos.mapper.IncomeMapper;
import com.roberto.gestor_despesa.dtos.request.IncomeRequest;
import com.roberto.gestor_despesa.dtos.response.CategoryResponse;
import com.roberto.gestor_despesa.dtos.response.IncomeResponse;
import com.roberto.gestor_despesa.entities.Category;
import com.roberto.gestor_despesa.entities.CategoryType;
import com.roberto.gestor_despesa.entities.Client;
import com.roberto.gestor_despesa.entities.Income;
import com.roberto.gestor_despesa.handler.exceptions.NotFoundException;
import com.roberto.gestor_despesa.repository.CategoryRepository;
import com.roberto.gestor_despesa.repository.ClientRepository;
import com.roberto.gestor_despesa.repository.IncomeRepository;
import com.roberto.gestor_despesa.services.IncomeService;
import jakarta.persistence.EntityNotFoundException;
import jdk.jfr.Description;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IncomeServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private IncomeMapper incomeMapper;

    @Mock
    private IncomeRepository incomeRepository;

    @Captor
    private ArgumentCaptor<Income> incomeArgumentCaptor;

    @Captor
    private ArgumentCaptor<IncomeResponse> incomeResponseArgumentCaptor;

    @InjectMocks
    private IncomeServiceImpl incomeService;

    @Nested
    class CreateIncomeTests {
        @Test
        @Description(value = "should create a new income")
        void shouldCreateNewIncomeWithSuccess() {
            IncomeRequest createRequest = new IncomeRequest("Salário mensal", LocalDate.of(2026, 3, 16), new BigDecimal("3500.00"), 8);
            Integer clientId = 2;
            Client existingClient = new Client(2, "Matias", LocalDate.of(2004, 1, 13), "Mat Wagner", "matias@email.com", "12345", true);
            Category existingCategory = new Category(8, "Investimentos", "Rendimentos a partir de investimentos", new CategoryType(2, "INCOME", "tipo de receita"));
            Income expectedIncome = new Income(1, "Salário mensal", LocalDate.of(2026, 3, 16), new BigDecimal("3500.00"), null, null);

            given(categoryRepository.findById(createRequest.category())).willReturn(Optional.of(existingCategory));
            given(clientRepository.findById(clientId)).willReturn(Optional.of(existingClient));
            given(incomeMapper.toEntity(createRequest)).willReturn(expectedIncome);
            given(incomeRepository.save(any())).willReturn(expectedIncome);


            Income incomeCreated = incomeService.createIncome(createRequest, 2);

            verify(incomeRepository).save(incomeArgumentCaptor.capture());
            var incomeCaptorValue = incomeArgumentCaptor.getValue();
            assertNotNull(incomeCreated);
            assertEquals(existingClient, incomeCaptorValue.getClient());
            assertEquals(existingCategory, incomeCaptorValue.getCategory());
            assertEquals(createRequest.receivedDate(), incomeCaptorValue.getReceivedDate());
            assertEquals(createRequest.value(), incomeCaptorValue.getValue());
        }

        @Test
        void shouldThrowExceptionWhenCategoryNotFound() {

            IncomeRequest request = new IncomeRequest("Salário", LocalDate.now(), new BigDecimal("1000"), 99);
            Integer currentClient = 2;

            Client client = new Client(2, "Matias", LocalDate.of(2004, 1, 13), "Mat Wagner", "matias@email.com", "12345", true);

            given(clientRepository.findById(currentClient))
                    .willReturn(Optional.of(client));

            given(categoryRepository.findById(request.category()))
                    .willReturn(Optional.empty());

            NotFoundException exception = assertThrows(
                    NotFoundException.class,
                    () -> incomeService.createIncome(request, currentClient)
            );
            assertTrue(exception.getMessage().contains(request.category().toString()));
        }
    }

    @Nested
    class UpdateIncomeTests {

        @Test
        @Description(value = "should update a income")
        void shouldUpdateIncomeSuccess() {

            IncomeRequest updateRequest = new IncomeRequest("Salário", LocalDate.of(2026, 1, 16), new BigDecimal("3700.00"), 7);
            Integer clientId = 2;
            Integer incomeId = 3;
            Category existingCategory = new Category(7, "Vendas", "Vendas de Produtos", new CategoryType(2, "INCOME", "tipo de receita"));
            Income existingIncome = new Income(3, "Salário mensal", LocalDate.of(2026, 3, 16), new BigDecimal("3500.00"), null, null);
            IncomeResponse expectedResponse = new IncomeResponse(3, "Salário", LocalDate.of(2026, 1, 16), new BigDecimal("3700.00"), new CategoryResponse(7, "Vendas"));

            when(categoryRepository.findById(updateRequest.category())).thenReturn(Optional.of(existingCategory));
            when(incomeRepository.findByClient_IdAndId(clientId, incomeId)).thenReturn(Optional.of(existingIncome));
            when(incomeRepository.save(any())).thenReturn(existingIncome);
            when(incomeMapper.toResponse(existingIncome)).thenReturn(expectedResponse);

            IncomeResponse updatedIncomeResponse = incomeService.updateIncome(updateRequest, incomeId, clientId);

            verify(incomeRepository).save(incomeArgumentCaptor.capture());
            Income incomeUpdated = incomeArgumentCaptor.getValue();

            assertEquals(updateRequest.description(), incomeUpdated.getDescription());
            assertEquals(updateRequest.receivedDate(), incomeUpdated.getReceivedDate());
            assertEquals(updateRequest.value(), incomeUpdated.getValue());
            assertEquals(existingCategory, incomeUpdated.getCategory());

            assertNotNull(updatedIncomeResponse);
            assertEquals(incomeUpdated.getValue(), updatedIncomeResponse.value());
            assertEquals(incomeUpdated.getDescription(), updatedIncomeResponse.description());
            assertEquals(incomeUpdated.getId(), updatedIncomeResponse.id());
            assertEquals(incomeUpdated.getCategory().getId(), updatedIncomeResponse.category().id());
        }
    }



    @Test
    void deleteIncome() {
    }

    @Test
    void findAll() {
    }
}