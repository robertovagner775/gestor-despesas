package com.roberto.gestor_despesa.repository.specifications;

import com.roberto.gestor_despesa.entities.Category;
import com.roberto.gestor_despesa.entities.Expense;
import com.roberto.gestor_despesa.entities.Income;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

public class IncomeSpecification {

    public static Specification<Income> descriptionLike(String description) {
        return (root, query, cb) -> cb.like(root.get("description"), "%" + description + "%");
    }

    public static Specification<Income> valueBetween(BigDecimal valueStart, BigDecimal valueEnd) {
        return (root, query, cb) -> cb.between(root.get("value"), valueStart, valueEnd);
    }

    public static Specification<Income> dateBetween(LocalDate dateStart, LocalDate dateEnd) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("receivedDate"), dateStart, dateEnd));
    }

    public static  Specification<Income> likeCategory(String category) {
        return  (root, query, cb) -> {

            Join<Expense, Category> expenseCategoryJoin = root.join("category", JoinType.INNER);

            return cb.like(cb.lower(expenseCategoryJoin.get("title")), "%" + category.toLowerCase() + "%");
        };
    }

    public static Specification<Income> equalClient(Integer idClient) {
        return (((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("client").get("id"), idClient)));
    }
}
