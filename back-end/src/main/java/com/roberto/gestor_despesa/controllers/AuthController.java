package com.roberto.gestor_despesa.controllers;


import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.roberto.gestor_despesa.dtos.request.LoginRequest;
import com.roberto.gestor_despesa.dtos.response.LoginResponse;
import com.roberto.gestor_despesa.services.AuthService;


@Tag(name = "Auth")
@RequestMapping("api/auth")
@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> autheticate(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/confirm")
    public ResponseEntity<Void> confirmAccount(@RequestParam String token) {
        authService.confirmAccount(token);
       return ResponseEntity.noContent().build();
    }

}
