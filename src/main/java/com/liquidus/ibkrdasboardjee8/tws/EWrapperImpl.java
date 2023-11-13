package com.liquidus.ibkrdasboardjee8.tws;


import com.ib.client.*;
import com.liquidus.ibkrdasboardjee8.entity.Position;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Implementation of {@link EWrapper} interface from <a href="https://interactivebrokers.github.io/tws-api/interfaceIBApi_1_1EWrapper.html">IBApi</a>
 */
@Named
@RequestScoped
public class EWrapperImpl implements EWrapper, Serializable {
    private final EReaderSignal readerSignal;
    private final EClientSocket clientSocket;
    protected int currentOrderId = -1;
    Logger logger = Logger.getLogger(EWrapperImpl.class.getName());
    private List<Position> positions = new ArrayList<>();
    // ![portfolio] PnL
    private double portfolioUnrealizedPnL;
    private double portfolioRealizedPnL;
    // ![portfolio] Net Liquidation
    private double portfolioNetLiquidation;
    // position's current market price
    private double currMarketPrice;

    public EWrapperImpl() {
        readerSignal = new EJavaSignal();
        clientSocket = new EClientSocket(this, readerSignal);
        currentOrderId++;
    }

    public double getPortfolioNetLiquidation() {
        return portfolioNetLiquidation;
    }

    public double getPortfolioUnrealizedPnL() {
        return portfolioUnrealizedPnL;
    }

    public double getPortfolioRealizedPnL() {
        return portfolioRealizedPnL;
    }

    public EClientSocket getClient() {
        return clientSocket;
    }

    public EReaderSignal getSignal() {
        return readerSignal;
    }

    public int getCurrentOrderId() {
        return currentOrderId;
    }


    @Override
    public void tickPrice(int tickerId, int field, double price, TickAttrib attribs) {
        logger.info("Tick Price: " + EWrapperMsgGenerator.tickPrice(tickerId, field, price, attribs));
    }


    @Override
    public void tickSize(int tickerId, int field, Decimal size) {
        // Uncomment if you need bid/ask tick data
//         logger.info("Tick Size: " + EWrapperMsgGenerator.tickSize(tickerId, field, size));
    }


    @Override
    public void tickOptionComputation(int tickerId, int field, int tickAttrib, double impliedVol, double delta, double optPrice,
                                      double pvDividend, double gamma, double vega, double theta, double undPrice) {
        logger.info("TickOptionComputation: " + EWrapperMsgGenerator.tickOptionComputation(tickerId, field, tickAttrib, impliedVol, delta, optPrice, pvDividend, gamma, vega, theta, undPrice));
    }


    @Override
    public void tickGeneric(int tickerId, int tickType, double value) {
        logger.info("Tick Generic: " + EWrapperMsgGenerator.tickGeneric(tickerId, tickType, value));
    }


    @Override
    public void tickString(int tickerId, int tickType, String value) {
        logger.info("Tick String: " + EWrapperMsgGenerator.tickString(tickerId, tickType, value));
    }


    @Override
    public void tickEFP(int tickerId, int tickType, double basisPoints, String formattedBasisPoints, double impliedFuture, int holdDays,
                        String futureLastTradeDate, double dividendImpact, double dividendsToLastTradeDate) {
        logger.info("TickEFP: " + EWrapperMsgGenerator.tickEFP(tickerId, tickType, basisPoints, formattedBasisPoints,
                impliedFuture, holdDays, futureLastTradeDate, dividendImpact, dividendsToLastTradeDate));
    }


    @Override
    public void orderStatus(int orderId, String status, Decimal filled, Decimal remaining, double avgFillPrice, int permId, int parentId,
                            double lastFillPrice, int clientId, String whyHeld, double mktCapPrice) {
        logger.info(EWrapperMsgGenerator.orderStatus(orderId, status, filled, remaining, avgFillPrice, permId, parentId, lastFillPrice, clientId, whyHeld, mktCapPrice));
    }


