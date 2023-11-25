package com.liquidus.ibkrdasboardjee8.rest.auth.resources;

import com.liquidus.ibkrdasboardjee8.tws.OrderDataRetrieval;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

@Path("/logout")
public class LogoutResource {

    Logger logger = Logger.getLogger(LogoutResource.class.getName());
    @Inject
    private OrderDataRetrieval app;

    @POST
    public Response logout(@Context HttpServletRequest request) {
        logger.info("Session invalidated. Disconnected from IB Client.");
        app.disconnectIbClient();   // disconnect from IB eClient
        request.getSession().invalidate();  // invalidate user's session
        return Response.ok().build();
    }
}
