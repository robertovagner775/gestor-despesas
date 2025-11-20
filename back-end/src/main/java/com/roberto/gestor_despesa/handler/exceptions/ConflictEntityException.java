package com.roberto.gestor_despesa.handler.exceptions;

public class ConflictEntityException extends RuntimeException{

    private String message;

    public ConflictEntityException(String message) {
        super(message);
        this.message = message;
    }
}