    @Override
    public void openOrder(int orderId, Contract contract, Order order, OrderState orderState) {
        logger.info(EWrapperMsgGenerator.openOrder(orderId, contract, order, orderState));
        logger.info("Order's vol: " + order.getVolatilityType());
    }


    @Override
    public void openOrderEnd() {
        logger.info("Open Order End: " + EWrapperMsgGenerator.openOrderEnd());
    }

    @Override
    public void updateAccountValue(String key, String value, String currency, String accountName) {
        if (key.equals("RealizedPnL")) {
            this.portfolioRealizedPnL = Double.parseDouble(value);
        } else if (key.equals("UnrealizedPnL")) {
            this.portfolioUnrealizedPnL = Double.parseDouble(value);
        }

        if (key.equals("NetLiquidation")) {
            this.portfolioNetLiquidation = Double.parseDouble(value);
        }
    }

    /**
     * <p>Calculates <b>Daily</b> Unrealized or Realized PnL for the portfolio</p>
     *
     * @param portfolioPnL RealizedPnL or UnrealizedPnL portfolio PnL value that we get from {@link EWrapper#updateAccountValue(String, String, String, String)}
     * @param pnlType      RealizedPnL or UnrealizedPnL. Added for pretty logging in the console and 0 check logic for RealizedPnL
     */
    private void calculatePnLHelper(double portfolioPnL, String pnlType) {
        RapidAPIGetMktPrice rapidAPIApp = new RapidAPIGetMktPrice();

        for (Position p : positions) {

            if (portfolioPnL == 0 && pnlType.equals("RealizedPnL")) {
                logger.info("RealizedPnL for " + p.getTicker() + " equals to 0");
                p.setRealizedPnL(0);
            } else if (portfolioPnL != 0) {
                // get current market price via Rapid API
                this.currMarketPrice = rapidAPIApp.getMarketPrice(p.getTicker());

                // save this position current market price
                p.setLastMarketPrice(this.currMarketPrice);

                logger.info("Last market price for: " + p.getTicker() + " is " + this.currMarketPrice);

                // Calculate position PnL
                double positionPnL = Math.abs(this.currMarketPrice - p.getAverageCost()) * p.getPosition();

                if (pnlType.equals("RealizedPnL")) {
                    p.setRealizedPnL(positionPnL);
                } else {
                    p.setUnrealizedPnL(positionPnL);
                }
                logger.info("[" + pnlType + "]" + " for the position: " + p.getTicker() + " is " + positionPnL);
            }
        }
    }


    @Override
    public void updatePortfolio(Contract contract, Decimal position, double marketPrice, double marketValue, double averageCost,
                                double unrealizedPNL, double realizedPNL, String accountName) {
        Position newPosition = new Position(
                contract.conid(),
                LocalDateTime.now(),
                contract.symbol(),
                position.value().doubleValue(),
                unrealizedPNL,
                realizedPNL,
                averageCost,
                marketPrice);

//         save positions to the list of EwrapperImpl instance
        positions.add(newPosition);
    }


    @Override
    public void updateAccountTime(String timeStamp) {
        logger.info(EWrapperMsgGenerator.updateAccountTime(timeStamp));
    }


    @Override
    public void accountDownloadEnd(String accountName) {
        logger.info(EWrapperMsgGenerator.accountDownloadEnd(accountName));
    }


    @Override
    public void nextValidId(int orderId) {
        logger.info(EWrapperMsgGenerator.nextValidId(orderId));
        currentOrderId = orderId;
    }


    @Override
    public void contractDetails(int reqId, ContractDetails contractDetails) {
        logger.info(EWrapperMsgGenerator.contractDetails(reqId, contractDetails));
    }


    @Override
    public void bondContractDetails(int reqId, ContractDetails contractDetails) {
        logger.info(EWrapperMsgGenerator.bondContractDetails(reqId, contractDetails));
    }


    @Override
    public void contractDetailsEnd(int reqId) {
        logger.info("Contract Details End: " + EWrapperMsgGenerator.contractDetailsEnd(reqId));
    }


