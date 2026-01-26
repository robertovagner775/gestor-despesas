package com.roberto.gestor_despesa.handler.exceptions;

public class DateInvalidException extends RuntimeException {
    public DateInvalidException(String message) {
        super(message);
    }
}
