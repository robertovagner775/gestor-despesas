package com.roberto.gestor_despesa.handler;

import java.sql.Timestamp;

public class ErrorResponse {

    private Timestamp timeStamp;
    private String error;
    private Integer status;
    private String message;
    private String path;


    public ErrorResponse(Timestamp timeStamp, String error, String message, String path, Integer status) {
        this.timeStamp = timeStamp;
        this.error = error;
        this.message = message;
        this.path = path;
        this.status = status;
    }

    public Integer  getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