    @Override
    public void execDetails(int reqId, Contract contract, Execution execution) {
        logger.info(EWrapperMsgGenerator.execDetails(reqId, contract, execution));
    }


    @Override
    public void execDetailsEnd(int reqId) {
        logger.info("Exec Details End: " + EWrapperMsgGenerator.execDetailsEnd(reqId));
    }


    @Override
    public void updateMktDepth(int tickerId, int position, int operation, int side, double price, Decimal size) {
        logger.info(EWrapperMsgGenerator.updateMktDepth(tickerId, position, operation, side, price, size));
    }


    //! [updatemktdepthl2]
    @Override
    public void updateMktDepthL2(int tickerId, int position, String marketMaker, int operation, int side, double price, Decimal size, boolean isSmartDepth) {
        logger.info(EWrapperMsgGenerator.updateMktDepthL2(tickerId, position, marketMaker, operation, side, price, size, isSmartDepth));
    }
    //! [updatemktdepthl2]


    @Override
    public void updateNewsBulletin(int msgId, int msgType, String message, String origExchange) {
        logger.info("News Bulletin: " + EWrapperMsgGenerator.updateNewsBulletin(msgId, msgType, message, origExchange));
    }


    @Override
    public void managedAccounts(String accountsList) {
        logger.info("Account list: " + accountsList);
    }


    @Override
    public void receiveFA(int faDataType, String xml) {
        logger.info("Receiving FA: " + faDataType + " - " + xml);
    }


    @Override
    public void historicalData(int reqId, Bar bar) {
        logger.info("HistoricalData:  " + EWrapperMsgGenerator.historicalData(reqId, bar.time(), bar.open(), bar.high(), bar.low(), bar.close(), bar.volume(), bar.count(), bar.wap()));
    }


    @Override
    public void historicalDataEnd(int reqId, String startDateStr, String endDateStr) {
        logger.info("HistoricalDataEnd. " + EWrapperMsgGenerator.historicalDataEnd(reqId, startDateStr, endDateStr));
    }


    @Override
    public void scannerParameters(String xml) {
        logger.info("ScannerParameters. " + xml + "\n");
    }


    @Override
    public void scannerData(int reqId, int rank, ContractDetails contractDetails, String distance, String benchmark, String projection, String legsStr) {
        logger.info("ScannerData: " + EWrapperMsgGenerator.scannerData(reqId, rank, contractDetails, distance, benchmark, projection, legsStr));
    }


    @Override
    public void scannerDataEnd(int reqId) {
        logger.info("ScannerDataEnd: " + EWrapperMsgGenerator.scannerDataEnd(reqId));
    }


    @Override
    public void realtimeBar(int reqId, long time, double open, double high, double low, double close, Decimal volume, Decimal wap, int count) {
        logger.info("RealTimeBar: " + EWrapperMsgGenerator.realtimeBar(reqId, time, open, high, low, close, volume, wap, count));
    }


    @Override
    public void currentTime(long time) {
        logger.info(EWrapperMsgGenerator.currentTime(time));
    }


    @Override
    public void fundamentalData(int reqId, String data) {
        logger.info("FundamentalData: " + EWrapperMsgGenerator.fundamentalData(reqId, data));
    }


    @Override
    public void deltaNeutralValidation(int reqId, DeltaNeutralContract deltaNeutralContract) {
        logger.info("Delta Neutral Validation: " + EWrapperMsgGenerator.deltaNeutralValidation(reqId, deltaNeutralContract));
    }


    @Override
    public void tickSnapshotEnd(int reqId) {
        logger.info("TickSnapshotEnd: " + EWrapperMsgGenerator.tickSnapshotEnd(reqId));
    }


    @Override
    public void marketDataType(int reqId, int marketDataType) {
        logger.info("MarketDataType: " + EWrapperMsgGenerator.marketDataType(reqId, marketDataType));
    }


    @Override
    public void commissionReport(CommissionReport commissionReport) {
        logger.info(EWrapperMsgGenerator.commissionReport(commissionReport));
    }


