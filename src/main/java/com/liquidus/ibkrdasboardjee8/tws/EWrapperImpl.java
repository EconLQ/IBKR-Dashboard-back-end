package com.liquidus.ibkrdasboardjee8.tws;


import com.ib.client.*;
import com.liquidus.ibkrdasboardjee8.entity.Position;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Logger;


@Named
@RequestScoped
public class EWrapperImpl implements EWrapper, Serializable {
    // TODO: Test on Monday open whether you need this HashMap. List could be enough
    private final Map<Integer, Position> positionsPnLMap = new HashMap<>();
    private final EReaderSignal readerSignal;
    private final EClientSocket clientSocket;
    protected int currentOrderId = -1;
    Logger logger = Logger.getLogger(EWrapperImpl.class.getName());
    private List<Position> positions = new ArrayList<>();
    // ![portfolio] PnL
    private double portfolioUnrealizedPnL;
    private double portfolioRealizedPnL;
    // ![position] PnL
    private double positionRealizedPnL;
    private double positionUnrealizedPnL;
    private double currMarketPrice;

    public EWrapperImpl() {
        readerSignal = new EJavaSignal();
        clientSocket = new EClientSocket(this, readerSignal);
        currentOrderId++;
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

    //! [tickprice]
    @Override
    public void tickPrice(int tickerId, int field, double price, TickAttrib attribs) {
        System.out.println("Tick Price: " + EWrapperMsgGenerator.tickPrice(tickerId, field, price, attribs));
    }
    //! [tickprice]

    //! [ticksize]
    @Override
    public void tickSize(int tickerId, int field, Decimal size) {
        // TODO: uncomment if you need bid/ask tick data data
//        System.out.println("Tick Size: " + EWrapperMsgGenerator.tickSize(tickerId, field, size));
    }
    //! [ticksize]

    //! [tickoptioncomputation]
    @Override
    public void tickOptionComputation(int tickerId, int field, int tickAttrib, double impliedVol, double delta, double optPrice,
                                      double pvDividend, double gamma, double vega, double theta, double undPrice) {
        System.out.println("TickOptionComputation: " + EWrapperMsgGenerator.tickOptionComputation(tickerId, field, tickAttrib, impliedVol, delta, optPrice, pvDividend, gamma, vega, theta, undPrice));
    }
    //! [tickoptioncomputation]

    //! [tickgeneric]
    @Override
    public void tickGeneric(int tickerId, int tickType, double value) {
        System.out.println("Tick Generic: " + EWrapperMsgGenerator.tickGeneric(tickerId, tickType, value));
    }
    //! [tickgeneric]

    //! [tickstring]
    @Override
    public void tickString(int tickerId, int tickType, String value) {
        System.out.println("Tick String: " + EWrapperMsgGenerator.tickString(tickerId, tickType, value));
    }

    //! [tickstring]
    @Override
    public void tickEFP(int tickerId, int tickType, double basisPoints, String formattedBasisPoints, double impliedFuture, int holdDays,
                        String futureLastTradeDate, double dividendImpact, double dividendsToLastTradeDate) {
        System.out.println("TickEFP: " + EWrapperMsgGenerator.tickEFP(tickerId, tickType, basisPoints, formattedBasisPoints,
                impliedFuture, holdDays, futureLastTradeDate, dividendImpact, dividendsToLastTradeDate));
    }

    //! [orderstatus]
    @Override
    public void orderStatus(int orderId, String status, Decimal filled, Decimal remaining, double avgFillPrice, int permId, int parentId,
                            double lastFillPrice, int clientId, String whyHeld, double mktCapPrice) {
        System.out.println(EWrapperMsgGenerator.orderStatus(orderId, status, filled, remaining, avgFillPrice, permId, parentId, lastFillPrice, clientId, whyHeld, mktCapPrice));
    }
    //! [orderstatus]

    //! [openorder]
    @Override
    public void openOrder(int orderId, Contract contract, Order order, OrderState orderState) {
        System.out.println(EWrapperMsgGenerator.openOrder(orderId, contract, order, orderState));
        System.out.println("Order's vol: " + order.getVolatilityType());
    }
    //! [openorder]

    @Override
    public void openOrderEnd() {
        System.out.println("Open Order End: " + EWrapperMsgGenerator.openOrderEnd());
    }

    @Override
    public void updateAccountValue(String key, String value, String currency, String accountName) {
        // TODO: Test on Monday open how it calculates RealizedPnL
        if (key.equals("RealizedPnL")) {
            this.portfolioRealizedPnL = Double.parseDouble(value);
            calculatePnLHelper(portfolioRealizedPnL, "RealizedPnL");
        } else if (key.equals("UnrealizedPnL")) {
            this.portfolioUnrealizedPnL = Double.parseDouble(value);
            calculatePnLHelper(portfolioUnrealizedPnL, "UnrealizedPnL");
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

        for (Position p : positionsPnLMap.values()) {

            if (portfolioPnL == 0 && pnlType.equals("RealizedPnL")) {
                p.setRealizedPnL(0);
            } else if (portfolioPnL != 0) {
                // get current market price via Rapid API
                this.currMarketPrice = rapidAPIApp.getMarketPrice(p.getTicker());
                logger.info("[" + pnlType + "] Last market price for: " + p.getTicker() + " is " + this.currMarketPrice);

                // Calculate position PnL
                double positionPnL = Math.abs(this.currMarketPrice - p.getAverageCost()) * p.getPosition();

                if (pnlType.equals("RealizedPnL")) {
                    p.setRealizedPnL(positionPnL);
                } else {
                    p.setUnrealizedPnL(positionPnL);
                }
                logger.info(pnlType + " for the position: " + p.getTicker() + " is " + positionPnL);
            }
        }
    }
    //! [updateaccountvalue]

    //! [updateportfolio]
    @Override
    public void updatePortfolio(Contract contract, Decimal position, double marketPrice, double marketValue, double averageCost,
                                double unrealizedPNL, double realizedPNL, String accountName) {
        System.out.println(EWrapperMsgGenerator.updatePortfolio(contract, position, marketPrice, marketValue, averageCost, unrealizedPNL, realizedPNL, accountName));
    }
    //! [updateportfolio]

    //! [updateaccounttime]
    @Override
    public void updateAccountTime(String timeStamp) {
        System.out.println(EWrapperMsgGenerator.updateAccountTime(timeStamp));
    }
    //! [updateaccounttime]

    //! [accountdownloadend]
    @Override
    public void accountDownloadEnd(String accountName) {
        System.out.println(EWrapperMsgGenerator.accountDownloadEnd(accountName));
    }
    //! [accountdownloadend]

    //! [nextvalidid]
    @Override
    public void nextValidId(int orderId) {
        System.out.println(EWrapperMsgGenerator.nextValidId(orderId));
        currentOrderId = orderId;
    }
    //! [nextvalidid]

    //! [contractdetails]
    @Override
    public void contractDetails(int reqId, ContractDetails contractDetails) {
        System.out.println(EWrapperMsgGenerator.contractDetails(reqId, contractDetails));
    }

    //! [contractdetails]
    @Override
    public void bondContractDetails(int reqId, ContractDetails contractDetails) {
        System.out.println(EWrapperMsgGenerator.bondContractDetails(reqId, contractDetails));
    }

    //! [contractdetailsend]
    @Override
    public void contractDetailsEnd(int reqId) {
        System.out.println("Contract Details End: " + EWrapperMsgGenerator.contractDetailsEnd(reqId));
    }
    //! [contractdetailsend]

    //! [execdetails]
    @Override
    public void execDetails(int reqId, Contract contract, Execution execution) {
        System.out.println(EWrapperMsgGenerator.execDetails(reqId, contract, execution));
    }
    //! [execdetails]

    //! [execdetailsend]
    @Override
    public void execDetailsEnd(int reqId) {
        System.out.println("Exec Details End: " + EWrapperMsgGenerator.execDetailsEnd(reqId));
    }
    //! [execdetailsend]

    //! [updatemktdepth]
    @Override
    public void updateMktDepth(int tickerId, int position, int operation, int side, double price, Decimal size) {
        System.out.println(EWrapperMsgGenerator.updateMktDepth(tickerId, position, operation, side, price, size));
    }
    //! [updatemktdepth]

    //! [updatemktdepthl2]
    @Override
    public void updateMktDepthL2(int tickerId, int position, String marketMaker, int operation, int side, double price, Decimal size, boolean isSmartDepth) {
        System.out.println(EWrapperMsgGenerator.updateMktDepthL2(tickerId, position, marketMaker, operation, side, price, size, isSmartDepth));
    }
    //! [updatemktdepthl2]

    //! [updatenewsbulletin]
    @Override
    public void updateNewsBulletin(int msgId, int msgType, String message, String origExchange) {
        System.out.println("News Bulletin: " + EWrapperMsgGenerator.updateNewsBulletin(msgId, msgType, message, origExchange));
    }
    //! [updatenewsbulletin]

    //! [managedaccounts]
    @Override
    public void managedAccounts(String accountsList) {
        System.out.println("Account list: " + accountsList);
    }
    //! [managedaccounts]

    //! [receivefa]
    @Override
    public void receiveFA(int faDataType, String xml) {
        System.out.println("Receiving FA: " + faDataType + " - " + xml);
    }
    //! [receivefa]

    //! [historicaldata]
    @Override
    public void historicalData(int reqId, Bar bar) {
        System.out.println("HistoricalData:  " + EWrapperMsgGenerator.historicalData(reqId, bar.time(), bar.open(), bar.high(), bar.low(), bar.close(), bar.volume(), bar.count(), bar.wap()));
    }
    //! [historicaldata]

    //! [historicaldataend]
    @Override
    public void historicalDataEnd(int reqId, String startDateStr, String endDateStr) {
        System.out.println("HistoricalDataEnd. " + EWrapperMsgGenerator.historicalDataEnd(reqId, startDateStr, endDateStr));
    }
    //! [historicaldataend]


    //! [scannerparameters]
    @Override
    public void scannerParameters(String xml) {
        System.out.println("ScannerParameters. " + xml + "\n");
    }
    //! [scannerparameters]

    //! [scannerdata]
    @Override
    public void scannerData(int reqId, int rank, ContractDetails contractDetails, String distance, String benchmark, String projection, String legsStr) {
        System.out.println("ScannerData: " + EWrapperMsgGenerator.scannerData(reqId, rank, contractDetails, distance, benchmark, projection, legsStr));
    }
    //! [scannerdata]

    //! [scannerdataend]
    @Override
    public void scannerDataEnd(int reqId) {
        System.out.println("ScannerDataEnd: " + EWrapperMsgGenerator.scannerDataEnd(reqId));
    }
    //! [scannerdataend]

    //! [realtimebar]
    @Override
    public void realtimeBar(int reqId, long time, double open, double high, double low, double close, Decimal volume, Decimal wap, int count) {
        System.out.println("RealTimeBar: " + EWrapperMsgGenerator.realtimeBar(reqId, time, open, high, low, close, volume, wap, count));
    }

    //! [realtimebar]
    @Override
    public void currentTime(long time) {
        System.out.println(EWrapperMsgGenerator.currentTime(time));
    }

    //! [fundamentaldata]
    @Override
    public void fundamentalData(int reqId, String data) {
        System.out.println("FundamentalData: " + EWrapperMsgGenerator.fundamentalData(reqId, data));
    }

    //! [fundamentaldata]
    @Override
    public void deltaNeutralValidation(int reqId, DeltaNeutralContract deltaNeutralContract) {
        System.out.println("Delta Neutral Validation: " + EWrapperMsgGenerator.deltaNeutralValidation(reqId, deltaNeutralContract));
    }

    //! [ticksnapshotend]
    @Override
    public void tickSnapshotEnd(int reqId) {
        System.out.println("TickSnapshotEnd: " + EWrapperMsgGenerator.tickSnapshotEnd(reqId));
    }
    //! [ticksnapshotend]

    //! [marketdatatype]
    @Override
    public void marketDataType(int reqId, int marketDataType) {
        System.out.println("MarketDataType: " + EWrapperMsgGenerator.marketDataType(reqId, marketDataType));
    }
    //! [marketdatatype]

    //! [commissionreport]
    @Override
    public void commissionReport(CommissionReport commissionReport) {
        System.out.println(EWrapperMsgGenerator.commissionReport(commissionReport));
    }
    //! [commissionreport]

    @Override
    public void position(String account, Contract contract, Decimal pos, double avgCost) {
        try {
            getLastMarketPrice(contract);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Position position = new Position(
                contract.conid(),
                LocalDateTime.now(),
                contract.symbol(),
                pos.value().doubleValue(),
                this.positionUnrealizedPnL,
                this.positionRealizedPnL,
                avgCost,
                this.currMarketPrice);

        // save positions to the list of EwrapperImpl instance
        this.positions.add(position);
        // add position to Position table
//        positionBean.addPosition(position);
        // save position to the map to calculate it's realized and unrealized PnL
        this.positionsPnLMap.put(contract.conid(), position);
//        System.out.println(EWrapperMsgGenerator.position(account, contract, pos, avgCost));
    }

    /**
     * Subscribe to a <a href="https://interactivebrokers.github.io/tws-api/market_data_type.html">Delayed</a> market date type
     * to get last ask price
     *
     * @param contract position in the portfolio. {@link Contract}
     */
    private void getLastMarketPrice(Contract contract) throws InterruptedException {
        // TODO: figure out how to get delayed market price data
        logger.info("Request DELAYED_LAST value for: " + contract.symbol());
        // request Delayed market data
//        Thread.sleep(1000);
        clientSocket.reqMarketDataType(Types.MktDataType.Delayed.ordinal());
        clientSocket.reqMktData(getCurrentOrderId(), contract, "221", false, false, null);

//        Thread.sleep(1000);
//        clientSocket.cancelMktData(getCurrentOrderId());
    }

    @Override
    public void positionEnd() {
        System.out.println("Position End: " + EWrapperMsgGenerator.positionEnd());
    }
    //! [positionend]

    //! [accountsummary]
    @Override
    public void accountSummary(int reqId, String account, String tag, String value, String currency) {
        System.out.println(EWrapperMsgGenerator.accountSummary(reqId, account, tag, value, currency));
    }
    //! [accountsummary]

    //! [accountsummaryend]
    @Override
    public void accountSummaryEnd(int reqId) {
        System.out.println("Account Summary End. Req Id: " + EWrapperMsgGenerator.accountSummaryEnd(reqId));
    }

    //! [accountsummaryend]
    @Override
    public void verifyMessageAPI(String apiData) {
        System.out.println("verifyMessageAPI");
    }

    @Override
    public void verifyCompleted(boolean isSuccessful, String errorText) {
        System.out.println("verifyCompleted");
    }

    @Override
    public void verifyAndAuthMessageAPI(String apiData, String xyzChallenge) {
        System.out.println("verifyAndAuthMessageAPI");
    }

    @Override
    public void verifyAndAuthCompleted(boolean isSuccessful, String errorText) {
        System.out.println("verifyAndAuthCompleted");
    }

    //! [displaygrouplist]
    @Override
    public void displayGroupList(int reqId, String groups) {
        System.out.println("Display Group List. ReqId: " + reqId + ", Groups: " + groups + "\n");
    }
    //! [displaygrouplist]

    //! [displaygroupupdated]
    @Override
    public void displayGroupUpdated(int reqId, String contractInfo) {
        System.out.println("Display Group Updated. ReqId: " + reqId + ", Contract info: " + contractInfo + "\n");
    }

    //! [displaygroupupdated]
    @Override
    public void error(Exception e) {
        System.out.println("Exception: " + e.getMessage());
    }

    @Override
    public void error(String str) {
        System.out.println("Error: " + str);
    }

    //! [error]
    @Override
    public void error(int id, int errorCode, String errorMsg, String advancedOrderRejectJson) {
        String str = "Error. Id: " + id + ", Code: " + errorCode + ", Msg: " + errorMsg;
        if (advancedOrderRejectJson != null) {
            str += (", AdvancedOrderRejectJson: " + advancedOrderRejectJson);
        }
        System.out.println(str + "\n");
    }

    //! [error]
    @Override
    public void connectionClosed() {
        System.out.println("Connection closed");
    }

    //! [connectack]
    @Override
    public void connectAck() {
        if (clientSocket.isAsyncEConnect()) {
            System.out.println("Acknowledging connection");
            clientSocket.startAPI();
        }
    }
    //! [connectack]

    //! [positionmulti]
    @Override
    public void positionMulti(int reqId, String account, String modelCode, Contract contract, Decimal pos, double avgCost) {
        System.out.println(EWrapperMsgGenerator.positionMulti(reqId, account, modelCode, contract, pos, avgCost));
    }
    //! [positionmulti]

    //! [positionmultiend]
    @Override
    public void positionMultiEnd(int reqId) {
        System.out.println("Position Multi End: " + EWrapperMsgGenerator.positionMultiEnd(reqId));
    }
    //! [positionmultiend]

    //! [accountupdatemulti]
    @Override
    public void accountUpdateMulti(int reqId, String account, String modelCode, String key, String value, String currency) {
        System.out.println("Account Update Multi: " + EWrapperMsgGenerator.accountUpdateMulti(reqId, account, modelCode, key, value, currency));
    }
    //! [accountupdatemulti]

    //! [accountupdatemultiend]
    @Override
    public void accountUpdateMultiEnd(int reqId) {
        System.out.println("Account Update Multi End: " + EWrapperMsgGenerator.accountUpdateMultiEnd(reqId));
    }
    //! [accountupdatemultiend]

    //! [securityDefinitionOptionParameter]
    @Override
    public void securityDefinitionOptionalParameter(int reqId, String exchange, int underlyingConId, String tradingClass, String multiplier,
                                                    Set<String> expirations, Set<Double> strikes) {
        System.out.println("Security Definition Optional Parameter: " + EWrapperMsgGenerator.securityDefinitionOptionalParameter(reqId, exchange, underlyingConId, tradingClass, multiplier, expirations, strikes));
    }
    //! [securityDefinitionOptionParameter]

    //! [securityDefinitionOptionParameterEnd]
    @Override
    public void securityDefinitionOptionalParameterEnd(int reqId) {
        System.out.println("Security Definition Optional Parameter End. Request Id: " + reqId);
    }
    //! [securityDefinitionOptionParameterEnd]

    //! [softDollarTiers]
    @Override
    public void softDollarTiers(int reqId, SoftDollarTier[] tiers) {
        System.out.print(EWrapperMsgGenerator.softDollarTiers(tiers));
    }
    //! [softDollarTiers]

    //! [familyCodes]
    @Override
    public void familyCodes(FamilyCode[] familyCodes) {
        System.out.print(EWrapperMsgGenerator.familyCodes(familyCodes));
    }
    //! [familyCodes]

    //! [symbolSamples]
    @Override
    public void symbolSamples(int reqId, ContractDescription[] contractDescriptions) {
        System.out.println(EWrapperMsgGenerator.symbolSamples(reqId, contractDescriptions));
    }
    //! [symbolSamples]

    //! [mktDepthExchanges]
    @Override
    public void mktDepthExchanges(DepthMktDataDescription[] depthMktDataDescriptions) {
        System.out.println(EWrapperMsgGenerator.mktDepthExchanges(depthMktDataDescriptions));
    }
    //! [mktDepthExchanges]

    //! [tickNews]
    @Override
    public void tickNews(int tickerId, long timeStamp, String providerCode, String articleId, String headline, String extraData) {
        System.out.println(EWrapperMsgGenerator.tickNews(tickerId, timeStamp, providerCode, articleId, headline, extraData));
    }
    //! [tickNews]

    //! [smartcomponents]
    @Override
    public void smartComponents(int reqId, Map<Integer, Entry<String, Character>> theMap) {
        System.out.println(EWrapperMsgGenerator.smartComponents(reqId, theMap));
    }
    //! [smartcomponents]

    //! [tickReqParams]
    @Override
    public void tickReqParams(int tickerId, double minTick, String bboExchange, int snapshotPermissions) {
        System.out.println("Tick req params: " + EWrapperMsgGenerator.tickReqParams(tickerId, minTick, bboExchange, snapshotPermissions));
    }
    //! [tickReqParams]

    //! [newsProviders]
    @Override
    public void newsProviders(NewsProvider[] newsProviders) {
        System.out.print(EWrapperMsgGenerator.newsProviders(newsProviders));
    }
    //! [newsProviders]

    //! [newsArticle]
    @Override
    public void newsArticle(int requestId, int articleType, String articleText) {
        System.out.println(EWrapperMsgGenerator.newsArticle(requestId, articleType, articleText));
    }
    //! [newsArticle]

    //! [historicalNews]
    @Override
    public void historicalNews(int requestId, String time, String providerCode, String articleId, String headline) {
        System.out.println(EWrapperMsgGenerator.historicalNews(requestId, time, providerCode, articleId, headline));
    }
    //! [historicalNews]

    //! [historicalNewsEnd]
    @Override
    public void historicalNewsEnd(int requestId, boolean hasMore) {
        System.out.println(EWrapperMsgGenerator.historicalNewsEnd(requestId, hasMore));
    }
    //! [historicalNewsEnd]

    //! [headTimestamp]
    @Override
    public void headTimestamp(int reqId, String headTimestamp) {
        System.out.println(EWrapperMsgGenerator.headTimestamp(reqId, headTimestamp));
    }
    //! [headTimestamp]

    //! [histogramData]
    @Override
    public void histogramData(int reqId, List<HistogramEntry> items) {
        System.out.println(EWrapperMsgGenerator.histogramData(reqId, items));
    }
    //! [histogramData]

    //! [historicalDataUpdate]
    @Override
    public void historicalDataUpdate(int reqId, Bar bar) {
        System.out.println("HistoricalDataUpdate. " + EWrapperMsgGenerator.historicalData(reqId, bar.time(), bar.open(), bar.high(), bar.low(), bar.close(), bar.volume(), bar.count(), bar.wap()));
    }
    //! [historicalDataUpdate]

    //! [rerouteMktDataReq]
    @Override
    public void rerouteMktDataReq(int reqId, int conId, String exchange) {
        System.out.println(EWrapperMsgGenerator.rerouteMktDataReq(reqId, conId, exchange));
    }
    //! [rerouteMktDataReq]

    //! [rerouteMktDepthReq]
    @Override
    public void rerouteMktDepthReq(int reqId, int conId, String exchange) {
        System.out.println(EWrapperMsgGenerator.rerouteMktDepthReq(reqId, conId, exchange));
    }
    //! [rerouteMktDepthReq]

    //! [marketRule]
    @Override
    public void marketRule(int marketRuleId, PriceIncrement[] priceIncrements) {
        System.out.println(EWrapperMsgGenerator.marketRule(marketRuleId, priceIncrements));
    }
    //! [marketRule]

    //! [pnl]
    @Override
    public void pnl(int reqId, double dailyPnL, double unrealizedPnL, double realizedPnL) {
//        this.realizedPnL = realizedPnL;
//        this.unrealizedPnL = unrealizedPnL;
        System.out.println(EWrapperMsgGenerator.pnl(reqId, dailyPnL, unrealizedPnL, realizedPnL));
    }
    //! [pnl]

    //! [pnlsingle]
    @Override
    public void pnlSingle(int reqId, Decimal pos, double dailyPnL, double unrealizedPnL, double realizedPnL, double value) {
        // requesting updates
        clientSocket.reqPnLSingle(reqId, "DU6742034", "EUstocks", 268084);

//        EWrapperImpl eWrapper = new EWrapperImpl();
        System.out.println("Creating new position...");
//        this.positions.add(new Position(reqId, pos, dailyPnL, unrealizedPnL, realizedPnL, value));
        System.out.println(EWrapperMsgGenerator.pnlSingle(reqId, pos, dailyPnL, unrealizedPnL, realizedPnL, value));

        clientSocket.cancelPnLSingle(reqId);
    }
    //! [pnlsingle]

    //! [historicalticks]
    @Override
    public void historicalTicks(int reqId, List<HistoricalTick> ticks, boolean done) {
        for (HistoricalTick tick : ticks) {
            System.out.println(EWrapperMsgGenerator.historicalTick(reqId, tick.time(), tick.price(), tick.size()));
        }
    }
    //! [historicalticks]

    //! [historicalticksbidask]
    @Override
    public void historicalTicksBidAsk(int reqId, List<HistoricalTickBidAsk> ticks, boolean done) {
        for (HistoricalTickBidAsk tick : ticks) {
            System.out.println(EWrapperMsgGenerator.historicalTickBidAsk(reqId, tick.time(), tick.tickAttribBidAsk(), tick.priceBid(), tick.priceAsk(), tick.sizeBid(),
                    tick.sizeAsk()));
        }
    }
    //! [historicalticksbidask]

    @Override
    //! [historicaltickslast]
    public void historicalTicksLast(int reqId, List<HistoricalTickLast> ticks, boolean done) {
        for (HistoricalTickLast tick : ticks) {
            System.out.println(EWrapperMsgGenerator.historicalTickLast(reqId, tick.time(), tick.tickAttribLast(), tick.price(), tick.size(), tick.exchange(),
                    tick.specialConditions()));
        }
    }
    //! [historicaltickslast]

    //! [tickbytickalllast]
    @Override
    public void tickByTickAllLast(int reqId, int tickType, long time, double price, Decimal size, TickAttribLast tickAttribLast,
                                  String exchange, String specialConditions) {
        System.out.println(EWrapperMsgGenerator.tickByTickAllLast(reqId, tickType, time, price, size, tickAttribLast, exchange, specialConditions));
    }
    //! [tickbytickalllast]

    //! [tickbytickbidask]
    @Override
    public void tickByTickBidAsk(int reqId, long time, double bidPrice, double askPrice, Decimal bidSize, Decimal askSize,
                                 TickAttribBidAsk tickAttribBidAsk) {
        System.out.println(EWrapperMsgGenerator.tickByTickBidAsk(reqId, time, bidPrice, askPrice, bidSize, askSize, tickAttribBidAsk));
    }
    //! [tickbytickbidask]

    //! [tickbytickmidpoint]
    @Override
    public void tickByTickMidPoint(int reqId, long time, double midPoint) {
        System.out.println(EWrapperMsgGenerator.tickByTickMidPoint(reqId, time, midPoint));
    }
    //! [tickbytickmidpoint]

    //! [orderbound]
    @Override
    public void orderBound(long orderId, int apiClientId, int apiOrderId) {
        System.out.println(EWrapperMsgGenerator.orderBound(orderId, apiClientId, apiOrderId));
    }
    //! [orderbound]

    //! [completedorder]
    @Override
    public void completedOrder(Contract contract, Order order, OrderState orderState) {
        System.out.println(EWrapperMsgGenerator.completedOrder(contract, order, orderState));
    }
    //! [completedorder]

    //! [completedordersend]
    @Override
    public void completedOrdersEnd() {
        System.out.println(EWrapperMsgGenerator.completedOrdersEnd());
    }
    //! [completedordersend]

    //! [replacefaend]
    @Override
    public void replaceFAEnd(int reqId, String text) {
        System.out.println(EWrapperMsgGenerator.replaceFAEnd(reqId, text));
    }
    //! [replacefaend]

    //! [wshMetaData]
    @Override
    public void wshMetaData(int reqId, String dataJson) {
        System.out.println(EWrapperMsgGenerator.wshMetaData(reqId, dataJson));
    }
    //! [wshMetaData]

    //! [wshEventData]
    @Override
    public void wshEventData(int reqId, String dataJson) {
        System.out.println(EWrapperMsgGenerator.wshEventData(reqId, dataJson));
    }
    //! [wshEventData]

    //! [historicalSchedule]
    @Override
    public void historicalSchedule(int reqId, String startDateTime, String endDateTime, String timeZone, List<HistoricalSession> sessions) {
        System.out.println(EWrapperMsgGenerator.historicalSchedule(reqId, startDateTime, endDateTime, timeZone, sessions));
    }
    //! [historicalSchedule]

    //! [userInfo]
    @Override
    public void userInfo(int reqId, String whiteBrandingId) {
        System.out.println(EWrapperMsgGenerator.userInfo(reqId, whiteBrandingId));
    }

    public List<Position> getPositions() {
        return positions;
    }
}