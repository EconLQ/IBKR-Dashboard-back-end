package com.liquidus.ibkrdasboardjee8.rest.dashboard.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

@Entity
@Table(name = "RISK_MEASURES_BENCHMARK_COMPARISON")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RiskMeasuresBenchmarkComparison {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RISK_MEASURES_BENCHMARK_COMPARISON_ID")
    private int riskMeasuresBenchmarkComparisonId;

    @Column(name = "SPXTR")
    private double spxtr;
    @Column(name = "EFA")
    private double efa;
    @Column(name = "VT")
    private double vt;
    @Column(name = "ACCOUNT")
    private double account;

    @Override
    public String toString() {
        return "RiskMeasuresBenchmarkComparison{" +
                "spxtr=" + spxtr +
                ", efa=" + efa +
                ", vt=" + vt +
                ", account=" + account +
                '}';
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