    @Override
    public void position(String account, Contract contract, Decimal pos, double avgCost) {
//        logger.info(EWrapperMsgGenerator.position(account, contract, pos, avgCost));
    }

    @Override
    public void positionEnd() {
//        logger.info("Position End: " + EWrapperMsgGenerator.positionEnd());
    }


    @Override
    public void accountSummary(int reqId, String account, String tag, String value, String currency) {
        logger.info(EWrapperMsgGenerator.accountSummary(reqId, account, tag, value, currency));
    }


    @Override
    public void accountSummaryEnd(int reqId) {
        logger.info("Account Summary End. Req Id: " + EWrapperMsgGenerator.accountSummaryEnd(reqId));
    }


    @Override
    public void verifyMessageAPI(String apiData) {
        logger.info("verifyMessageAPI");
    }

    @Override
    public void verifyCompleted(boolean isSuccessful, String errorText) {
        logger.info("verifyCompleted");
    }

    @Override
    public void verifyAndAuthMessageAPI(String apiData, String xyzChallenge) {
        logger.info("verifyAndAuthMessageAPI");
    }

    @Override
    public void verifyAndAuthCompleted(boolean isSuccessful, String errorText) {
        logger.info("verifyAndAuthCompleted");
    }


    @Override
    public void displayGroupList(int reqId, String groups) {
        logger.info("Display Group List. ReqId: " + reqId + ", Groups: " + groups + "\n");
    }


    @Override
    public void displayGroupUpdated(int reqId, String contractInfo) {
        logger.info("Display Group Updated. ReqId: " + reqId + ", Contract info: " + contractInfo + "\n");
    }


    @Override
    public void error(Exception e) {
        logger.info("Exception: " + e.getMessage());
    }

    @Override
    public void error(String str) {
        logger.info("Error: " + str);
    }


    @Override
    public void error(int id, int errorCode, String errorMsg, String advancedOrderRejectJson) {
        String str = "Error. Id: " + id + ", Code: " + errorCode + ", Msg: " + errorMsg;
        if (advancedOrderRejectJson != null) {
            str += (", AdvancedOrderRejectJson: " + advancedOrderRejectJson);
        }
        logger.info(str + "\n");
    }


    @Override
    public void connectionClosed() {
        logger.info("Connection closed");
    }


    @Override
    public void connectAck() {
        if (clientSocket.isAsyncEConnect()) {
            logger.info("Acknowledging connection");
            clientSocket.startAPI();
        }
    }


    @Override
    public void positionMulti(int reqId, String account, String modelCode, Contract contract, Decimal pos, double avgCost) {
        logger.info(EWrapperMsgGenerator.positionMulti(reqId, account, modelCode, contract, pos, avgCost));
    }


    @Override
    public void positionMultiEnd(int reqId) {
        logger.info("Position Multi End: " + EWrapperMsgGenerator.positionMultiEnd(reqId));
    }


    @Override
    public void accountUpdateMulti(int reqId, String account, String modelCode, String key, String value, String currency) {
        logger.info("Account Update Multi: " + EWrapperMsgGenerator.accountUpdateMulti(reqId, account, modelCode, key, value, currency));
    }


    @Override
    public void accountUpdateMultiEnd(int reqId) {
        logger.info("Account Update Multi End: " + EWrapperMsgGenerator.accountUpdateMultiEnd(reqId));
    }


    @Override
    public void securityDefinitionOptionalParameter(int reqId, String exchange, int underlyingConId, String tradingClass, String multiplier,
                                                    Set<String> expirations, Set<Double> strikes) {
        logger.info("Security Definition Optional Parameter: " + EWrapperMsgGenerator.securityDefinitionOptionalParameter(reqId, exchange, underlyingConId, tradingClass, multiplier, expirations, strikes));
    }


    @Override
    public void securityDefinitionOptionalParameterEnd(int reqId) {
        logger.info("Security Definition Optional Parameter End. Request Id: " + reqId);
    }


