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
        // run application - init EWrapperImpl, get EClient and EReader signal
        app.run();
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            if (app.isConnected()) {
                // request portfolio updates from the app
                app.getPortfolioUpdates();
                // calculate portfolio PnL
                double portfolioUnrealizedPnL = app.portfolioUnrealizedPnL();
                double portfolioRealizedPnL = app.portfolioRealizedPnL();
                double portfolioNetLiquidation = app.portfolioNetLiquidation();

                List<Position> positions = positionBean.getAllPositions();
                req.setAttribute("positions", positions);
                req.setAttribute("portfolioUnrealizedPnL", portfolioUnrealizedPnL);
                req.setAttribute("portfolioRealizedPnL", portfolioRealizedPnL);
                req.setAttribute("portfolioNetLiquidation", portfolioNetLiquidation);

                resp.setHeader("Refresh", "15; URL=app-servlet"); // refresh page each 5s

                getServletContext().getRequestDispatcher("/positions.jsp").forward(req, resp);
            } else {
                // app was disconnected, try to connect one more time...
                logger.warning("App is not connected to TWS/IB Gateway. Trying to establish connection...");
                app.run();
            }
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
    }
}
