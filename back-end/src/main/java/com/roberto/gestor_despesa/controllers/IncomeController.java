package com.roberto.gestor_despesa.controllers;

import com.roberto.gestor_despesa.dtos.request.IncomeRequest;
import com.roberto.gestor_despesa.dtos.response.IncomeResponse;
import com.roberto.gestor_despesa.entities.Income;
import com.roberto.gestor_despesa.services.IncomeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;

@Tag(name = "Income")
@RequiredArgsConstructor
@RequestMapping("api/incomes")
@RestController
public class IncomeController {

    private final IncomeService service;

    @PostMapping
    public ResponseEntity<Void> createIncome(@AuthenticationPrincipal Jwt jwt, @RequestBody @Valid IncomeRequest request) {

        Long idCurrentClient = jwt.getClaim("clientId");

        Income createdIncome = service.createIncome(request, idCurrentClient.intValue());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdIncome.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<IncomeResponse> updateIncome(@AuthenticationPrincipal Jwt jwt, @PathVariable("id") Integer id, @RequestBody @Valid IncomeRequest request) {
        Long idCurrentClient = jwt.getClaim("clientId");
        IncomeResponse response = service.updateIncome(request, id, idCurrentClient.intValue());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncomeResponse> findById(@AuthenticationPrincipal Jwt jwt, @PathVariable("id") Integer id) {
        Long idCurrentClient = jwt.getClaim("clientId");
        return ResponseEntity.ok(service.findById(idCurrentClient.intValue(), id));
    }

    @GetMapping
    public ResponseEntity<Page<IncomeResponse>> findAll(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "6") Integer pageSize,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) LocalDate dateStart,
            @RequestParam(required = false) LocalDate dateEnd,
            @RequestParam(required = false) BigDecimal valueStart,
            @RequestParam(required = false) BigDecimal valueEnd,
            @RequestParam(required = false) String category) {
        Long idCurrentClient = jwt.getClaim("clientId");

        Page<IncomeResponse> response = service.findAll(idCurrentClient.intValue(),valueStart, valueEnd, description, dateStart, dateEnd, category, pageNumber, pageSize);

        if (response.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@AuthenticationPrincipal Jwt jwt, @PathVariable("id") Integer id) {

        Long idCurrentClient = jwt.getClaim("clientId");

        service.deleteIncome(idCurrentClient.intValue(), id);

        return ResponseEntity.noContent().build();
    }
}
