package com.roberto.gestor_despesa.controllers;

import com.roberto.gestor_despesa.dtos.request.ClientRequestDTO;
import com.roberto.gestor_despesa.dtos.response.ClientResponseDTO;
import com.roberto.gestor_despesa.entities.Client;
import com.roberto.gestor_despesa.services.ClientService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Tag(name = "Clients")
@RequestMapping("api/clients")
@RestController
public class ClientController {

    private final ClientService service;

    public ClientController(ClientService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Void> register(@RequestBody @Valid ClientRequestDTO request) {
        Client client = service.save(request);
        return ResponseEntity.created(ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(client.getId())
                .toUri()).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> findById(@PathVariable("id") Integer id) {
       return ResponseEntity.ok(service.findClientById(id));
    }
}
