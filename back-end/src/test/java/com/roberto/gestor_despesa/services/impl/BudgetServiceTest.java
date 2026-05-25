package com.roberto.gestor_despesa.services.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.roberto.gestor_despesa.dtos.request.BudgetCategoryRequest;
import com.roberto.gestor_despesa.dtos.request.BudgetRequest;
import com.roberto.gestor_despesa.entities.Budget;
import com.roberto.gestor_despesa.entities.BudgetCategory;
import com.roberto.gestor_despesa.entities.BudgetCategoryId;
import com.roberto.gestor_despesa.entities.Category;
import com.roberto.gestor_despesa.entities.CategoryType;
import com.roberto.gestor_despesa.entities.Client;
import com.roberto.gestor_despesa.entities.enums.PeriodType;
import com.roberto.gestor_despesa.entities.enums.Status;
import com.roberto.gestor_despesa.handler.exceptions.ConflictEntityException;
import com.roberto.gestor_despesa.handler.exceptions.NotFoundException;
import com.roberto.gestor_despesa.repository.BudgetCategoryRepository;
import com.roberto.gestor_despesa.repository.BudgetRepository;
import com.roberto.gestor_despesa.repository.CategoryRepository;
import com.roberto.gestor_despesa.repository.ClientRepository;
import com.roberto.gestor_despesa.testData.builder.BudgetCategoryTestBuilder;
import com.roberto.gestor_despesa.testData.builder.BudgetTestBuilder;
import com.roberto.gestor_despesa.utils.DateUtils;

@ExtendWith(MockitoExtension.class)
@DisplayName(value = "Budget Service Test")
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

    @DisplayName(value = "Create a budget tests")
    @Nested
    class CreateBudgetTests {

        private BudgetRequest request;
        private Client client;
        private List<Category> categories;

        @BeforeEach
        void setup() {
            request = BudgetTestData.createBudgetRequest();
            client = new ClientTestBuilder().build();
            categories = BudgetTestData.createCategoryList();
        }

        @Test
        @DisplayName(value = "Should create a new budget when return success.")
        void shouldCreateNewBudgetWithSuccess() {

            Integer currentClient = 1;

            List<BudgetCategoryRequest> budgetCategoryRequest = request.budgetCategory();
            when(clientRepository.findById(currentClient)).thenReturn(Optional.of(client));
            when(categoryRepository.findById(budgetCategoryRequest.get(0).category_id())).thenReturn(Optional.of(categories.get(0)));
            when(categoryRepository.findById(budgetCategoryRequest.get(1).category_id())).thenReturn(Optional.of(categories.get(1)));
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
        @DisplayName(value = "Should return a Conflict Entity Exception when there is an active budget in the same period.")
        void shouldReturnConflictEntityExceptionWhenActiveBudgetInSamePeriod() {

            Integer currentClient = 1;
            LocalDate expectedDate = request.periodReference().atDay(1);

            when(clientRepository.findById(currentClient)).thenReturn(Optional.of(client));
            when(budgetRepository.existsByClientAndPeriodReferenceAndStatus(client, expectedDate, Status.ACTIVE)).thenReturn(true);

            ConflictEntityException exception = assertThrows(ConflictEntityException.class, () -> budgetService.createBudget(request, currentClient.longValue()));

            assertTrue(exception.getMessage().contains(request.periodReference().toString()));
        }
    }

    @DisplayName(value = "Update a budget tests")
    @Nested
    class UpdateBudgetTests {

        private BudgetRequest updatedBudgetRequest;

        @BeforeEach
        void setup() {
            updatedBudgetRequest = BudgetTestData.createBudgetRequest();
        }

        @DisplayName(value = "Should update a budget with success")
        @Test
        void shouldUpdateBudgetWithSuccess() {
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

        @DisplayName(value = "Should return Not Found Exception when budget or category not found")
        @Test
        void shouldReturnBudgetNotFoundException() {

            Integer idBudget = 1;

            when(budgetRepository.findById(idBudget)).thenReturn(Optional.empty());

            NotFoundException exception = assertThrows(NotFoundException.class, () -> budgetService.updateBudget(updatedBudgetRequest, idBudget));

            assertTrue(exception.getMessage().contains(idBudget.toString()));
        }

        @DisplayName(value = "Should return Not Found Exception when category not found")
        @Test
        void shouldReturnCategoryNotFoundException() {
            Integer idBudget = 1;

            Budget budget = new BudgetTestBuilder().build();

            when(budgetRepository.findById(idBudget)).thenReturn(Optional.of(budget));

            BudgetCategoryId bc1 = new BudgetCategoryId(1, 1);

            when(budgetCategoryRepository.findById(bc1)).thenReturn(Optional.empty());

            NotFoundException exception = assertThrows(NotFoundException.class, () -> budgetService.updateBudget(updatedBudgetRequest, idBudget));

            assertTrue(exception.getMessage().contains(idBudget.toString()));
        }
    }

}