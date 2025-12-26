package com.roberto.gestor_despesa.repository;

import com.roberto.gestor_despesa.entities.ConfirmAccountToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmAccountTokenRepository extends JpaRepository<ConfirmAccountToken, Integer> {

    ConfirmAccountToken findByToken(String token);
}
