package com.liquidus.ibkrdasboardjee8.servlet;

import com.liquidus.ibkrdasboardjee8.dao.PositionDao;
import com.liquidus.ibkrdasboardjee8.dao.PositionLocal;
import com.liquidus.ibkrdasboardjee8.tws.OrderDataRetrieval;

import javax.enterprise.inject.spi.CDI;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

@WebServlet(name = "AppServlet", urlPatterns = "/app-servlet")
public class AppServlet extends HttpServlet {

    PositionLocal positionBean = CDI.current().select(PositionDao.class).get();
    private Logger logger = Logger.getLogger(AppServlet.class.getName());

    @Override
    public void init() throws ServletException {
        logger.info("Starting AppServlet...");
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        OrderDataRetrieval app = new OrderDataRetrieval();
        try {
            resp.getWriter().println("Connected to an IB API successfully.\n" +
                    "Data fetched accordingly...\n");

            positionBean.getAllPositions().forEach(position -> {
                try {
                    // print each position
                    resp.getWriter().println(position + "\n");
                } catch (IOException e) {
                    logger.warning("IOException: " + e.getMessage());
                }
            });
            app.run();
        } catch (InterruptedException e) {
            logger.warning("InterruptedException: " + e.getMessage());
        }
    }
}