    @Override
    public void softDollarTiers(int reqId, SoftDollarTier[] tiers) {
        System.out.print(EWrapperMsgGenerator.softDollarTiers(tiers));
    }


    @Override
    public void familyCodes(FamilyCode[] familyCodes) {
        System.out.print(EWrapperMsgGenerator.familyCodes(familyCodes));
    }


    @Override
    public void symbolSamples(int reqId, ContractDescription[] contractDescriptions) {
        logger.info(EWrapperMsgGenerator.symbolSamples(reqId, contractDescriptions));
    }


    @Override
    public void mktDepthExchanges(DepthMktDataDescription[] depthMktDataDescriptions) {
        logger.info(EWrapperMsgGenerator.mktDepthExchanges(depthMktDataDescriptions));
    }


    @Override
    public void tickNews(int tickerId, long timeStamp, String providerCode, String articleId, String headline, String extraData) {
        logger.info(EWrapperMsgGenerator.tickNews(tickerId, timeStamp, providerCode, articleId, headline, extraData));
    }


    @Override
    public void smartComponents(int reqId, Map<Integer, Entry<String, Character>> theMap) {
        logger.info(EWrapperMsgGenerator.smartComponents(reqId, theMap));
    }


    @Override
    public void tickReqParams(int tickerId, double minTick, String bboExchange, int snapshotPermissions) {
        logger.info("Tick req params: " + EWrapperMsgGenerator.tickReqParams(tickerId, minTick, bboExchange, snapshotPermissions));
    }


    @Override
    public void newsProviders(NewsProvider[] newsProviders) {
        System.out.print(EWrapperMsgGenerator.newsProviders(newsProviders));
    }


    @Override
    public void newsArticle(int requestId, int articleType, String articleText) {
        logger.info(EWrapperMsgGenerator.newsArticle(requestId, articleType, articleText));
    }


    @Override
    public void historicalNews(int requestId, String time, String providerCode, String articleId, String headline) {
        logger.info(EWrapperMsgGenerator.historicalNews(requestId, time, providerCode, articleId, headline));
    }


    @Override
    public void historicalNewsEnd(int requestId, boolean hasMore) {
        logger.info(EWrapperMsgGenerator.historicalNewsEnd(requestId, hasMore));
    }


    @Override
    public void headTimestamp(int reqId, String headTimestamp) {
        logger.info(EWrapperMsgGenerator.headTimestamp(reqId, headTimestamp));
    }


    @Override
    public void histogramData(int reqId, List<HistogramEntry> items) {
        logger.info(EWrapperMsgGenerator.histogramData(reqId, items));
    }


    @Override
    public void historicalDataUpdate(int reqId, Bar bar) {
        logger.info("HistoricalDataUpdate. " + EWrapperMsgGenerator.historicalData(reqId, bar.time(), bar.open(), bar.high(), bar.low(), bar.close(), bar.volume(), bar.count(), bar.wap()));
    }


    @Override
    public void rerouteMktDataReq(int reqId, int conId, String exchange) {
        logger.info(EWrapperMsgGenerator.rerouteMktDataReq(reqId, conId, exchange));
    }


    @Override
    public void rerouteMktDepthReq(int reqId, int conId, String exchange) {
        logger.info(EWrapperMsgGenerator.rerouteMktDepthReq(reqId, conId, exchange));
    }


    @Override
    public void marketRule(int marketRuleId, PriceIncrement[] priceIncrements) {
        logger.info(EWrapperMsgGenerator.marketRule(marketRuleId, priceIncrements));
    }


    @Override
    public void pnl(int reqId, double dailyPnL, double unrealizedPnL, double realizedPnL) {
        logger.info(EWrapperMsgGenerator.pnl(reqId, dailyPnL, unrealizedPnL, realizedPnL));
    }


