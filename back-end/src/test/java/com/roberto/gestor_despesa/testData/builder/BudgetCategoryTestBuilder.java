package com.roberto.gestor_despesa.testData.builder;

import com.roberto.gestor_despesa.entities.*;
import jakarta.persistence.*;
import org.checkerframework.checker.units.qual.C;

import java.math.BigDecimal;

public class BudgetCategoryTestBuilder {

    private BudgetCategoryId id;

    private Budget budget;

    private Category category;

    private BigDecimal plannedValue;

    public BudgetCategoryTestBuilder() {
        this.id = new BudgetCategoryId(1, 1);
        this.budget = new BudgetTestBuilder().build();
        this.plannedValue = new BigDecimal(2700);
        this.category = new Category(1, "VENDAS", "Vendas em Geral", new CategoryType(1, "RECEITA", "entrada de dinheiro"));
    }

    public BudgetCategoryTestBuilder withId(Integer idBudget, Integer idCategory) {
        this.id = new BudgetCategoryId(idBudget, idCategory);
        return this;
    }

    public BudgetCategoryTestBuilder withBudget(Budget budget) {
        this.budget = budget;
        return this;
    }

    public BudgetCategoryTestBuilder withCategory(Category category) {
        this.category = category;
        return this;
    }

    public BudgetCategoryTestBuilder withPlannedValue(BigDecimal plannedValue) {
        this.plannedValue = plannedValue;
        return this;
    }

    public BudgetCategory build() {
        BudgetCategory budgetCategory = new BudgetCategory();

        budgetCategory.setId(id);
        budgetCategory.setBudget(budget);
        budgetCategory.setCategory(category);
        budgetCategory.setPlannedValue(plannedValue);

        return budgetCategory;
    }
}
