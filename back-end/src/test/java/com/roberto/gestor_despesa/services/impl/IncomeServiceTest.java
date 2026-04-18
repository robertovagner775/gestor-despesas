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
import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IncomeServiceTest {

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
    private ArgumentCaptor<Specification<Income>> specificationArgumentCaptor;

    @Captor
    private ArgumentCaptor<Pageable> pageableArgumentCaptor;

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

        @Test
        void shouldThrowExceptionWhenCategoryNotFound() {
            IncomeRequest updateRequest = new IncomeRequest("Salário", LocalDate.of(2026, 1, 16), new BigDecimal("3700.00"), 7);
            Integer clientId = 2;
            Integer incomeId = 3;
            Income existingIncome = new Income(3, "Salário mensal", LocalDate.of(2026, 3, 16), new BigDecimal("3500.00"), null, null);

            when(incomeRepository.findByClient_IdAndId(clientId, incomeId)).thenReturn(Optional.of(existingIncome));
            when(categoryRepository.findById(updateRequest.category())).thenReturn(Optional.empty());

            NotFoundException exception = assertThrows(
                    NotFoundException.class,
                    () -> incomeService.updateIncome(updateRequest, incomeId, clientId)
            );
            assertTrue(exception.getMessage().contains(updateRequest.category().toString()));
        }

        @Test
        void shouldThrowExceptionWhenIncomeNotFound() {
            IncomeRequest updateRequest = new IncomeRequest("Salário", LocalDate.of(2026, 1, 16), new BigDecimal("3700.00"), 7);
            Integer clientId = 2;
            Integer incomeId = 3;

            when(incomeRepository.findByClient_IdAndId(clientId, incomeId)).thenReturn(Optional.empty());

            NotFoundException exception = assertThrows(
                    NotFoundException.class,
                    () -> incomeService.updateIncome(updateRequest, incomeId, clientId)
            );
            assertTrue(exception.getMessage().contains(incomeId.toString()));
        }
    }

    @DisplayName(value = "Find all income tests")
    @Nested
    class findAllTests {

        @DisplayName(value = "Should return paginated incomes when filters are applied")
        @ParameterizedTest
        @CsvSource({"2, 50.00, 200.00, 'Salário recebido ao longo dos meses', 2026-01-01, 2026-03-12, 'Investimento', 0, 5"})
        void shouldReturnPagedIncomesWhenFilteringByParameters(Integer idCurrentClient, BigDecimal valueStart, BigDecimal valueEnd, String description, LocalDate dateStart, LocalDate dateEnd, String category, Integer pageNumber, Integer pageSize) {

            Client client = new Client(2, "Matias", LocalDate.of(2004, 1, 13), "Mat Wagner", "matias@email.com", "12345", true);
            CategoryType type = new CategoryType(2, "RECEITA", "entrada de dinheiro");

            Category category0 = new Category(1, "VENDA", "Venda de materiais", type);
            Category category1 = new Category(2, "INVESTIMENTO", "Investimentos em geral", type);

            Income income0 = new Income(1, "Vendi meu celular", LocalDate.of(2026, 01, 01), new BigDecimal(1500.00), category0 , client);
            Income income1 = new Income(2, "Recebimento de Rendimentos de Investimento em Bolsa de Valores", LocalDate.of(2026, 01, 01), new BigDecimal(1000.00), category1, client);

            List<Income> incomes = List.of(income0, income1);

            Page<Income> incomesPage = new PageImpl<>(incomes);

            when(incomeRepository.findAll((Specification<Income>)  any(), any(Pageable.class))).thenReturn(incomesPage);

            Page<IncomeResponse> result = incomeService.findAll(idCurrentClient, valueStart, valueEnd, description, dateStart, dateEnd, category, pageNumber, pageSize);

            verify(incomeRepository).findAll(specificationArgumentCaptor.capture(), pageableArgumentCaptor.capture());
            verify(incomeMapper, times(2)).toResponse(incomeArgumentCaptor.capture());

            Specification<Income> incomeSpecification = specificationArgumentCaptor.getValue();
            Pageable pageable = pageableArgumentCaptor.getValue();

            assertEquals(result.getTotalElements(), incomes.size());
            assertEquals(pageable.getPageNumber(), pageNumber);
            assertEquals(pageable.getPageSize(), pageSize);
            assertNotNull(incomeSpecification);
        }

    }

    @DisplayName("Delete Income Tests")
    @Nested
    class DeleteIncomeTests {

        @Test
        @DisplayName(value = "Should delete income when it exists for the given client")
        void shouldDeleteIncomeWhenIncomeExists() {
            Integer currentClient = 3;
            Integer idIncome = 2;
            Income income = new Income();

            when(incomeRepository.findByClient_IdAndId(currentClient, idIncome)).thenReturn(Optional.of(income));

            incomeService.deleteIncome(3, 2);

            verify(incomeRepository).delete(income);
        }

        @Test
        @DisplayName(value = "Should throw exception when income does not exist")
        void shouldThrowExceptionWhenIncomeNotFound() {
            Integer currentClient = 3;
            Integer idIncome = 2;
            Income income = new Income();

            when(incomeRepository.findByClient_IdAndId(currentClient, idIncome)).thenReturn(Optional.empty());

            NotFoundException exception = assertThrows(NotFoundException.class, () -> incomeService.deleteIncome(currentClient, idIncome));

            assertEquals(idIncome, exception.getIdEntity());
            assertTrue(exception.getMessage().contains(idIncome.toString()));
            verify(incomeRepository, never()).delete(income);
        }

    }
}