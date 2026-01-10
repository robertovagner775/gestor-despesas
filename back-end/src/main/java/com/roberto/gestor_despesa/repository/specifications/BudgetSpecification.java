package com.roberto.gestor_despesa.repository.specifications;

import com.roberto.gestor_despesa.entities.Budget;
import com.roberto.gestor_despesa.entities.BudgetCategory;
import com.roberto.gestor_despesa.entities.Category;
import com.roberto.gestor_despesa.entities.Client;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.hibernate.Cache;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class BudgetSpecification {

    public static Specification<Budget> descriptionLike(String description) {
        return (root, query, cb) -> cb.like(root.get("description"), "%" + description + "%");
    }

    public static Specification<Budget> equalsStatus(String status) {
        return ((root, query, cb) -> cb.equal( root.get("status"), status));
    }

    public static Specification<Budget> likeCategory(String category) {
        return  (root, query, cb) -> {

            Join<Budget, BudgetCategory> budgetBudgetCategoryJoin = root.join("categories", JoinType.INNER);

            Join<BudgetCategory, Category> categoryJoin =  budgetBudgetCategoryJoin.join("category", JoinType.INNER);

            return cb.like(cb.lower(categoryJoin.get("title")), "%" + category.toLowerCase() + "%");
        };
    }

    public static Specification<Budget> dateBetween(LocalDate dateStart, LocalDate dateEnd) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("periodReference"), dateStart, dateEnd));
    }

    public static Specification<Budget> equalClient(Integer idClient) {
        return (((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("client").get("id"), idClient)));
    }
}
