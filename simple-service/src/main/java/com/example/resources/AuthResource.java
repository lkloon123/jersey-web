package com.example.resources;

import com.example.database.JdbiInstance;
import com.example.exception.AuthenticationException;
import com.example.interfaces.Auth;
import com.example.model.User;
import com.example.services.JwtService;
import com.example.services.LogService;
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.*;

import org.glassfish.grizzly.http.server.Request;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.HashMap;
import java.util.Optional;

@Path("auth")
public class AuthResource {

    @Inject
    private Provider<Request> grizzlyRequest;

    @POST
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@FormParam("email") String email, @FormParam("password") String password) {
        //select user first
        Optional<User> user = JdbiInstance.getInstance().withHandle(handle -> {
            handle.registerRowMapper(ConstructorMapper.factory(User.class));
            return handle.select("SELECT * FROM user WHERE email = ?")
                    .bind(0, email)
                    .mapTo(User.class)
                    .findFirst();
        });

        if (user.isPresent() && user.get().comparePassword(password)) {
            //log for audit purpose
            LogService.logger.info(grizzlyRequest.get().getRemoteAddr() + " login with " + email + " success");

            //generate token
            String token = new JwtService().generateToken(user.get());
            return Response.status(Response.Status.OK).entity(new HashMap<String, String>() {{
                put("token", token);
            }}).build();
        }

        //log for audit purpose
        LogService.logger.info(grizzlyRequest.get().getRemoteAddr() + " login with " + email + " failed");

        throw new AuthenticationException("invalid email or password");
    }

    @GET
    @Auth
    @Path("me")
    @Produces(MediaType.APPLICATION_JSON)
    public Response me(@Context SecurityContext securityContext) {
        return Response.ok(securityContext.getUserPrincipal()).build();
    }
}
