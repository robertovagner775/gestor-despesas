package com.roberto.gestor_despesa.repository;

import com.roberto.gestor_despesa.dtos.response.BudgetCategoryResponse;
import com.roberto.gestor_despesa.entities.BudgetCategory;
import com.roberto.gestor_despesa.entities.BudgetCategoryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BudgetCategoryRepository extends JpaRepository<BudgetCategory, BudgetCategoryId> {

    @Query(nativeQuery = false, value = """
            SELECT
                bc.category.id,
                bc.category.title,
                COALESCE(SUM(bc.plannedValue), 0),
                COALESCE(SUM(e.value), 0),
                COALESCE(SUM(bc.plannedValue), 0) - COALESCE(SUM(e.value), 0)
            FROM BudgetCategory bc
            LEFT JOIN Expense e
                ON e.budget.id = bc.budget.id
                AND e.category.id = bc.category.id
            WHERE bc.budget.id = :idBudget
            GROUP BY bc.category.id, bc.category.title      
            """
    )
    List<BudgetCategoryResponse> findAllBudgetCategoryTotal(@Param("idBudget") Integer idBudget);
}
