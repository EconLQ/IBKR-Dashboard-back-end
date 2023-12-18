package com.liquidus.ibkrdasboardjee8.rest.dashboard;

import com.liquidus.ibkrdasboardjee8.rest.dashboard.entity.*;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;

@Path("/dashboard")
public class DashboardResource {

    Logger logger = Logger.getLogger(DashboardResource.class.getName());
    @Inject
    DashboardDao dashboardDao;

    @GET
    @Path("/key-statistics")
    @Produces("application/json")
    public Response keyStatisticsEndpoint() {
        List<KeyStatistics> keyStatistics = dashboardDao.getKeyStatistics();
        if (keyStatistics.isEmpty()) {
            logger.info("Key Statistics table has no data");
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.ok(keyStatistics).build();
    }

    @GET
    @Path("/risk-measures-benchmark")
    @Produces("application/json")
    public Response riskMeasuresBenchmarkComparisonEndpoint() {
        List<RiskMeasuresBenchmarkComparison> riskMeasureBenchComp =
                dashboardDao.getRiskMeasuresBenchmarkComparison();
        if (riskMeasureBenchComp.isEmpty()) {
            logger.info("Risk Measures Benchmark Comparison table has no data");
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.ok(riskMeasureBenchComp).build();
    }

    @GET
    @Path("/allocation-asset-class")
    @Produces("application/json")
    public Response allocationByAssetClassEndpoint() {
        List<AllocationByAssetClass> allocationByAssetClass =
                dashboardDao.getAllocationByAssetClass();
        if (allocationByAssetClass.isEmpty()) {
            logger.info("Allocation By Asset Class table has no data");
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.ok(allocationByAssetClass).build();
    }

    @GET
    @Path("/allocation-performance-region")
    @Produces("application/json")
    public Response allocationPerformanceRegionEndpoint() {
        List<AllocationPerformanceByRegion> allocationPerformanceByRegion =
                dashboardDao.getAllocationPerformanceByRegion();
        if (allocationPerformanceByRegion.isEmpty()) {
            logger.info("Allocation Performance Region table has no data");
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.ok(allocationPerformanceByRegion).build();
    }

    @GET
    @Path("/allocation-performance-sector")
    @Produces("application/json")
    public Response allocationPerformanceSectorEndpoint() {
        List<AllocationPerformanceSector> allocationPerformanceSector =
                dashboardDao.getAllocationPerformanceSector();
        if (allocationPerformanceSector.isEmpty()) {
            logger.info("Allocation Performance Sector table has no data");
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.ok(allocationPerformanceSector).build();
    }

    @GET
    @Path("/hist-perf-bench-comp")
    @Produces("application/json")
    public Response historicalPerformanceBenchCompEndpoint() {
        List<HistoricalPerformanceBenchmarkComparison> comparison =
                dashboardDao.getHistoricalPerformanceBenchmarkComparison();
        if (comparison.isEmpty()) {
            logger.info("Historical Performance Benchmark Comparison has no data");
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.ok(comparison).build();
    }

    @GET
    @Path("/concentration-sectors")
    @Produces("application/json")
    public Response concentrationSectorAllocationEndpoint() {
        List<ConcentrationSectorAllocation> concentrationSectors =
                dashboardDao.getConcentrationSectorAllocation();
        if (concentrationSectors.isEmpty()) {
            logger.info("Concentration Sector Allocation table has no data");
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.ok(concentrationSectors).build();
    }

    @GET
    @Path("/concentration-holdings")
    @Produces("application/json")
    public Response concentrationHoldingsEndpoint() {
        List<ConcentrationHoldings> concentrationHoldings =
                dashboardDao.getConcentrationHoldings();
        if (concentrationHoldings.isEmpty()) {
            logger.info("Concentration Holdings table has no data");
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.ok(concentrationHoldings).build();
    }

    @GET
    @Path("/time-period-bench-comp")
    @Produces("application/json")
    public Response timePeriodBenchmarkComparisonEndpoint() {
        List<TimePeriodBenchmarkComparison> timePeriodBenchmarkComparison =
                dashboardDao.getTimePeriodBenchmarkComparison();
        if (timePeriodBenchmarkComparison.isEmpty()) {
            logger.info("Time Period Benchmark Comparison has no data");
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.ok(timePeriodBenchmarkComparison).build();
    }
}
