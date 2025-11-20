package com.roberto.gestor_despesa.controllers;

import com.roberto.gestor_despesa.dtos.request.BudgetRequestDTO;
import com.roberto.gestor_despesa.dtos.response.BudgetResponseDTO;
import com.roberto.gestor_despesa.entities.Budget;
import com.roberto.gestor_despesa.services.BudgetService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;

@RequestMapping("api/budgets")
@RestController
public class BudgetController {

    private final BudgetService service;

    public BudgetController(BudgetService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Void> createBudget(@AuthenticationPrincipal Jwt jwt, @RequestBody @Valid BudgetRequestDTO request) {
        Long idClient = jwt.getClaim("clientId");

        Budget budgetCreated = this.service.createTotalBudget(request, idClient);

       URI location = ServletUriComponentsBuilder
               .fromCurrentRequest()
               .path("/{id}")
               .buildAndExpand(budgetCreated.getId())
               .toUri();
       return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetResponseDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findBudgetbyId(id));
    }
}
