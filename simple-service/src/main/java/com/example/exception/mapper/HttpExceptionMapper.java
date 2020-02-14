package com.example.exception.mapper;

import com.example.exception.HttpException;
import com.example.model.ApiError;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class HttpExceptionMapper implements ExceptionMapper<HttpException> {
    @Override
    public Response toResponse(HttpException e) {
        ApiError apiError = new ApiError();
        apiError.setStatus(e.getStatus().getStatusCode());
        apiError.setMessage(e.getMessage());

        return Response.status(e.getStatus())
                .entity(apiError)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
