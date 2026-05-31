package com.roberto.gestor_despesa.testData.builder;

import com.roberto.gestor_despesa.entities.Category;
import com.roberto.gestor_despesa.entities.CategoryType;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class CategoryTestBuilder {

    private Integer id;
    private String title;
    private String description;
    private CategoryType categoryType;

    public CategoryTestBuilder() {
        this.id = 1;
        this.title = "VENDAS";
        this.description = "Vendas de Produtos";
        this.categoryType = new CategoryType(1, "RECEITA", "Entrada de Dinheiro");
    }

    public CategoryTestBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public CategoryTestBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public CategoryTestBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public CategoryTestBuilder withCategoryType(CategoryType categoryType) {
        this.categoryType = categoryType;
        return this;
    }

    public Category build() {
        Category category = new Category();

        category.setId(id);
        category.setTitle(title);
        category.setDescription(description);
        category.setCategoryType(categoryType);

        return category;
    }
}
