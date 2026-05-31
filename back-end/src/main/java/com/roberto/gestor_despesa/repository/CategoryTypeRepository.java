package com.roberto.gestor_despesa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.roberto.gestor_despesa.entities.CategoryType;

@Repository
public interface CategoryTypeRepository extends JpaRepository<CategoryType, Integer> {
}
