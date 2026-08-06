package com.roberto.gestor_despesa.services.impl;

import com.roberto.gestor_despesa.dtos.mapper.CategoryMapper;
import com.roberto.gestor_despesa.dtos.request.CategoryRequest;
import com.roberto.gestor_despesa.dtos.response.CategoryResponse;
import com.roberto.gestor_despesa.entities.Category;
import com.roberto.gestor_despesa.entities.CategoryType;
import com.roberto.gestor_despesa.entities.Client;
import com.roberto.gestor_despesa.handler.exceptions.NotFoundException;
import com.roberto.gestor_despesa.repository.CategoryRepository;
import com.roberto.gestor_despesa.repository.CategoryTypeRepository;
import com.roberto.gestor_despesa.repository.ClientRepository;
import com.roberto.gestor_despesa.services.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ClientRepository clientRepository;
    private final CategoryTypeRepository categoryTypeRepository;
    private final CategoryMapper mapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository,ClientRepository clientRepository,CategoryTypeRepository categoryTypeRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.clientRepository = clientRepository;
        this.categoryTypeRepository = categoryTypeRepository;
        this.mapper = categoryMapper;
    }

    @Override
    public Category save(CategoryRequest request, Integer clientId) {
        Category category = mapper.toEntity(request);

        Client client = clientRepository.findById(clientId).orElseThrow(() -> new NotFoundException(clientId));
        CategoryType categoryType = categoryTypeRepository.findById(request.categoryTypeId()).orElseThrow(() -> new NotFoundException(request.categoryTypeId()));

        category.setClient(client);
        category.setCategoryType(categoryType);

        return categoryRepository.save(category);
    }

    @Override
    public CategoryResponse update(CategoryRequest request, Integer id, Integer clientId) {
        return null;
    }

    @Override
    public CategoryResponse findCategoryById(Integer id, Integer clientId) {
        return null;
    }

    @Override
    public void delete(Integer id, Integer clientId) {

    }

    @Override
    public Page<CategoryResponse> findAll(Integer clientId, Integer pageNumber, Integer pageSize, String title, String description) {
        return null;
    }
}
