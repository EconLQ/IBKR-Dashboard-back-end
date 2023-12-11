package com.liquidus.ibkrdasboardjee8.rest.dashboard.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

@Entity
@Table(name = "CONCENTRATION_SECTOR_ALLOCATION")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConcentrationSectorAllocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CONCENTRATION_SECTOR_ALLOCATION_ID")
    private int concentrationSectorAllocation;

    @Column(name = "SECTOR")
    private String sector;
    @Column(name = "LONG_PARSED_WEIGHT")
    private double longParsedWeight;
    @Column(name = "SHORT_PARSED_WEIGHT")
    private double shortParsedWeight;

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    @Override
    public String toString() {
        return "ConcentrationSectorAllocation{" +
                "sector='" + sector + '\'' +
                ", longParsedWeight=" + longParsedWeight +
                ", shortParsedWeight=" + shortParsedWeight +
                '}';
    }

    public double getLongParsedWeight() {
        return longParsedWeight;
    }

    public void setLongParsedWeight(double longParsedWeight) {
        this.longParsedWeight = longParsedWeight;
    }

    public double getShortParsedWeight() {
        return shortParsedWeight;
    }

    public void setShortParsedWeight(double shortParsedWeight) {
        this.shortParsedWeight = shortParsedWeight;
    }
}
