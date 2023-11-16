package com.liquidus.ibkrdasboardjee8.tws;

import com.ib.client.EClientSocket;
import com.ib.client.EReaderSignal;
import com.liquidus.ibkrdasboardjee8.dao.PositionLocal;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.logging.Logger;

@Named
@SessionScoped
public class OrderDataRetrieval implements Serializable {
    private static final String ACCOUNT_CODE = "DU6742034";
    private static final Logger logger = Logger.getLogger(OrderDataRetrieval.class.getName());
    private final EWrapperImpl wrapper = new EWrapperImpl();
    private final EClientSocket clientSocket = wrapper.getClient();
    private final EReaderSignal readerSignal = wrapper.getSignal();
    @Inject
    PositionLocal positionBean;
    @Inject
    TWSConnection twsConnection;

    public OrderDataRetrieval() {
    }

    public void run() {
        try {
            twsConnection.run(clientSocket, readerSignal);
        } catch (InterruptedException e) {
            logger.severe("Couldn't establish connection to IB API: " + e.getMessage());
        }
    }

    public boolean isConnected() {
        return clientSocket.isConnected();
    }

    public void getPortfolioUpdates() {
        clientSocket.reqPositions();
        try {
            Thread.sleep(1000);
            // request account updates (from TWS -> Account -> Account Window)
            clientSocket.reqAccountUpdates(true, ACCOUNT_CODE);
            Thread.sleep(1000);

            // add positions to Position table
            logger.info("Get a list of all positions from the table Position: ");
            wrapper.getPositions().forEach(position -> positionBean.addPosition(position));
        } catch (InterruptedException e) {
            logger.warning("Connection was interrupted: " + e.getMessage());
        }
    }


    /**
     * Get Unrealized PnL from {@link EWrapperImpl#updateAccountValue(String, String, String, String)}
     *
     * @return portfolio unrealized PnL from the account updates
     */
    public double portfolioUnrealizedPnL() {
        return wrapper.getPortfolioUnrealizedPnL();
    }

    /**
     * Get Realized PnL from {@link EWrapperImpl#updateAccountValue(String, String, String, String)}
     *
     * @return portfolio realized PnL from the account updates
     */
    public double portfolioRealizedPnL() {
        return wrapper.getPortfolioRealizedPnL();
    }

    /**
     * Get portfolio Net Liquidation value from {@link com.ib.client.EClient#reqAccountSummary(int, String, String)}
     *
     * @return {@link EWrapperImpl#getPortfolioNetLiquidation()}: total cash value + stock value + options value + bond value
     */
    public double portfolioNetLiquidation() {
        return wrapper.getPortfolioNetLiquidation();
    }
}
