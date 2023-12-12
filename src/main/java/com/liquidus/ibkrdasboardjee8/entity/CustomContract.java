package com.liquidus.ibkrdasboardjee8.entity;

import javax.persistence.*;

@Entity
@Table(name = "CUSTOM_CONTRACT")
public class CustomContract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CUSTOM_CONTRACT_ID")
    private int customContactId;
    @Column(name = "CONTRACT_ID")
    private int contractId;
    @Column(name = "SYMBOL")
    private String symbol;
    @Column(name = "SEC_TYPE")
    private String secType;
    @Column(name = "EXCHANGE")
    private String exchange;
    @Column(name = "CURRENCY")
    private String currency;

    public CustomContract() {
    }

    public CustomContract(int contractId, String symbol, String secType, String exchange, String currency) {
        this.contractId = contractId;
        this.symbol = symbol;
        this.secType = secType;
        this.exchange = exchange;
        this.currency = currency;
    }

    public int getCustomContactId() {
        return customContactId;
    }

    public void setCustomContactId(int customContact) {
        this.customContactId = customContact;
    }

    public int getContractId() {
        return contractId;
    }

    public void setContractId(int contractId) {
        this.contractId = contractId;
    }

    @Override
    public String toString() {
        return "CustomContract{" +
                "contractId=" + contractId +
                ", symbol='" + symbol + '\'' +
                ", secType='" + secType + '\'' +
                ", exchange='" + exchange + '\'' +
                ", currency='" + currency + '\'' +
                '}';
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSecType() {
        return secType;
    }

    public void setSecType(String secType) {
        this.secType = secType;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
