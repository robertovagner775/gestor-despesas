package com.roberto.gestor_despesa.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "budget_category")
public class BudgetCategory {

    @EmbeddedId
    private BudgetCategoryId id = new BudgetCategoryId();

    @ManyToOne
    @MapsId("budgetId")
    @JoinColumn(name = "budget_id")
    private Budget budget;

    @ManyToOne
    @MapsId("categoryId")
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(nullable = false, precision = 9, scale = 2)
    private BigDecimal value;

    public BudgetCategory() {
    }

    public BudgetCategory(Category category, BigDecimal value, Budget budget) {
        this.category = category;
        this.value = value;
        this.budget = budget;
    }

    public BudgetCategoryId getId() {
        return id;
    }

    public void setId(BudgetCategoryId id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Budget getBudget() {
        return budget;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}