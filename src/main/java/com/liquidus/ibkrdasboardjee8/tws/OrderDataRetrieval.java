package com.liquidus.ibkrdasboardjee8.tws;

import com.liquidus.ibkrdasboardjee8.dao.CustomContractLocal;
import com.liquidus.ibkrdasboardjee8.dao.PositionLocal;
import com.liquidus.ibkrdasboardjee8.rest.auth.enitity.User;
import com.liquidus.ibkrdasboardjee8.rest.auth.resources.LoginResource;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.logging.Logger;

@Named
@SessionScoped
public class OrderDataRetrieval implements Serializable {
    private static final Logger logger = Logger.getLogger(OrderDataRetrieval.class.getName());
    @Inject
    PositionLocal positionBean;
    @Inject
    CustomContractLocal contractBean;
    @Inject
    TWSConnection twsConnection;
    private String accountCode;

    public OrderDataRetrieval() {
    }

    public String getAccountCode() {
        return accountCode;
    }

    /**
     * Gets accountCode from the successfully logged user in {@link LoginResource#login(User)}
     *
     * @param accountCode is the account code from{@link com.ib.client.EClient#reqAccountUpdates(boolean, String)}
     */
    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public void run() {
        try {
            twsConnection.run();
        } catch (InterruptedException e) {
            logger.severe("Couldn't establish connection to IB API: " + e.getMessage());
        }
    }

    /**
     * Checks whether the connection is alive
     *
     * @return true if connection still alive
     */
    public boolean isConnected() {
        return twsConnection.isConnected();
    }

    /**
     * Disconnect from IB API
     */
    public void disconnectIbClient() {
        twsConnection.disconnect();
    }

    public void getPortfolioUpdates() {
        try {
            Thread.sleep(1000);
            // request account updates (from TWS -> Account -> Account Window)
            logger.warning("this.getAccountCode(): " + this.getAccountCode());
            twsConnection.getClientSocket().reqAccountUpdates(true, this.getAccountCode());
            Thread.sleep(1000);

            // add positions to Position table
            logger.info("Get a list of all positions from the table Position: ");
            twsConnection.getWrapper()
                    .getPositions()
                    .forEach(position -> positionBean.addPosition(position));

            // add contracts to CustomContracts table
            twsConnection.getWrapper()
                    .getContracts()
                    .forEach(contract -> contractBean.saveContract(contract));
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
        return twsConnection.getWrapper().getPortfolioUnrealizedPnL();
    }

    /**
     * Get Realized PnL from {@link EWrapperImpl#updateAccountValue(String, String, String, String)}
     *
     * @return portfolio realized PnL from the account updates
     */
    public double portfolioRealizedPnL() {
        return twsConnection.getWrapper().getPortfolioRealizedPnL();
    }

    /**
     * Get portfolio Net Liquidation value from {@link com.ib.client.EClient#reqAccountSummary(int, String, String)}
     *
     * @return {@link EWrapperImpl#getPortfolioNetLiquidation()}: total cash value + stock value + options value + bond value
     */
    public double portfolioNetLiquidation() {
        return twsConnection.getWrapper().getPortfolioNetLiquidation();
    }
}
