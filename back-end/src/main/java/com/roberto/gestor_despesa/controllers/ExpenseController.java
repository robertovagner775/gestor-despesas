package com.roberto.gestor_despesa.controllers;

import com.roberto.gestor_despesa.dtos.request.ExpenseRequest;
import com.roberto.gestor_despesa.dtos.response.ExpenseResponse;
import com.roberto.gestor_despesa.entities.Expense;
import com.roberto.gestor_despesa.services.ExpenseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.time.YearMonth;


@Tag(name = "Expense")
@RequestMapping("api/expense")
@RestController
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    public ResponseEntity<Void> save(@AuthenticationPrincipal Jwt jwt, @RequestBody @Valid ExpenseRequest request) {

        Long idClient = jwt.getClaim("clientId");

       Expense createdExpense = expenseService.save(request, idClient.intValue());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdExpense.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponse> updateExpense(@PathVariable Integer id, @RequestBody @Valid ExpenseRequest request) {
        ExpenseResponse response = expenseService.update(request, id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponse> findById(@PathVariable Integer id) {
        ExpenseResponse response = expenseService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<ExpenseResponse>> findAll(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "6") Integer pageSize,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) YearMonth periodRefence,
            @RequestParam(required = false) BigDecimal valueStart,
            @RequestParam(required = false) BigDecimal valueEnd,
            @RequestParam(required = false) String category) {
        Long idCurrentClient = jwt.getClaim("clientId");
       Page<ExpenseResponse> listExpense = expenseService.findAll(pageNumber, pageSize, category,  description, periodRefence, valueStart, valueEnd, idCurrentClient.intValue());
       if (listExpense.isEmpty()) return ResponseEntity.noContent().build();
       return ResponseEntity.ok(listExpense);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Integer id) {
        expenseService.delete(id);
        return ResponseEntity.noContent().build();
    }

 }
