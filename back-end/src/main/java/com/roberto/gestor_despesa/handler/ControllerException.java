package com.roberto.gestor_despesa.handler;

import com.roberto.gestor_despesa.dtos.response.ErroCampo;
import com.roberto.gestor_despesa.handler.exceptions.ConflictEntityException;
import com.roberto.gestor_despesa.handler.exceptions.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ControllerException extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> notFound404(NotFoundException notFoundException, HttpServletRequest request) {
        ErrorResponse err = new ErrorResponse(Timestamp.from(Instant.now()), "Not Found", notFoundException.getMessage(), request.getRequestURI(), 404);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    @ExceptionHandler(ConflictEntityException.class)
    public ResponseEntity<ErrorResponse> conflict409(ConflictEntityException conflictEntityException, HttpServletRequest request) {
        ErrorResponse err = new ErrorResponse(Timestamp.from(Instant.now()), "Conflict Entity Exception", conflictEntityException.getMessage(), request.getRequestURI(), 409);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(err);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var erro = ex.getFieldErrors();
        List<ErroCampo> list = erro.stream().map(ErroCampo::new).toList();

        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        HttpServletRequest servletRequest = servletWebRequest.getRequest();

        Map<String, Object> erroRetorno = new LinkedHashMap<>();
        erroRetorno.put("timestamp", Timestamp.from(Instant.now()));
        erroRetorno.put("status", 400);
        erroRetorno.put("message", "Method Argument Not Valid Exception");
        erroRetorno.put("path", servletRequest.getRequestURI());
        erroRetorno.put("errors", list);

        return ResponseEntity.badRequest().body(erroRetorno);
    }
}