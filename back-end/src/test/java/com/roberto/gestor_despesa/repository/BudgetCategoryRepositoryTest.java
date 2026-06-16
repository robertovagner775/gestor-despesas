package com.roberto.gestor_despesa.repository;

import com.roberto.gestor_despesa.dtos.response.BudgetCategoryResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@Sql("/sql/budget-data.sql")
class BudgetCategoryRepositoryTest {

    @Autowired
    private BudgetCategoryRepository budgetCategoryRepository;

    @Test
    void shouldReturnAllBudgetsWithSuccess() {
        Integer budgetId = 1;

        List<BudgetCategoryResponse> response = budgetCategoryRepository.findAllBudgetCategoryTotal(budgetId);

        response.forEach((r) -> {
            assertNotNull(r.idCategory(), () -> "Category ID should not be null");
            assertNotNull(r.title(), () -> "Title should not be null");
            assertEquals(r.remainingValue(),  r.plannedValue().subtract(r.spentValue()), () -> "Remaining value should be equal to planned value minus spent value");
        });
    }
}