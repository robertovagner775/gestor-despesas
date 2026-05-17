package com.roberto.gestor_despesa.entities;

import com.roberto.gestor_despesa.entities.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    private LocalDate paidDate;

    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "budget_id", nullable = false)
    private Budget budget;

    public Expense(String description, PaymentMethod paymentMethod, LocalDate paidDate, BigDecimal amount, Client client, Category category, Budget budget) {
        this.description = description;
        this.paymentMethod = paymentMethod;
        this.paidDate = paidDate;
        this.amount = amount;
        this.client = client;
        this.category = category;
        this.budget = budget;
    }
}
