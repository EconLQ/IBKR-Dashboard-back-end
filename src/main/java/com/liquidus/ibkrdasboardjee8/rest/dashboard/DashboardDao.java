package com.liquidus.ibkrdasboardjee8.rest.dashboard;


import com.liquidus.ibkrdasboardjee8.rest.dashboard.entity.*;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.logging.Logger;

@Singleton
public class DashboardDao {
    private Logger logger = Logger.getLogger(DashboardDao.class.getName());

    @PersistenceContext
    private EntityManager entityManager;

    public List<KeyStatistics> getKeyStatistics() {
        logger.info("Reading data from KeyStatistics table...");
        return entityManager.createQuery("select k from KeyStatistics k", KeyStatistics.class)
                .getResultList();
    }

    public List<AllocationByAssetClass> getAllocationByAssetClass() {
        logger.info("Reading data from AllocationByAssetClass table...");
        return entityManager.createQuery("select al from AllocationByAssetClass al", AllocationByAssetClass.class)
                .getResultList();
    }

    public List<AllocationPerformanceSector> getAllocationPerformanceSector() {
        logger.info("Reading data from AllocationPerformanceSector table...");
        return entityManager.createQuery("select aps from AllocationPerformanceSector aps", AllocationPerformanceSector.class)
                .getResultList();
    }

    public List<AllocationPerformanceByRegion> getAllocationPerformanceByRegion() {
        logger.info("Reading data from AllocationPerformanceByRegion table...");
        return entityManager.createQuery("select apr from AllocationPerformanceByRegion apr", AllocationPerformanceByRegion.class)
                .getResultList();
    }

    public List<ConcentrationHoldings> getConcentrationHoldings() {
        logger.info("Reading data from ConcentrationHoldings table...");
        return entityManager.createQuery("select ch from ConcentrationHoldings ch", ConcentrationHoldings.class)
                .getResultList();
    }

    public List<ConcentrationSectorAllocation> getConcentrationSectorAllocation() {
        logger.info("Reading data from ConcentrationSectorAllocation table...");
        return entityManager.createQuery("select csa from ConcentrationSectorAllocation csa", ConcentrationSectorAllocation.class)
                .getResultList();
    }

    public List<HistoricalPerformanceBenchmarkComparison> getHistoricalPerformanceBenchmarkComparison() {
        logger.info("Reading data from HistoricalPerformanceBenchmarkComparison table...");
        return entityManager.createQuery("select hpbc from HistoricalPerformanceBenchmarkComparison hpbc", HistoricalPerformanceBenchmarkComparison.class)
                .getResultList();
    }

    public List<RiskMeasuresBenchmarkComparison> getRiskMeasuresBenchmarkComparison() {
        logger.info("Reading data from RiskMeasuresBenchmarkComparison table...");
        return entityManager.createQuery("select rmbc from RiskMeasuresBenchmarkComparison rmbc", RiskMeasuresBenchmarkComparison.class)
                .getResultList();
    }

    public List<TimePeriodBenchmarkComparison> getTimePeriodBenchmarkComparison() {
        logger.info("Reading data from TimePeriodBenchmarkComparison table...");
        return entityManager.createQuery("select tpbc from TimePeriodBenchmarkComparison tpbc", TimePeriodBenchmarkComparison.class)
                .getResultList();
    }
}
