package com.roberto.gestor_despesa.repository;

import com.roberto.gestor_despesa.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
