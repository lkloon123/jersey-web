package com.example.exception;

import javax.ws.rs.core.Response.Status;

public class HttpException extends RuntimeException {
    protected Status status;

    public HttpException(Status status) {
        this(status, "");
    }

    public HttpException(Status status, String message) {
        super(message);
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }
}
