package com.roberto.gestor_despesa.repository;

import java.time.LocalDate;

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


@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class BudgetRepositoryTest {

    @Autowired
    private BudgetRepository repository;

    @Autowired
    private BudgetScenario scenario;

    @ParameterizedTest
    @CsvSource({"1, 'Salário recebido ao longo dos meses', ACTIVE, 2026-01-01, 2026-03-12, 0, 5"})
    void searchBudgets(Integer clientId, String description, Status status, LocalDate dateStart, LocalDate dateEnd, Integer pageNumber, Integer pageSize) {

        scenario.createBudgetWithExpensesForMonthlyTotals();

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<BudgetResponse> response =  repository.searchBudgets(clientId, description, status, dateStart, dateEnd, pageable);
    }
}