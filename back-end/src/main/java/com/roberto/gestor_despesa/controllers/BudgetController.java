package com.roberto.gestor_despesa.controllers;

import com.roberto.gestor_despesa.dtos.request.BudgetRequest;
import com.roberto.gestor_despesa.dtos.response.BudgetResponse;
import com.roberto.gestor_despesa.entities.Budget;
import com.roberto.gestor_despesa.services.BudgetService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@Tag(name = "Budgets")
@RequestMapping("api/budgets")
@RestController
public class BudgetController {

    private final BudgetService service;

    public BudgetController(BudgetService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Void> createBudget(@AuthenticationPrincipal Jwt jwt, @RequestBody @Valid BudgetRequest request) {
        Long idClient = jwt.getClaim("clientId");

        Budget budgetCreated = this.service.createTotalBudget(request, idClient);

       URI location = ServletUriComponentsBuilder
               .fromCurrentRequest()
               .path("/{id}")
               .buildAndExpand(budgetCreated.getId())
               .toUri();
       return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateBudget(@PathVariable Integer id,  @RequestBody @Valid BudgetRequest request) {
        Budget budgetUpdated = service.updateBudget(request, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetResponse> getById(@AuthenticationPrincipal Jwt jwt, @PathVariable Integer id) {
        Long idClient = jwt.getClaim("clientId");
        return ResponseEntity.ok(service.findBudgetByIdAndClient(id, idClient.intValue()));
    }

    @GetMapping
    public List<BudgetResponse> searchBudgets(@AuthenticationPrincipal Jwt jwt, @RequestParam(required = false) String description,@RequestParam(required = false)  LocalDate dateStart, @RequestParam(required = false)  LocalDate dateEnd, @RequestParam(required = false) String status,   @RequestParam(required = false) String category) {
        Long idClient = jwt.getClaim("clientId");

          List<BudgetResponse> budgetResponses =  this.service.searchBudgets(idClient.intValue(), description, status, dateStart, dateEnd, category);
          return budgetResponses;
    }
}
