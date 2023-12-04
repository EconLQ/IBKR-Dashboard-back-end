package com.liquidus.ibkrdasboardjee8.rest.dashboard.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

@Entity
@Table(name = "HISTORICAL_PERFORMANCE_BENCHMARK_COMPARISON")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HistoricalPerformanceBenchmarkComparison {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HISTORICAL_PERFORMANCE_BENCHMARK_COMPARISON_ID")
    private int historicalPerformanceBenchmarkComparisonId;
    @Column(name = "MONTH")
    private String month;
    @Column(name = "SPXTR")
    private double spxtr;
    @Column(name = "EFA")
    private double efa;
    @Column(name = "VT")
    private double vt;
    @Column(name = "ACCOUNT")
    private double account;

    public HistoricalPerformanceBenchmarkComparison() {
    }

    @Override
    public String toString() {
        return "HistoricalPerformanceBenchmarkComparison{" +
                "month='" + month + '\'' +
                ", spxtr=" + spxtr +
                ", efa=" + efa +
                ", vt=" + vt +
                ", account=" + account +
                '}';
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public double getSpxtr() {
        return spxtr;
    }

    public void setSpxtr(double spxtr) {
        this.spxtr = spxtr;
    }

    public double getEfa() {
        return efa;
    }

    public void setEfa(double efa) {
        this.efa = efa;
    }

    public double getVt() {
        return vt;
    }

    public void setVt(double vt) {
        this.vt = vt;
    }

    public double getAccount() {
        return account;
    }

    public void setAccount(double account) {
        this.account = account;
    }
}
