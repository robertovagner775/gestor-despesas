package com.roberto.gestor_despesa.controllers;

import com.roberto.gestor_despesa.dtos.request.ClientRequestDTO;
import com.roberto.gestor_despesa.dtos.response.ClientResponseDTO;
import com.roberto.gestor_despesa.entities.Client;
import com.roberto.gestor_despesa.services.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RequestMapping("api/client/")
@RestController
public class ClientController {

    private ClientService service;

    @PostMapping
    public ResponseEntity<Void> register(@RequestBody ClientRequestDTO request) {
        Client client = service.save(request);
        return ResponseEntity.created(ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(client.getId())
                .toUri()).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> findById(@PathVariable("ïd") Integer id) {
       return ResponseEntity.ok(service.findClientById(id));
    }
}
