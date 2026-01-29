package com.roberto.gestor_despesa.repository.specifications;

import com.roberto.gestor_despesa.entities.Budget;
import com.roberto.gestor_despesa.entities.BudgetCategory;
import com.roberto.gestor_despesa.entities.Category;
import com.roberto.gestor_despesa.entities.Expense;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

public class ExpenseSpecification {

    public static Specification<Expense> descriptionLike(String description) {
        return (root, query, cb) -> cb.like(root.get("description"), "%" + description + "%");
    }

    public static Specification<Expense> valueBetween(BigDecimal valueStart, BigDecimal valueEnd) {
        return (root, query, cb) -> cb.between(root.get("value"), valueStart, valueEnd);
    }

    public static Specification<Expense> dateBetween(YearMonth date) {
        LocalDate dateStart = date.atDay(1);
        LocalDate dateEnd = date.atEndOfMonth();
        return ((root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("periodReference"), dateStart, dateEnd));
    }

    public static  Specification<Expense> likeCategory(String category) {
        return  (root, query, cb) -> {

            Join<Expense, Category> expenseCategoryJoin = root.join("category", JoinType.INNER);

            return cb.like(cb.lower(expenseCategoryJoin.get("title")), "%" + category.toLowerCase() + "%");
        };
    }

    public static Specification<Expense> equalClient(Integer idClient) {
        return (((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("client").get("id"), idClient)));
    }


}
