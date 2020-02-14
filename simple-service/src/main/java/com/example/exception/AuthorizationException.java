package com.example.exception;

import javax.ws.rs.core.Response;

public class AuthorizationException extends HttpException {
    public AuthorizationException() {
        super(Response.Status.FORBIDDEN);
    }

    public AuthorizationException(String message) {
        super(Response.Status.FORBIDDEN, message);
    }
}
