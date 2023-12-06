package com.liquidus.ibkrdasboardjee8.rest.dashboard;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.core.Response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

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
        Response response = dashboardResource.keyStatisticsEndpoint();
        assertThat(response.getStatus(), is(equalTo(Response.Status.OK.getStatusCode())));
    }

    @Test
    void riskMeasuresBenchmarkComparisonEndpoint() {
        Response response = dashboardResource.riskMeasuresBenchmarkComparisonEndpoint();
        assertThat(response.getStatus(), is(equalTo(Response.Status.OK.getStatusCode())));
    }

    @Test
    void allocationByAssetClassEndpoint() {
        Response response = dashboardResource.allocationByAssetClassEndpoint();
        assertThat(response.getStatus(), is(equalTo(Response.Status.OK.getStatusCode())));
    }

    @Test
    void allocationPerformanceRegionEndpoint() {
        Response response = dashboardResource.allocationPerformanceRegionEndpoint();
        assertThat(response.getStatus(), is(equalTo(Response.Status.OK.getStatusCode())));
    }

    @Test
    void allocationPerformanceSectorEndpoint() {
        Response response = dashboardResource.allocationPerformanceSectorEndpoint();
        assertThat(response.getStatus(), is(equalTo(Response.Status.OK.getStatusCode())));
    }

    @Test
    void historicalPerformanceBenchCompEndpoint() {
        Response response = dashboardResource.historicalPerformanceBenchCompEndpoint();
        assertThat(response.getStatus(), is(equalTo(Response.Status.OK.getStatusCode())));
    }

    @Test
    void concentrationHoldingsEndpoint() {
        Response response = dashboardResource.concentrationHoldingsEndpoint();
        assertThat(response.getStatus(), is(equalTo(Response.Status.OK.getStatusCode())));
    }

    @Test
    void timePeriodBenchmarkComparisonEndpoint() {
        Response response = dashboardResource.timePeriodBenchmarkComparisonEndpoint();
        assertThat(response.getStatus(), is(equalTo(Response.Status.OK.getStatusCode())));
    }
}