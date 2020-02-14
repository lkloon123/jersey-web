package com.example.config;

import com.example.model.User;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

public class JwtSecurityContext implements SecurityContext {
    private User user;
    private boolean isSecure;

    public JwtSecurityContext(User user, boolean isSecure) {
        this.user = user;
        this.isSecure = isSecure;
    }

    @Override
    public Principal getUserPrincipal() {
        return this.user;
    }

    @Override
    public boolean isUserInRole(String s) {
        return this.user.getRole().equalsIgnoreCase(s);
    }

    @Override
    public boolean isSecure() {
        return isSecure;
    }

    @Override
    public String getAuthenticationScheme() {
        return "Bearer";
    }
}
