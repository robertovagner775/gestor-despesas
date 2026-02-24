package com.roberto.gestor_despesa.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
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


    public void sumPlannedValue(BigDecimal value) {
        this.plannedValue = plannedValue.add(value);
    }

}