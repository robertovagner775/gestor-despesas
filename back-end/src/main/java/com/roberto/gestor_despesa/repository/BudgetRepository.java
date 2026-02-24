package com.roberto.gestor_despesa.repository;

import com.roberto.gestor_despesa.dtos.response.BudgetResponse;
import com.roberto.gestor_despesa.entities.Budget;
import com.roberto.gestor_despesa.entities.Client;
import com.roberto.gestor_despesa.entities.enums.PeriodType;
import com.roberto.gestor_despesa.entities.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Integer>, JpaSpecificationExecutor<Budget> {

    @Query(nativeQuery = false, value = """
    SELECT new com.roberto.gestor_despesa.dtos.response.BudgetResponse(
        b.id,
        b.description,
        (SELECT COALESCE(SUM(bc.plannedValue), 0)
         FROM BudgetCategory bc
         WHERE bc.budget = b),
        (SELECT COALESCE(SUM(e.value), 0)
         FROM Expense e
         WHERE e.budget = b),
        (SELECT COALESCE(SUM(bc.plannedValue), 0)
         FROM BudgetCategory bc
         WHERE bc.budget = b)
        -
        (SELECT COALESCE(SUM(e.value), 0)
         FROM Expense e
         WHERE e.budget = b),
        b.periodReference,
        b.periodType,
        b.status
    )
    FROM Budget b
    WHERE b.client.id = :clientId
      AND (LOWER(b.description) LIKE CONCAT('%', LOWER(:description) , '%') OR :description IS NULL)
      AND (:status IS NULL OR b.status = :status)
      AND (b.periodReference BETWEEN :dateStart AND :dateEnd or (cast(:dateStart as DATE) IS NULL AND cast(:dateEnd as DATE) IS NULL))
    """)
    Page<BudgetResponse> searchBudgets(
            @Param("clientId") Integer clientId,
            @Param("description") String description,
            @Param("status") Status status,
            @Param("dateStart") LocalDate dateStart,
            @Param("dateEnd") LocalDate dateEnd,
            Pageable pageable
    );

    Optional<Budget> findByIdAndClient_Id(Integer id, Integer clientId);

    boolean existsByClientAndPeriodReferenceAndStatus(Client client, LocalDate periodReference, Status status);

    Optional<Budget> findByClientAndPeriodReferenceAndStatus(Client client, LocalDate periodReference, Status status);
}
