package com.liquidus.ibkrdasboardjee8.tws;

import com.ib.client.EClientSocket;
import com.ib.client.EReader;
import com.ib.client.EReaderSignal;
import com.liquidus.ibkrdasboardjee8.dao.PositionDao;
import com.liquidus.ibkrdasboardjee8.dao.PositionLocal;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Named;
import java.io.IOException;
import java.util.logging.Logger;

@Named
@ApplicationScoped
public class OrderDataRetrieval {
    private static final String ACCOUNT_CODE = "DU6742034";
    private static Logger logger = Logger.getLogger(OrderDataRetrieval.class.getName());

    // TODO: investigate why it fails with @Inject
    PositionLocal positionBean = CDI.current().select(PositionDao.class).get();

    public OrderDataRetrieval() {
    }

    private static void pnl(EClientSocket client) throws InterruptedException {
        client.reqPnL(1, ACCOUNT_CODE, "");

        Thread.sleep(5000);

        client.cancelPnL(1);
    }

    public void run() throws InterruptedException {
        logger.info("Connecting to IB API socket...");
        EWrapperImpl wrapper = new EWrapperImpl();
        final EClientSocket clientSocket = wrapper.getClient();
        final EReaderSignal readerSignal = wrapper.getSignal();


//        clientSocket.eConnect("127.0.0.1", 7496, 3);  // TWS connection
        clientSocket.eConnect("127.0.0.1", 4002, 3);    // IB Gateway connection

        final EReader reader = new EReader(clientSocket, readerSignal);

        reader.start();
        new Thread(() -> {
            while (clientSocket.isConnected()) {
                readerSignal.waitForSignal();
                try {
                    reader.processMsgs();
                } catch (IOException e) {
                    System.out.println("Exception: " + e.getMessage());
                }
            }
        }).start();

        Thread.sleep(1000);

        clientSocket.reqPositions();
        Thread.sleep(3000); // sleep to ensure all the positions are added to the list (wrapper.getPositions())

        // request account updates (from TWS -> Account -> Account Window)
        clientSocket.reqAccountUpdates(true, ACCOUNT_CODE);
        Thread.sleep(3000);

        // get portfolio's Realized PnL, Unrealized PnL and Daily PnL
        pnl(clientSocket);
        Thread.sleep(3000);

        // add positions to Position table
        wrapper.getPositions().forEach(position -> positionBean.addPosition(position));

        System.out.println("Get a list of all positions from the table Position: ");
        positionBean.getAllPositions().forEach(System.out::println);

        Thread.sleep(1000);  // sleep for 1s before disconnecting
        clientSocket.eDisconnect();
    }
}
