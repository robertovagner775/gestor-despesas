package com.roberto.gestor_despesa.testData.builder;

import com.roberto.gestor_despesa.entities.Budget;
import com.roberto.gestor_despesa.entities.BudgetCategory;
import com.roberto.gestor_despesa.entities.Client;
import com.roberto.gestor_despesa.entities.Expense;
import com.roberto.gestor_despesa.entities.enums.PeriodType;
import com.roberto.gestor_despesa.entities.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BudgetTestBuilder {

    private Integer id;
    private String description;
    private BigDecimal totalPlanned;
    private LocalDate periodReference;
    private PeriodType periodType;
    private Status status;
    private Client client;
    private List<BudgetCategory> categories;
    private List<Expense> expense;


    public BudgetTestBuilder() {
        this.id = 1;
        this.description = "Budget 20/04/2026";
        this.totalPlanned = new BigDecimal(2000);
        this.periodReference = LocalDate.of(2026, 04, 01);
        this.periodType = PeriodType.MONTH;
        this.status = Status.ACTIVE;
        this.client = new ClientTestBuilder().build();
        this.categories = new ArrayList<>();
        this.expense = new ArrayList<>();
    }

    public BudgetTestBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public BudgetTestBuilder withPeriodReference(LocalDate periodReference) {
        this.periodReference = periodReference;
        return this;
    }

    public  BudgetTestBuilder withClient(Client client) {
        this.client = client;
        return this;
    }

    public BudgetTestBuilder withBudgetCategories(BudgetCategory... categories) {
        this.categories = Arrays.asList(categories);
        return this;
    }

    public BudgetTestBuilder withExpenses(Expense... expense) {
        this.expense = Arrays.asList(expense);
        return this;
    }

    public Budget build() {
        return new Budget(id, description, totalPlanned, periodReference, periodType, status, client, categories, expense);
    }
}