    @Override
    public void pnlSingle(int reqId, Decimal pos, double dailyPnL, double unrealizedPnL, double realizedPnL, double value) {
        // requesting updates
        clientSocket.reqPnLSingle(reqId, "DU6742034", "EUstocks", 268084);

//        EWrapperImpl eWrapper = new EWrapperImpl();
        logger.info("Creating new position...");
//        this.positions.add(new Position(reqId, pos, dailyPnL, unrealizedPnL, realizedPnL, value));
        logger.info(EWrapperMsgGenerator.pnlSingle(reqId, pos, dailyPnL, unrealizedPnL, realizedPnL, value));

        clientSocket.cancelPnLSingle(reqId);
    }


    @Override
    public void historicalTicks(int reqId, List<HistoricalTick> ticks, boolean done) {
        for (HistoricalTick tick : ticks) {
            logger.info(EWrapperMsgGenerator.historicalTick(reqId, tick.time(), tick.price(), tick.size()));
        }
    }


    @Override
    public void historicalTicksBidAsk(int reqId, List<HistoricalTickBidAsk> ticks, boolean done) {
        for (HistoricalTickBidAsk tick : ticks) {
            logger.info(EWrapperMsgGenerator.historicalTickBidAsk(reqId, tick.time(), tick.tickAttribBidAsk(), tick.priceBid(), tick.priceAsk(), tick.sizeBid(),
                    tick.sizeAsk()));
        }
    }


    @Override

    public void historicalTicksLast(int reqId, List<HistoricalTickLast> ticks, boolean done) {
        for (HistoricalTickLast tick : ticks) {
            logger.info(EWrapperMsgGenerator.historicalTickLast(reqId, tick.time(), tick.tickAttribLast(), tick.price(), tick.size(), tick.exchange(),
                    tick.specialConditions()));
        }
    }


    @Override
    public void tickByTickAllLast(int reqId, int tickType, long time, double price, Decimal size, TickAttribLast tickAttribLast,
                                  String exchange, String specialConditions) {
        logger.info(EWrapperMsgGenerator.tickByTickAllLast(reqId, tickType, time, price, size, tickAttribLast, exchange, specialConditions));
    }


    @Override
    public void tickByTickBidAsk(int reqId, long time, double bidPrice, double askPrice, Decimal bidSize, Decimal askSize,
                                 TickAttribBidAsk tickAttribBidAsk) {
        logger.info(EWrapperMsgGenerator.tickByTickBidAsk(reqId, time, bidPrice, askPrice, bidSize, askSize, tickAttribBidAsk));
    }


    @Override
    public void tickByTickMidPoint(int reqId, long time, double midPoint) {
        logger.info(EWrapperMsgGenerator.tickByTickMidPoint(reqId, time, midPoint));
    }


    @Override
    public void orderBound(long orderId, int apiClientId, int apiOrderId) {
        logger.info(EWrapperMsgGenerator.orderBound(orderId, apiClientId, apiOrderId));
    }


    @Override
    public void completedOrder(Contract contract, Order order, OrderState orderState) {
        logger.info(EWrapperMsgGenerator.completedOrder(contract, order, orderState));
    }


    @Override
    public void completedOrdersEnd() {
        logger.info(EWrapperMsgGenerator.completedOrdersEnd());
    }


    @Override
    public void replaceFAEnd(int reqId, String text) {
        logger.info(EWrapperMsgGenerator.replaceFAEnd(reqId, text));
    }


    @Override
    public void wshMetaData(int reqId, String dataJson) {
        logger.info(EWrapperMsgGenerator.wshMetaData(reqId, dataJson));
    }


    @Override
    public void wshEventData(int reqId, String dataJson) {
        logger.info(EWrapperMsgGenerator.wshEventData(reqId, dataJson));
    }


    @Override
    public void historicalSchedule(int reqId, String startDateTime, String endDateTime, String timeZone, List<HistoricalSession> sessions) {
        logger.info(EWrapperMsgGenerator.historicalSchedule(reqId, startDateTime, endDateTime, timeZone, sessions));
    }


    @Override
    public void userInfo(int reqId, String whiteBrandingId) {
        logger.info(EWrapperMsgGenerator.userInfo(reqId, whiteBrandingId));
    }

    public List<Position> getPositions() {
        return positions;
    }
}