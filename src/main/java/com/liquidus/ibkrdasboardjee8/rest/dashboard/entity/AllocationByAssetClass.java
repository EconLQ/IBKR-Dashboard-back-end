package com.liquidus.ibkrdasboardjee8.rest.dashboard.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

@Entity
@Table(name = "ALLOCATION_BY_ASSET_CLASS")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AllocationByAssetClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ALLOCATION_BY_ASSET_CLASS_ID")
    private int allocationByAssetClassId;

    @Column(name = "DATE")
    private String date;
    @Column(name = "ETFS")
    private double etfs;
    @Column(name = "STOCKS")
    private double stocks;
    @Column(name = "CASH")
    private double cash;
    @Column(name = "NAV")
    private double nav;

    @Override
    public String toString() {
        return "AllocationByAssetClass{" +
                "date='" + date + '\'' +
                ", etfs=" + etfs +
                ", stocks=" + stocks +
                ", cash=" + cash +
                ", nav=" + nav +
                '}';
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getEtfs() {
        return etfs;
    }

    public void setEtfs(double etfs) {
        this.etfs = etfs;
    }

    public double getStocks() {
        return stocks;
    }

    public void setStocks(double stocks) {
        this.stocks = stocks;
    }

    public double getCash() {
        return cash;
    }

    public void setCash(double cash) {
        this.cash = cash;
    }

    public double getNav() {
        return nav;
    }

    public void setNav(double nav) {
        this.nav = nav;
    }
}
