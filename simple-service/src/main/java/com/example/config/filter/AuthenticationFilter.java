package com.example.config.filter;

import com.example.config.JwtSecurityContext;
import com.example.exception.AuthenticationException;
import com.example.interfaces.Auth;
import com.example.model.User;
import com.example.services.JwtService;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Auth
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {
    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        String authorizationHeader = containerRequestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new AuthenticationException("Unauthenticated");
        }

        String token = authorizationHeader.substring(7);
        User user = new JwtService().parseToken(token);

        if (user == null) {
            throw new AuthenticationException("Unauthenticated");
        }

        boolean isSecure = containerRequestContext.getSecurityContext().isSecure();
        containerRequestContext.setSecurityContext(new JwtSecurityContext(user, isSecure));
    }
}
