package com.example.exception.mapper;

import com.example.model.ApiError;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
public class ExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception e) {
        ApiError apiError = new ApiError();
        apiError.setStatus(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        apiError.setMessage(e.getMessage());

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(apiError)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
