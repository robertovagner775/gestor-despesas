package com.roberto.gestor_despesa.handler.exceptions;

public class AuthException extends RuntimeException {

  private final String  MESSAGE_ERROR_EMAIL = "Email não cadastrado: ";

  private String message;
  private String email;

  @Override
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public AuthException(String email) {
      this.message = MESSAGE_ERROR_EMAIL + email;
      this.email = email;
    }
}
