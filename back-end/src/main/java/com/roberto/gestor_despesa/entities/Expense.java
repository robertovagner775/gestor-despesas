package com.roberto.gestor_despesa.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String description;

    private LocalDate data;

    private BigDecimal value;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public Expense() {
    }

    public Expense(Integer id, Category category, Client client, BigDecimal value, LocalDate data, String description) {
        this.id = id;
        this.category = category;
        this.client = client;
        this.value = value;
        this.data = data;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDate() {
        return data;
    }

    public Client getClient() {
        return client;
    }

    public Category getCategory() {
        return category;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public void setDate(LocalDate data) {
        this.data = data;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
