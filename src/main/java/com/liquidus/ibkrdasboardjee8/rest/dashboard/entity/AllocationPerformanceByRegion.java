package com.liquidus.ibkrdasboardjee8.rest.dashboard.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

@Entity
@Table(name = "ALLOCATION_PERFORMANCE_BY_REGION")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AllocationPerformanceByRegion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ALLOCATION_PERFORMANCE_BY_REGION_ID")
    private int allocationPerformanceByRegionId;

    @Column(name = "REGION")
    private String region;
    @Column(name = "ENDING_NAV")
    private double endingNav;

    @Override
    public String toString() {
        return "AllocationPerformanceByRegion{" +
                "region='" + region + '\'' +
                ", endingNav='" + endingNav + '\'' +
                '}';
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public double getEndingNav() {
        return endingNav;
    }

    public void setEndingNav(double endingNav) {
        this.endingNav = endingNav;
    }
}
