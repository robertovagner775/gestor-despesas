package com.roberto.gestor_despesa.dtos.response;

import org.springframework.validation.FieldError;

public record ErroCampo(String field, String message) {
    public ErroCampo(FieldError erro) {
        this(erro.getField(), erro.getDefaultMessage());
    }
}