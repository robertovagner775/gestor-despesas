package com.roberto.gestor_despesa.controllers;

import com.roberto.gestor_despesa.dtos.request.CategoryRequest;
import com.roberto.gestor_despesa.dtos.response.CategoryResponse;
import com.roberto.gestor_despesa.entities.Category;
import com.roberto.gestor_despesa.services.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Tag(name = "Categories")
@RequestMapping("api/categories")
@RestController
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<Void> createCategory(@AuthenticationPrincipal Jwt jwt, @RequestBody @Valid CategoryRequest categoryRequest) {

        Long idClient = jwt.getClaim("clientId");

        Category categoryCreated = categoryService.save(categoryRequest, idClient.intValue());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(categoryCreated.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
