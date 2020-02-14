package com.example.exception;

import javax.ws.rs.core.Response;

public class AuthenticationException extends HttpException {
    public AuthenticationException() {
        super(Response.Status.UNAUTHORIZED);
    }

    public AuthenticationException(String message) {
        super(Response.Status.UNAUTHORIZED, message);
    }
}
