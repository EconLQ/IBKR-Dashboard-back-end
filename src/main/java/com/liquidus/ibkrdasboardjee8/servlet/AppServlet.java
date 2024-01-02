package com.liquidus.ibkrdasboardjee8.servlet;

import com.liquidus.ibkrdasboardjee8.dao.PositionLocal;
import com.liquidus.ibkrdasboardjee8.tws.OrderDataRetrieval;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

@WebServlet(name = "AppServlet", urlPatterns = "/app-servlet")
public class AppServlet extends HttpServlet {

    @Inject
    PositionLocal positionBean;
    @Inject
    OrderDataRetrieval app;
    private Logger logger = Logger.getLogger(AppServlet.class.getName());

    @Override
    public void init() throws ServletException {
        super.init();
        logger.info("Starting AppServlet...");
        // run application - init EWrapperImpl, get EClient and EReader signal
        app.run();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();

        resp.setContentType("application/json");
        // Create a JSON object to return
        JsonObject json = null;
        if (app.isConnected()) {
            // request portfolio updates from the app
            app.getPortfolioUpdates();
            // build response JSON object
            json = Json.createObjectBuilder()
                    .add("status", "success")
                    .build();

            // send 200 on response
            resp.setStatus(HttpServletResponse.SC_OK);
        } else {
            // app was disconnected as user logged out, try to connect one more time...
            logger.warning("App is not connected to TWS/IB Gateway. Trying to establish connection...");

            json = Json.createObjectBuilder()
                    .add("status", "accepted")
                    .build();
            // send 202 to client
            resp.setStatus(HttpServletResponse.SC_ACCEPTED);

            // rerun the application to run update after connection
            app.run();
        }
        if (json != null) {
            out.print(json);
            out.flush();
        }
    }
}
