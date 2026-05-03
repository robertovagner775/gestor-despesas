package com.roberto.gestor_despesa.services.impl;

import com.roberto.gestor_despesa.handler.exceptions.NotFoundException;
import com.roberto.gestor_despesa.testData.builder.BudgetCategoryTestBuilder;
import com.roberto.gestor_despesa.testData.builder.BudgetTestBuilder;
import com.roberto.gestor_despesa.dtos.request.BudgetCategoryRequest;
import com.roberto.gestor_despesa.dtos.request.BudgetRequest;
import com.roberto.gestor_despesa.entities.*;
import com.roberto.gestor_despesa.entities.enums.PeriodType;
import com.roberto.gestor_despesa.entities.enums.Status;
import com.roberto.gestor_despesa.handler.exceptions.ConflictEntityException;
import com.roberto.gestor_despesa.repository.BudgetCategoryRepository;
import com.roberto.gestor_despesa.repository.BudgetRepository;
import com.roberto.gestor_despesa.repository.CategoryRepository;
import com.roberto.gestor_despesa.repository.ClientRepository;
import com.roberto.gestor_despesa.utils.DateUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BudgetServiceTest {

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private DateUtils dateUtils;

    @Mock
    private BudgetCategoryRepository budgetCategoryRepository;

    @InjectMocks
    private BudgetServiceImpl budgetService;

    @Nested
    class CreateBudgetTests {

        @Test
        void shouldCreateNewBudgetWithSuccess() {

            Integer currentClient = 1;
            BudgetRequest request = createBudgetRequest();
            List<BudgetCategoryRequest> budgetCategoryRequests = request.budgetCategory();
            Client client = createClient(1);
            List<Category> categoryList = createCategoryList();

            when(clientRepository.findById(currentClient)).thenReturn(Optional.of(client));
            when(categoryRepository.findById(budgetCategoryRequests.get(0).category_id())).thenReturn(Optional.of(categoryList.get(0)));
            when(categoryRepository.findById(budgetCategoryRequests.get(1).category_id())).thenReturn(Optional.of(categoryList.get(1)));
            when(budgetRepository.save(any(Budget.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Budget budget = budgetService.createBudget(request, currentClient.longValue());

            BigDecimal expectedTotalPlannedValue = new BigDecimal(2700);

            assertEquals(expectedTotalPlannedValue, budget.getTotalPlanned());

            assertNotNull(budget);
            assertEquals(client, budget.getClient());
            assertEquals(2, budget.getCategories().size());
            assertEquals(new BigDecimal("2700"), budget.getTotalPlanned());

            verify(clientRepository).findById(currentClient);
            verify(categoryRepository, times(2)).findById(anyInt());
            verify(budgetRepository).save(any(Budget.class));
        }

        @Test
        @DisplayName(value = "It should return a Conflict Entity Exception when there is an active budget in the same period.")
        void shouldReturnConflictEntityExceptionWhenActiveBudgetInSamePeriod() {

            Integer currentClient = 1;
            BudgetRequest request = createBudgetRequest();
            Client client = createClient(currentClient);
            LocalDate expectedDate = request.periodReference().atDay(1);

            when(clientRepository.findById(currentClient)).thenReturn(Optional.of(client));
            when(budgetRepository.existsByClientAndPeriodReferenceAndStatus(client, expectedDate, Status.ACTIVE)).thenReturn(true);

            ConflictEntityException exception = assertThrows(ConflictEntityException.class, () -> budgetService.createBudget(request, currentClient.longValue()));

            assertTrue(exception.getMessage().contains(request.periodReference().toString()));
        }
    }

    @Nested
    class UpdateBudgetTests {

        @Test
        void shouldUpdateBudgetWithSuccess() {
            BudgetRequest updatedBudgetRequest = createBudgetRequest();
            Integer idBudget = 1;

            Budget budget = new BudgetTestBuilder()
                    .withDescription(updatedBudgetRequest.description())
                    .withPeriodReference(updatedBudgetRequest.periodReference().atDay(1))
                    .build();

            BudgetCategoryId budgetCategoryId1 = new BudgetCategoryId(idBudget, 1);
            BudgetCategoryId budgetCategoryId2 = new BudgetCategoryId(idBudget, 2);

            BudgetCategory budgetCategory0 = new BudgetCategoryTestBuilder()
                    .withId(1, 1)
                    .withPlannedValue(new BigDecimal(1200))
                    .build();

            BudgetCategory budgetCategory1 = new BudgetCategoryTestBuilder()
                    .withId(1, 2)
                    .withPlannedValue(new BigDecimal(1500))
                    .build();

            when(budgetRepository.findById(idBudget)).thenReturn(Optional.of(budget));

            when(budgetCategoryRepository.findById(budgetCategoryId1)).thenReturn(Optional.of(budgetCategory0));

            when(budgetCategoryRepository.findById(budgetCategoryId2)).thenReturn(Optional.of(budgetCategory1));


            when(budgetRepository.save(any(Budget.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Budget budgetUpdated = budgetService.updateBudget(updatedBudgetRequest, idBudget);

            assertEquals(new BigDecimal(2700), budgetUpdated.getTotalPlanned());
            assertEquals(2, budgetUpdated.getCategories().size());
        }

        @Test
        void shouldReturnBudgetNotFoundException() {

            BudgetRequest updatedBudgetRequest = createBudgetRequest();
            Integer idBudget = 1;

            when(budgetRepository.findById(idBudget)).thenReturn(Optional.empty());

            NotFoundException exception = assertThrows(NotFoundException.class, () -> budgetService.updateBudget(updatedBudgetRequest, idBudget));

            assertTrue(exception.getMessage().contains(idBudget.toString()));
        }

        @Test
        void shouldReturnCategoryNotFoundException() {
            BudgetRequest updatedBudgetRequest = createBudgetRequest();
            Integer idBudget = 1;

            Budget budget = new BudgetTestBuilder().build();

            when(budgetRepository.findById(idBudget)).thenReturn(Optional.of(budget));

            BudgetCategoryId bc1 = new BudgetCategoryId(1, 1);

            when(budgetCategoryRepository.findById(bc1)).thenReturn(Optional.empty());

            NotFoundException exception = assertThrows(NotFoundException.class, () -> budgetService.updateBudget(updatedBudgetRequest, idBudget));

            assertTrue(exception.getMessage().contains(idBudget.toString()));
        }
    }

    private Client createClient(Integer id) {
        return new Client(id, "robertovagner", LocalDate.now(), "Roberto Vagner",
                "robertovagner@email.com", "12131", true);
    }

    private List<Category> createCategoryList() {
        CategoryType type = new CategoryType(1, "DESPESA", "Saída");
        Category category0 = new Category(1, "Compras", "Compras em Geral", type);
        Category category1 = new Category(2, "Alimentação", "Gastos com alimentação", type);
        return List.of(category0, category1);
    }

    private BudgetRequest createBudgetRequest() {
        var req0 = new BudgetCategoryRequest(1, new BigDecimal("1500"));
        var req1 = new BudgetCategoryRequest(2, new BigDecimal("1200"));
        return new BudgetRequest("Orcamento Abril", YearMonth.of(2026, 4),
                PeriodType.MONTH, List.of(req0, req1));
    }

}