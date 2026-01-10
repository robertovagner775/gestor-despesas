package com.roberto.gestor_despesa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class BudgetCategoryId implements Serializable {

    @Column(name = "budget_id")
    private Integer budgetId;

    @Column(name = "category_id")
    private Integer categoryId;

    public BudgetCategoryId() {
    }

    public BudgetCategoryId(Integer budgetId, Integer categoryId) {
        this.budgetId = budgetId;
        this.categoryId = categoryId;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BudgetCategoryId that = (BudgetCategoryId) o;
        return Objects.equals(budgetId, that.budgetId) && Objects.equals(categoryId, that.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(budgetId, categoryId);
    }
}
