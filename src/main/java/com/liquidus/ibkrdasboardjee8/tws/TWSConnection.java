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
    Logger logger = Logger.getLogger(TWSConnection.class.getName());
    private int currentOrderId = 3;
    private EWrapperImpl wrapper = new EWrapperImpl();
    private final EClientSocket clientSocket = wrapper.getClient();
    private final EReaderSignal readerSignal = wrapper.getSignal();

    public TWSConnection() {
    }

    public EWrapperImpl getWrapper() {
        return wrapper;
    }

    public EClientSocket getClientSocket() {
        return clientSocket;
    }

    public void run() throws InterruptedException {
        logger.info("Connecting to IB API socket...");

        // establishing connection to Interactive Broker's TWS or Gateway
        establishConnectionToIB(clientSocket);
        // increment currentOrderId for the next calls
        currentOrderId++;
        wrapper.nextValidId(currentOrderId);

        EReader reader = new EReader(clientSocket, readerSignal);

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
//        Thread.sleep(60_000);  // sleep for 1m before disconnecting
//        clientSocket.eDisconnect();
    }

    private void establishConnectionToIB(EClientSocket clientSocket) {
        // trying to connect to TWS first
        clientSocket.eConnect(LOCALHOST, TWS_PORT, currentOrderId);
        // increment currentOrderId for the next connection attempt
        wrapper.nextValidId(currentOrderId++);
        if (!clientSocket.isConnected()) {
            logger.info("Failed to connect to TWS. Trying IB Gateway...");
            clientSocket.eConnect(LOCALHOST, IB_GATEWAY_PORT, currentOrderId);

            if (!clientSocket.isConnected()) {
                logger.warning("Failed to connect to IB Gateway and TWS. Review host, port or clientId values");
            } else {
                logger.info("Connected to IB Gateway...");
            }
        } else {
            logger.info("Connected to TWS...");
        }
    }

    public boolean isConnected() {
        return clientSocket.isConnected();
    }

    public void disconnect() {
        clientSocket.eDisconnect();
    }
}
