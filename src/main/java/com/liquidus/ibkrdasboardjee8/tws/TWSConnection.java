package com.liquidus.ibkrdasboardjee8.tws;

import com.ib.client.EClientSocket;
import com.ib.client.EReader;
import com.ib.client.EReaderSignal;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Manages logic of connection to TWS
 */
@Named
@ApplicationScoped
public class TWSConnection {
    public static final int TWS_PORT = 7496;
    public static final int IB_GATEWAY_PORT = 4002;
    private static final String LOCALHOST = "127.0.0.1";

    private final EWrapperImpl wrapper = new EWrapperImpl();
    Logger logger = Logger.getLogger(TWSConnection.class.getName());

    public TWSConnection() {
    }

    public void run(EClientSocket clientSocket, EReaderSignal readerSignal) throws InterruptedException {
        logger.info("Connecting to IB API socket...");

        // establishing connection to Interactive Broker's TWS or Gateway
        establishConnectionToIB(clientSocket);

        final EReader reader = new EReader(clientSocket, readerSignal);

        reader.start();
        new Thread(() -> {
            while (clientSocket.isConnected()) {
                readerSignal.waitForSignal();
                try {
                    reader.processMsgs();
                } catch (IOException e) {
                    logger.warning("Failed processing messages from EReader: " + e.getMessage());
                }
            }
        }).start();

        Thread.sleep(1000);

//        Thread.sleep(1000);  // sleep for 1s before disconnecting
//        clientSocket.eDisconnect();
    }

    private void establishConnectionToIB(EClientSocket clientSocket) {
        // trying to connect to TWS first
        clientSocket.eConnect(LOCALHOST, TWS_PORT, 3);
        if (!clientSocket.isConnected()) {
            logger.info("Failed to connect to TWS. Trying IB Gateway...");
            clientSocket.eConnect(LOCALHOST, IB_GATEWAY_PORT, 3);

            if (!clientSocket.isConnected()) {
                logger.warning("Failed to connect to IB Gateway and TWS. Review host, port or clientId values");
            } else {
                logger.info("Connected to IB Gateway...");
            }
        } else {
            logger.info("Connected to TWS...");
        }
    }
}
