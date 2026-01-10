package com.roberto.gestor_despesa.repository;

import com.roberto.gestor_despesa.entities.Budget;
import com.roberto.gestor_despesa.entities.Client;
import com.roberto.gestor_despesa.entities.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Integer>, JpaSpecificationExecutor<Budget> {

    Optional<Budget> findByIdAndClient_Id(Integer id, Integer clientId);


    boolean existsByClientAndPeriodReferenceAndStatus(Client client, LocalDate periodReference, Status status);
}
