package com.liquidus.ibkrdasboardjee8.entity;


import javax.enterprise.inject.Default;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "Position")
public class Position {

    @Id
    @Column(name = "POSITION_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int positionId;

    @NotNull
    @Column(name = "CONTRACT_ID")
    private int contractId;


    @NotNull
    @Default()
    @Column(name = "LOCAL_DATE")
    private LocalDateTime date;

    @NotNull
    @Column(name = "TICKER")
    private String ticker;

    @Column(name = "POSITION")
    private double position;

    @Column(name = "UNREALIZED_PNL")
    private double unrealizedPnL;
    @Column(name = "REALIZED_PNL")
    private double realizedPnL;
    @Column(name = "AVERAGE_COST")
    private double averageCost;

    @Column(name = "LAST_MARKET_PRICE")
    private double lastMarketPrice;

    public Position() {
    }

    public Position(int contractId, LocalDateTime date, String ticker, double position, double unrealizedPnL, double realizedPnL, double averageCost, double lastMarketPrice) {
        super();
        this.contractId = contractId;
        this.date = date;
        this.ticker = ticker;
        this.position = position;
        this.unrealizedPnL = unrealizedPnL;
        this.realizedPnL = realizedPnL;
        this.averageCost = averageCost;
        this.lastMarketPrice = lastMarketPrice;
    }

    public int getContractId() {
        return contractId;
    }

    public void setContractId(int contractId) {
        this.contractId = contractId;
    }

    public double getPosition() {
        return position;
    }

    public void setPosition(double position) {
        this.position = position;
    }

    public double getUnrealizedPnL() {
        return unrealizedPnL;
    }

    public void setUnrealizedPnL(double unrealizedPnL) {
        this.unrealizedPnL = unrealizedPnL;
    }

    public double getRealizedPnL() {
        return realizedPnL;
    }

    public void setRealizedPnL(double realizedPnL) {
        this.realizedPnL = realizedPnL;
    }

    public double getAverageCost() {
        return averageCost;
    }

    public void setAverageCost(double averageCost) {
        this.averageCost = averageCost;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public double getLastMarketPrice() {
        return lastMarketPrice;
    }

    public void setLastMarketPrice(double lastMarketPrice) {
        this.lastMarketPrice = lastMarketPrice;
    }

    @Override
    public String toString() {
        return "Position{" +
                "contractId=" + contractId +
                ", date=" + date +
                ", ticker='" + ticker + '\'' +
                ", position=" + position +
                ", unrealizedPnL=" + unrealizedPnL +
                ", realizedPnL=" + realizedPnL +
                ", averageCost=" + averageCost +
                ", lastMarketPrice=" + lastMarketPrice +
                '}';
    }
}
