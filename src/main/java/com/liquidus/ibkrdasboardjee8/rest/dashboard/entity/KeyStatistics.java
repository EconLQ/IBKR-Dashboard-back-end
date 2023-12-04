package com.liquidus.ibkrdasboardjee8.rest.dashboard.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

@Entity
@Table(name = "KEY_STATISTICS")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KeyStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "KEY_STATISTICS_ID")
    private int keyStatisticsId;

    @Column(name = "BeginningNAV")
    private double beginningNAV;
    @Column(name = "EndingNAV")
    private double endingNAV;
    @Column(name = "CumulativeReturn")
    private double cumulativeReturn;
    @Column(name = "1MonthReturn")
    private double oneMonthReturn;
    @Column(name = "1MonthReturnDateRange")
    private String oneMonthReturnDateRange;
    @Column(name = "3MonthReturn")
    private double threeMonthReturn;
    @Column(name = "3MonthReturnDateRange")
    private String threeMonthReturnDateRange;
    @Column(name = "BestReturn")
    private double bestReturn;
    @Column(name = "BestReturnDate")
    private String bestReturnDate;
    @Column(name = "WorstReturn")
    private double worstReturn;
    @Column(name = "WorstReturnDate")
    private String worstReturnDate;
    @Column(name = "DepositsWithdrawals")
    private double depositsWithdrawals;

    public KeyStatistics() {
    }

    @Override
    public String toString() {
        return "KeyStatistics{" +
                "beginningNAV=" + beginningNAV +
                ", endingNAV=" + endingNAV +
                ", cumulativeReturn=" + cumulativeReturn +
                ", oneMonthReturn=" + oneMonthReturn +
                ", oneMonthReturnDateRange='" + oneMonthReturnDateRange + '\'' +
                ", threeMonthReturn=" + threeMonthReturn +
                ", threeMonthReturnDateRange='" + threeMonthReturnDateRange + '\'' +
                ", bestReturn=" + bestReturn +
                ", bestReturnDate='" + bestReturnDate + '\'' +
                ", worstReturn=" + worstReturn +
                ", worstReturnDate='" + worstReturnDate + '\'' +
                ", depositsWithdrawals=" + depositsWithdrawals +
                '}';
    }

    public double getBeginningNAV() {
        return beginningNAV;
    }

    public void setBeginningNAV(double beginningNAV) {
        this.beginningNAV = beginningNAV;
    }

    public double getEndingNAV() {
        return endingNAV;
    }

    public void setEndingNAV(double endingNAV) {
        this.endingNAV = endingNAV;
    }

    public double getCumulativeReturn() {
        return cumulativeReturn;
    }

    public void setCumulativeReturn(double cumulativeReturn) {
        this.cumulativeReturn = cumulativeReturn;
    }

    public double getOneMonthReturn() {
        return oneMonthReturn;
    }

    public void setOneMonthReturn(double oneMonthReturn) {
        this.oneMonthReturn = oneMonthReturn;
    }

    public String getOneMonthReturnDateRange() {
        return oneMonthReturnDateRange;
    }

    public void setOneMonthReturnDateRange(String oneMonthReturnDateRange) {
        this.oneMonthReturnDateRange = oneMonthReturnDateRange;
    }

    public double getThreeMonthReturn() {
        return threeMonthReturn;
    }

    public void setThreeMonthReturn(double threeMonthReturn) {
        this.threeMonthReturn = threeMonthReturn;
    }

    public String getThreeMonthReturnDateRange() {
        return threeMonthReturnDateRange;
    }

    public void setThreeMonthReturnDateRange(String threeMonthReturnDateRange) {
        this.threeMonthReturnDateRange = threeMonthReturnDateRange;
    }

    public double getBestReturn() {
        return bestReturn;
    }

    public void setBestReturn(double bestReturn) {
        this.bestReturn = bestReturn;
    }

    public String getBestReturnDate() {
        return bestReturnDate;
    }

    public void setBestReturnDate(String bestReturnDate) {
        this.bestReturnDate = bestReturnDate;
    }

    public double getWorstReturn() {
        return worstReturn;
    }

    public void setWorstReturn(double worstReturn) {
        this.worstReturn = worstReturn;
    }

    public String getWorstReturnDate() {
        return worstReturnDate;
    }

    public void setWorstReturnDate(String worstReturnDate) {
        this.worstReturnDate = worstReturnDate;
    }

    public double getDepositsWithdrawals() {
        return depositsWithdrawals;
    }

    public void setDepositsWithdrawals(double depositsWithdrawals) {
        this.depositsWithdrawals = depositsWithdrawals;
    }
}
