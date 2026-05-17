package com.roberto.gestor_despesa.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", nullable = false)
    private CategoryType categoryType;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    public Category(String title, String description, CategoryType categoryType, Client client) {
        this.title = title;
        this.description = description;
        this.categoryType = categoryType;
        this.client = client;
    }
}
