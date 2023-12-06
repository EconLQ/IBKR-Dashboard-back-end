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
    @Column(name = "RISK_RATIO_CATEGORY")
    private String riskRatioCategory;
    @Column(name = "SPXTR")
    private double spxtr;
    @Column(name = "EFA")
    private double efa;
    @Column(name = "VT")
    private double vt;
    @Column(name = "ACCOUNT")
    private double account;

    public String getRiskRatioCategory() {
        return riskRatioCategory;
    }

    public void setRiskRatioCategory(String riskRationCategory) {
        this.riskRatioCategory = riskRationCategory;
    }

    @Override
    public String toString() {
        return "RiskMeasuresBenchmarkComparison{" +
                "riskRationCategory='" + riskRatioCategory + '\'' +
                ", spxtr=" + spxtr +
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
