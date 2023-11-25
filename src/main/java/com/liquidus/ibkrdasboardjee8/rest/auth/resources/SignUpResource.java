package com.liquidus.ibkrdasboardjee8.rest.auth.resources;

import com.liquidus.ibkrdasboardjee8.rest.auth.UserBean;
import com.liquidus.ibkrdasboardjee8.rest.auth.enitity.User;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.logging.Logger;

@Path("/sign-up")
public class SignUpResource {
    Logger logger = Logger.getLogger(SignUpResource.class.getName());
    @Inject
    private UserBean userBean;

    @Transactional
    @POST
    @Consumes("application/json")
    public Response signUpUser(final User user) {
        if (user == null) {
            logger.warning("[signUpUser] Passed user is null");
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }

        // check if user exists and if not create
        if (userBean.findUserByUsername(user.getUsername())) {
            logger.warning("User with such username: [" + user.getUsername() + "] already exists");
            return Response.status(Response.Status.CONFLICT).build();
        }
        // create user
        userBean.addUser(user);

        // create URI with user's username and pass it to Response.created()
        UriBuilder uriBuilder = UriBuilder.fromPath("/sign-up/{username}")
                .resolveTemplate("username", user.getUsername());
        // return 201
        return Response.created(uriBuilder.build()).build();
    }
}
