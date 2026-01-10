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
    private BigDecimal plannedValue;

    @Column(nullable = false, precision = 9, scale = 2)
    private BigDecimal spentValue;

    @Column(nullable = false, precision = 9, scale = 2)
    private BigDecimal remainingValue;


    public BudgetCategory() {

    }

    public BudgetCategory(BigDecimal remainingValue, BigDecimal spentValue, BigDecimal plannedValue, Category category, Budget budget) {
        this.remainingValue = remainingValue;
        this.spentValue = spentValue;
        this.plannedValue = plannedValue;
        this.category = category;
        this.budget = budget;
    }

    public BudgetCategoryId getId() {
        return id;
    }

    public void setId(BudgetCategoryId id) {
        this.id = id;
    }

    public BigDecimal getRemainingValue() {
        return remainingValue;
    }

    public void setRemainingValue(BigDecimal remainingValue) {
        this.remainingValue = remainingValue;
    }

    public BigDecimal getSpentValue() {
        return spentValue;
    }

    public void setSpentValue(BigDecimal spentValue) {
        this.spentValue = spentValue;
    }

    public BigDecimal getPlannedValue() {
        return plannedValue;
    }

    public void setPlannedValue(BigDecimal plannedValue) {
        this.plannedValue = plannedValue;
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
}