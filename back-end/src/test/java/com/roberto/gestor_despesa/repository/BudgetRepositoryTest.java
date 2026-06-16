package com.roberto.gestor_despesa.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.logging.Logger;

import com.roberto.gestor_despesa.entities.Budget;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.roberto.gestor_despesa.dtos.response.BudgetResponse;
import com.roberto.gestor_despesa.entities.enums.Status;
import com.roberto.gestor_despesa.testData.scenario.BudgetScenario;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@Sql("/sql/budget-data.sql")
class BudgetRepositoryTest {

    @Autowired
    private BudgetRepository repository;

    @Nested
    class searchBudgetTests {

        @ParameterizedTest
        @CsvSource(
                value = {
                        "1, ORCAMENTO, ACTIVE, 2024-09-01, 2024-11-12, 0, 5",
                        "2, '', ACTIVE, 2023-09-01, 2023-11-12, 0, 5",
                        "1, orcamento, ACTIVE, 2024-01-01, 2025-11-12, 0, 5",
                        "NULL, NULL, NULL, NULL, NULL, 0, 5"
                },
                nullValues = "NULL"
        )
        @DisplayName("Should search budgets when multiple filter combinations")
        void shouldSearchBudgetsWhenMultipleFilterCombinations(Integer clientId, String description, Status status, LocalDate dateStart, LocalDate dateEnd, Integer pageNumber, Integer pageSize) {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);

            Page<BudgetResponse> response = repository.searchBudgets(clientId, description, status, dateStart, dateEnd, pageable);

            assertNotNull(response, () -> "Response should not be null");
            assertNotNull(response.getContent(), () -> "Response content should not be null");
            assertEquals(pageNumber, response.getNumber(), () -> "Page number should match the expected value");
            assertEquals(pageSize, response.getSize(), () -> "Page size should match the expected value");
        }

        @Test
        @DisplayName("Should return budget when verifying total values")
        void shouldReturnBudgetWhenVerifyingTotalValue() {
            Integer clientId = 1;
            String description = "ORCAMENTO";
            Status status = Status.ACTIVE;
            LocalDate dateStart = LocalDate.of(2024, 9, 1);
            LocalDate dateEnd = LocalDate.of(2024, 11, 12);
            Integer pageNumber = 0;
            Integer pageSize = 5;

            Pageable pageable = PageRequest.of(pageNumber, pageSize);

            Page<BudgetResponse> response = repository.searchBudgets(clientId, description, status, dateStart, dateEnd, pageable);

            assertFalse(response.isEmpty());
            BudgetResponse budget = response.getContent().get(0);

            BigDecimal expectedTotalPlanned = new BigDecimal("1100.00");
            BigDecimal expectedTotalSpent = new BigDecimal("780.00");
            BigDecimal expectedTotalRemaining = new BigDecimal("320.00");

            assertEquals(expectedTotalPlanned, budget.totalPlanned(), () -> "Total planned should match the expected value");
            assertEquals(expectedTotalSpent, budget.totalSpent(), () ->  "Total spent should match the expected value");
            assertEquals(expectedTotalRemaining, budget.totalRemaining(), () -> "Total remaining should match the expected value");
        }
    }
}