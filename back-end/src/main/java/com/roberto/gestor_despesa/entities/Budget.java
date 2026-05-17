package com.roberto.gestor_despesa.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.roberto.gestor_despesa.entities.enums.PeriodType;
import com.roberto.gestor_despesa.entities.enums.Status;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table
@Entity
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String description;

    private BigDecimal totalPlanned;

    private LocalDate periodReference;

    private PeriodType periodType;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @OneToMany(mappedBy = "budget", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BudgetCategory> categories = new ArrayList<>();


    @OneToMany(mappedBy = "budget", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Expense> expense = new ArrayList<>();

    public Budget(String description, BigDecimal totalPlanned, LocalDate periodReference, PeriodType periodType, Status status, Client client) {
        this.description = description;
        this.totalPlanned = totalPlanned;
        this.periodReference = periodReference;
        this.periodType = periodType;
        this.status = status;
        this.client = client;

        this.categories = new ArrayList<>();
        this.expense = new ArrayList<>();
    }

    public void recalculateTotalPlanned() {
        this.totalPlanned = categories.stream()
                .map(BudgetCategory::getPlannedValue)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalSpent() {
        return expense.stream()
                .map(Expense::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void addCategory(BudgetCategory category) {
        categories.add(category);
        category.setBudget(this);
    }


}
