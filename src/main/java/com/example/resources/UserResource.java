package com.example.resources;

import com.example.database.JdbiInstance;
import com.example.exception.AuthorizationException;
import com.example.exception.HttpException;
import com.example.interfaces.Auth;
import com.example.model.User;
import com.example.services.LogService;
import org.glassfish.grizzly.http.server.Request;
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;
import java.util.Optional;

@Path("user")
public class UserResource {
    @Context
    private SecurityContext securityContext;

    @Inject
    private Provider<Request> grizzlyRequest;

    @GET
    @Auth
    @Produces(MediaType.APPLICATION_JSON)
    public Response view() {
        User currentUser = (User) securityContext.getUserPrincipal();

        List<User> user = JdbiInstance.getInstance().withHandle(handle -> {
            handle.registerRowMapper(ConstructorMapper.factory(User.class));

            //user only able to view themselves
            if (securityContext.isUserInRole("user")) {
                return handle.select("SELECT * FROM user WHERE id = ?")
                        .bind(0, currentUser.getId())
                        .mapTo(User.class)
                        .list();
            }

            //manager able to view their own department
            if (securityContext.isUserInRole("manager")) {
                return handle.select("SELECT * FROM user WHERE department = ? AND role != ?")
                        .bind(0, currentUser.getDepartment())
                        .bind(1, "admin")
                        .mapTo(User.class)
                        .list();
            }

            //admin can view everyone
            return handle.select("SELECT * FROM user")
                    .mapTo(User.class)
                    .list();
        });

        return Response.ok(user).build();
    }

    @GET
    @Path("{userId}")
    @Auth
    public Response view(@PathParam("userId") String userId) {
        User currentUser = (User) securityContext.getUserPrincipal();

        Optional<User> user = JdbiInstance.getInstance().withHandle(handle -> {
            handle.registerRowMapper(ConstructorMapper.factory(User.class));

            //user can only view themselves
            if (securityContext.isUserInRole("user")) {
                if (!userId.equalsIgnoreCase("" + currentUser.getId())) {
                    throw new AuthorizationException("you are not authorized");
                }

                return handle.select("SELECT * FROM user WHERE id = ?")
                        .bind(0, currentUser.getId())
                        .mapTo(User.class)
                        .findFirst();
            }

            //manager able to view their own department
            if (securityContext.isUserInRole("manager")) {
                return handle.select("SELECT * FROM user WHERE id = ? AND department = ? AND role != ?")
                        .bind(0, userId)
                        .bind(1, currentUser.getDepartment())
                        .bind(2, "admin")
                        .mapTo(User.class)
                        .findFirst();
            }

            //admin can view everyone
            return handle.select("SELECT * FROM user WHERE id = ?")
                    .bind(0, userId)
                    .mapTo(User.class)
                    .findFirst();
        });

        if (user.isPresent()) {
            return Response.ok(user.get()).build();
        }

        throw new HttpException(Response.Status.NOT_FOUND, "user not found");
    }

    @POST
    @Auth
    public Response create(
            @FormParam("email") String email,
            @FormParam("phone") String phone,
            @FormParam("password") String password,
            @FormParam("department") String department
    ) {
        //only admin can create user
        if (!securityContext.isUserInRole("admin")) {
            throw new AuthorizationException("you are not authorized");
        }

        User currentUser = (User) securityContext.getUserPrincipal();

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setPhone(phone);
        user.setDepartment(department);

        user.hashPassword();

        JdbiInstance.getInstance().useHandle(handle -> {
            handle.execute("INSERT INTO user(email, phone, password, department) VALUES (?, ?, ?, ?)",
                    user.getEmail(), user.getPhone(), user.getPassword(), user.getDepartment());

            //log for audit purpose
            LogService.logger.info(grizzlyRequest.get().getRemoteAddr() + " " + currentUser.getEmail() + " created " + user.toString());
        });

        return Response.status(Response.Status.CREATED).build();
    }

    @PATCH
    @Path("{userId}")
    @Auth
    public Response update(
            @PathParam("userId") String userId,
            @FormParam("email") String email,
            @FormParam("phone") String phone
    ) {
        User currentUser = (User) securityContext.getUserPrincipal();

        User user = new User();
        user.setEmail(email);
        user.setPhone(phone);

        if (securityContext.isUserInRole("admin")) {
            //admin can update everyone's phone and email
            JdbiInstance.getInstance().useHandle(handle -> {
                handle.execute("UPDATE user SET email = ?, phone = ? WHERE id = ?",
                        user.getEmail(), user.getPhone(), userId);

                //log for audit purpose
                LogService.logger.info(grizzlyRequest.get().getRemoteAddr() + " " + currentUser.getEmail() + " updated user id: " + userId + " with (" + user.getEmail() + ", " + user.getPhone() + ")");
            });

            return Response.noContent().build();
        }

        if (securityContext.isUserInRole("manager")) {
            //manager able to update user but not admin
            JdbiInstance.getInstance().useHandle(handle -> {
                handle.execute("UPDATE user SET phone = ? WHERE id = ? AND department = ? AND role != ?",
                        user.getPhone(), userId, currentUser.getDepartment(), "admin");

                //log for audit purpose
                LogService.logger.info(grizzlyRequest.get().getRemoteAddr() + " " + currentUser.getEmail() + " updated user id: " + userId + " with (" + user.getPhone() + ")");
            });

            return Response.noContent().build();
        }

        if (securityContext.isUserInRole("user")) {
            //user only allowed to update themselves
            if (!userId.equalsIgnoreCase("" + currentUser.getId())) {
                throw new AuthorizationException("you are not authorized");
            }

            JdbiInstance.getInstance().useHandle(handle -> {
                handle.execute("UPDATE user SET phone = ? WHERE id = ?", user.getPhone(), userId);

                //log for audit purpose
                LogService.logger.info(grizzlyRequest.get().getRemoteAddr() + " " + currentUser.getEmail() + " updated user id: " + userId + " with (" + user.getPhone() + ")");
            });

            return Response.noContent().build();
        }

        throw new AuthorizationException("you are not authorized");
    }

    @DELETE
    @Auth
    @Path("{userId}")
    public Response delete(@PathParam("userId") String userId) {
        User currentUser = (User) securityContext.getUserPrincipal();

        //only admin can delete user
        if (!securityContext.isUserInRole("admin")) {
            throw new AuthorizationException("you are not authorized");
        }

        if (userId.equalsIgnoreCase("" + currentUser.getId())) {
            throw new HttpException(Response.Status.BAD_REQUEST, "you cannot delete yourself");
        }

        JdbiInstance.getInstance().useHandle(handle -> {
            handle.execute("DELETE FROM user WHERE id = ?", userId);

            //log for audit purpose
            LogService.logger.info(grizzlyRequest.get().getRemoteAddr() + " " + currentUser.getEmail() + " deleted user id: " + userId);
        });

        return Response.noContent().build();
    }
}
