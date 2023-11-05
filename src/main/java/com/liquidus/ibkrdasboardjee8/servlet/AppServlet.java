package com.liquidus.ibkrdasboardjee8.servlet;

import com.liquidus.ibkrdasboardjee8.dao.PositionLocal;
import com.liquidus.ibkrdasboardjee8.entity.Position;
import com.liquidus.ibkrdasboardjee8.tws.OrderDataRetrieval;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
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
        logger.info("Starting AppServlet...");
        try {
            app.run();
        } catch (InterruptedException e) {
            logger.warning("Failed to start application: " + e.getMessage());
        }
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            List<Position> positions = positionBean.getAllPositions();
            req.setAttribute("positions", positions);
            getServletContext().getRequestDispatcher("/positions.jsp").forward(req, resp);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
    }
}
