package com.liquidus.ibkrdasboardjee8.rest.dashboard;

import com.liquidus.ibkrdasboardjee8.rest.dashboard.entity.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;

/**
 * Basic unit tests on 200 status code
 */
@ExtendWith(MockitoExtension.class)
class DashboardResourceTest {

    @Mock
    DashboardDao dashboardDao;
    @InjectMocks
    DashboardResource dashboardResource;

    @Test
    void keyStatisticsEndpoint() {
        List<KeyStatistics> keyStatistics = new ArrayList<>();
        keyStatistics.add(new KeyStatistics());

        given(dashboardDao.getKeyStatistics()).willReturn(keyStatistics);

        Response response = dashboardResource.keyStatisticsEndpoint();
        assertThat(response.getStatus(), is(equalTo(Response.Status.OK.getStatusCode())));
        assertThat(response.getEntity(), is(equalTo(keyStatistics)));
    }


    @Test
    void riskMeasuresBenchmarkComparisonEndpoint() {
        List<RiskMeasuresBenchmarkComparison> riskMeasuresBenchmarkComparisons = new ArrayList<>();

        riskMeasuresBenchmarkComparisons.add(new RiskMeasuresBenchmarkComparison());

        given(dashboardDao.getRiskMeasuresBenchmarkComparison()).willReturn(riskMeasuresBenchmarkComparisons);

        Response response = dashboardResource.riskMeasuresBenchmarkComparisonEndpoint();
        assertThat(response.getStatus(), is(equalTo(Response.Status.OK.getStatusCode())));
        assertThat(response.getEntity(), is(equalTo(riskMeasuresBenchmarkComparisons)));

    }

    @Test
    void allocationPerformanceRegionEndpoint() {
        List<AllocationPerformanceByRegion> allocationPerformanceByRegions = new ArrayList<>();
        allocationPerformanceByRegions.add(new AllocationPerformanceByRegion());
        given(dashboardDao.getAllocationPerformanceByRegion()).willReturn(allocationPerformanceByRegions);

        Response response = dashboardResource.allocationPerformanceRegionEndpoint();
        assertThat(response.getStatus(), is(equalTo(Response.Status.OK.getStatusCode())));
        assertThat(response.getEntity(), is(equalTo(allocationPerformanceByRegions)));
    }

    @Test
    void allocationByAssetClassEndpoint() {
        List<AllocationByAssetClass> allocationByAssetClasses = new ArrayList<>();
        allocationByAssetClasses.add(new AllocationByAssetClass());

        given(dashboardDao.getAllocationByAssetClass()).willReturn(allocationByAssetClasses);

        Response response = dashboardResource.allocationByAssetClassEndpoint();
        assertThat(response.getStatus(), is(equalTo(Response.Status.OK.getStatusCode())));
        assertThat(response.getEntity(), is(equalTo(allocationByAssetClasses)));
    }

    @Test
    void allocationPerformanceSectorEndpoint() {
        List<AllocationPerformanceSector> allocationPerformanceSectors = new ArrayList<>();
        allocationPerformanceSectors.add(new AllocationPerformanceSector());

        given(dashboardDao.getAllocationPerformanceSector()).willReturn(allocationPerformanceSectors);

        Response response = dashboardResource.allocationPerformanceSectorEndpoint();
        assertThat(response.getStatus(), is(equalTo(Response.Status.OK.getStatusCode())));
        assertThat(response.getEntity(), is(equalTo(allocationPerformanceSectors)));
    }

    @Test
    void historicalPerformanceBenchCompEndpoint() {
        List<HistoricalPerformanceBenchmarkComparison> benchmarkComparisons = new ArrayList<>();
        benchmarkComparisons.add(new HistoricalPerformanceBenchmarkComparison());

        given(dashboardDao.getHistoricalPerformanceBenchmarkComparison()).willReturn(benchmarkComparisons);

        Response response = dashboardResource.historicalPerformanceBenchCompEndpoint();
        assertThat(response.getStatus(), is(equalTo(Response.Status.OK.getStatusCode())));
        assertThat(response.getEntity(), is(equalTo(benchmarkComparisons)));
    }

    @Test
    void concentrationHoldingsEndpoint() {
        List<ConcentrationHoldings> concentrationHoldings = new ArrayList<>();
        concentrationHoldings.add(new ConcentrationHoldings());
        given(dashboardDao.getConcentrationHoldings()).willReturn(concentrationHoldings);

        Response response = dashboardResource.concentrationHoldingsEndpoint();

        assertThat(response.getStatus(), is(equalTo(Response.Status.OK.getStatusCode())));
        assertThat(response.getEntity(), is(equalTo(concentrationHoldings)));
    }

    @Test
    void concentrationSectorAllocationEndpoint() {
        List<ConcentrationSectorAllocation> concentrationSectorAllocations = new ArrayList<>();
        concentrationSectorAllocations.add(new ConcentrationSectorAllocation());
        given(dashboardDao.getConcentrationSectorAllocation()).willReturn(concentrationSectorAllocations);

        Response response = dashboardResource.concentrationSectorAllocationEndpoint();

        assertThat(response.getStatus(), is(equalTo(Response.Status.OK.getStatusCode())));
        assertThat(response.getEntity(), is(equalTo(concentrationSectorAllocations)));
    }

    @Test
    void timePeriodBenchmarkComparisonEndpoint() {
        List<TimePeriodBenchmarkComparison> timePeriodBenchmarkComparisons = new ArrayList<>();
        timePeriodBenchmarkComparisons.add(new TimePeriodBenchmarkComparison());
        given(dashboardDao.getTimePeriodBenchmarkComparison()).willReturn(timePeriodBenchmarkComparisons);

        Response response = dashboardResource.timePeriodBenchmarkComparisonEndpoint();

        assertThat(response.getStatus(), is(equalTo(Response.Status.OK.getStatusCode())));
        assertThat(response.getEntity(), is(equalTo(timePeriodBenchmarkComparisons)));
    }
}