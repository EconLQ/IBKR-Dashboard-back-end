package com.liquidus.ibkrdasboardjee8.rest.dashboard.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

@Entity
@Table(name = "ALLOCATION_PERFORMANCE_SECTOR")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AllocationPerformanceSector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ALLOCATION_PERFORMANCE_SECTOR_ID")
    private int allocationPerformanceSector;

    @Column(name = "SECTOR")
    private String sector;

    @Column(name = "ENDING_NAV_USD")
    private double endingNavUsd;

    @Column(name = "ENDING_NAV_PCT")
    private double endingNavPct;

    public AllocationPerformanceSector() {

    }

    @Override
    public String toString() {
        return "AllocationPerformanceSector{" +
                "sector='" + sector + '\'' +
                ", endingNavUsd=" + endingNavUsd +
                ", endingNavPct=" + endingNavPct +
                '}';
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public double getEndingNavUsd() {
        return endingNavUsd;
    }

    public void setEndingNavUsd(double endingNavUsd) {
        this.endingNavUsd = endingNavUsd;
    }

    public double getEndingNavPct() {
        return endingNavPct;
    }

    public void setEndingNavPct(double endingNavPct) {
        this.endingNavPct = endingNavPct;
    }
}
