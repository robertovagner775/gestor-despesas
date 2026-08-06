package com.roberto.gestor_despesa.services;

import com.roberto.gestor_despesa.dtos.request.CategoryRequest;
import com.roberto.gestor_despesa.dtos.response.CategoryResponse;
import com.roberto.gestor_despesa.entities.Category;
import org.springframework.data.domain.Page;

public interface CategoryService {

    public Category save(CategoryRequest request, Integer idClient);

    public CategoryResponse update(CategoryRequest request, Integer id, Integer idClient);

    public CategoryResponse findCategoryById(Integer id, Integer idClient);

    public void delete(Integer id, Integer idClient);

    public Page<CategoryResponse> findAll(Integer idClient, Integer pageNumber, Integer pageSize, String title, String description);
}
