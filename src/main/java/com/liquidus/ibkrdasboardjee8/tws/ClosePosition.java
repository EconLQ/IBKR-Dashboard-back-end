package com.liquidus.ibkrdasboardjee8.tws;

import com.ib.client.Contract;
import com.ib.client.Decimal;
import com.ib.client.Order;
import com.liquidus.ibkrdasboardjee8.dao.CustomContractLocal;
import com.liquidus.ibkrdasboardjee8.entity.CustomContract;
import com.liquidus.ibkrdasboardjee8.entity.Position;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.logging.Logger;

/**
 * The class is responsible for closing positions and is available only during request - {@link  RequestScoped}
 */
@Named
@RequestScoped
public class ClosePosition {
    private static final Logger logger = Logger.getLogger(ClosePosition.class.getName());
    @Inject
    TWSConnection twsConnection;

    @Inject
    CustomContractLocal contractDao;

    public ClosePosition() {
    }

    /**
     * Get {@link CustomContract} object from the request. Creates Order object and pass values to
     * {@link com.ib.client.EClientSocket#placeOrder(int, Contract, Order)} method
     *
     * @param position is {@link Position}
     */
    public void closePosition(Position position) {
        // tries to connect to another instance of TWS connection
        try {
            twsConnection.run();
        } catch (InterruptedException e) {
            logger.warning("Failed to connect to IB API..." + e.getMessage());
        }


        // creates order
        Order order = new Order();

        // negative position (position size less than 0) is short, positive is long
        // close short position - buy contracts, close long position - sell contracts
        order.action(position.getPosition() < 0 ? "BUY" : "SELL");
        order.orderType("MKT");
        order.totalQuantity(Decimal.get(position.getPosition()));

        // get contract by position's contractId
        CustomContract customContract = contractDao.findContractByPositionId(position.getContractId());

        Contract contract = new Contract();
        // set values to build IB's Contract object
        contract.symbol(customContract.getSymbol());
        contract.secType(customContract.getSecType());
        contract.currency(customContract.getCurrency());
        contract.exchange(customContract.getExchange() == null
                ? "SMART"
                : customContract.getExchange());

        int orderId = twsConnection.getWrapper().currentOrderId;

        // place order
        twsConnection.getClientSocket().placeOrder(orderId, contract, order);

        // disconnect after placing order
        twsConnection.getClientSocket().eDisconnect();
    }
}
