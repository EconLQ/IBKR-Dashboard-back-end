package com.liquidus.ibkrdasboardjee8.rest.dashboard;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.core.Response;
import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class DashboardResourceNoContentTest {
    @Mock
    DashboardDao dashboardDao;
    @InjectMocks
    DashboardResource dashboardResource;

    @Test
    void keyStatisticsEndpointNoContent() {
        given(dashboardDao.getKeyStatistics()).willReturn(new ArrayList<>());
        Response response = dashboardResource.keyStatisticsEndpoint();
        assertThat(response.getStatus(), is(equalTo(Response.Status.NO_CONTENT.getStatusCode())));
    }

    @Test
    void riskMeasuresBenchmarkComparisonEndpointNoContent() {
        given(dashboardDao.getRiskMeasuresBenchmarkComparison()).willReturn(new ArrayList<>());
        Response response = dashboardResource.riskMeasuresBenchmarkComparisonEndpoint();
        assertThat(response.getStatus(), is(equalTo(Response.Status.NO_CONTENT.getStatusCode())));
    }

    @Test
    void allocationPerformanceRegionEndpointNoContent() {
        given(dashboardDao.getAllocationPerformanceByRegion()).willReturn(new ArrayList<>());
        Response response = dashboardResource.allocationPerformanceRegionEndpoint();
        assertThat(response.getStatus(), is(equalTo(Response.Status.NO_CONTENT.getStatusCode())));
    }

    @Test
    void allocationByAssetClassEndpointNoContent() {
        given(dashboardDao.getAllocationByAssetClass()).willReturn(new ArrayList<>());
        Response response = dashboardResource.allocationByAssetClassEndpoint();
        assertThat(response.getStatus(), is(equalTo(Response.Status.NO_CONTENT.getStatusCode())));
    }

    @Test
    void allocationPerformanceSectorEndpointNoContent() {
        given(dashboardDao.getAllocationPerformanceSector()).willReturn(new ArrayList<>());
        Response response = dashboardResource.allocationPerformanceSectorEndpoint();
        assertThat(response.getStatus(), is(equalTo(Response.Status.NO_CONTENT.getStatusCode())));
    }

    @Test
    void historicalPerformanceBenchCompEndpointNoContent() {
        given(dashboardDao.getHistoricalPerformanceBenchmarkComparison()).willReturn(new ArrayList<>());
        Response response = dashboardResource.historicalPerformanceBenchCompEndpoint();
        assertThat(response.getStatus(), is(equalTo(Response.Status.NO_CONTENT.getStatusCode())));
    }

    @Test
    void concentrationHoldingsEndpointNoContent() {
        given(dashboardDao.getConcentrationHoldings()).willReturn(new ArrayList<>());
        Response response = dashboardResource.concentrationHoldingsEndpoint();
        assertThat(response.getStatus(), is(equalTo(Response.Status.NO_CONTENT.getStatusCode())));
    }

    @Test
    void concentrationSectorAllocationEndpointNoContent() {
        given(dashboardDao.getConcentrationSectorAllocation()).willReturn(new ArrayList<>());
        Response response = dashboardResource.concentrationSectorAllocationEndpoint();
        assertThat(response.getStatus(), is(equalTo(Response.Status.NO_CONTENT.getStatusCode())));
    }

    @Test
    void timePeriodBenchmarkComparisonEndpointNoContent() {
        given(dashboardDao.getTimePeriodBenchmarkComparison()).willReturn(new ArrayList<>());
        Response response = dashboardResource.timePeriodBenchmarkComparisonEndpoint();
        assertThat(response.getStatus(), is(equalTo(Response.Status.NO_CONTENT.getStatusCode())));
    }
}
