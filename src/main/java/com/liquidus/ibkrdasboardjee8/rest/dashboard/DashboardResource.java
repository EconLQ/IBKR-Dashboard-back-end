package com.liquidus.ibkrdasboardjee8.rest.dashboard;

import com.liquidus.ibkrdasboardjee8.rest.dashboard.entity.KeyStatistics;
import com.liquidus.ibkrdasboardjee8.rest.dashboard.entity.RiskMeasuresBenchmarkComparison;

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
        }
        return Response.ok(riskMeasureBenchComp).build();
    }
}
