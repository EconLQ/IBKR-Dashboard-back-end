package com.liquidus.ibkrdasboardjee8.rest.dashboard.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

@Entity
@Table(name = "CONCENTRATION_HOLDINGS")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConcentrationHoldings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CONCENTRATION_HOLDINGS_ID")
    private int concentrationHoldings;

    @Column(name = "SYMBOL")
    private String symbol;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "SECTOR")
    private String sector;

    @Column(name = "LONG_VALUE")
    private double longValue;

    @Column(name = "SHORT_VALUE")
    private double shortValue;

    @Column(name = "NET_VALUE")
    private double netValue;


    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public double getLongValue() {
        return longValue;
    }

    public void setLongValue(double longValue) {
        this.longValue = longValue;
    }

    public double getShortValue() {
        return shortValue;
    }

    public void setShortValue(double shortValue) {
        this.shortValue = shortValue;
    }

    public double getNetValue() {
        return netValue;
    }

    public void setNetValue(double netValue) {
        this.netValue = netValue;
    }

    @Override
    public String toString() {
        return "ConcentrationHoldings{" +
                "symbol='" + symbol + '\'' +
                ", description='" + description + '\'' +
                ", sector='" + sector + '\'' +
                ", longValue=" + longValue +
                ", shortValue=" + shortValue +
                ", netValue=" + netValue +
                '}';
    }
}
