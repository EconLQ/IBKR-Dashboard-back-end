package com.liquidus.ibkrdasboardjee8.rest.dashboard.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

public class SaveDataToDatabase {
    private static String DB_URL;
    private static String DB_USERNAME;
    private static String DB_PASSWORD;
    private static Logger logger = Logger.getLogger(SaveDataToDatabase.class.getName());

    public SaveDataToDatabase() {
        Properties properties = new Properties();
        try {
            properties.load(SaveDataToDatabase.class.getClassLoader().getResourceAsStream("DBCredentials.properties"));

            // assign values from the file
            DB_URL = properties.getProperty("db.url");
            DB_USERNAME = properties.getProperty("db.username");
            DB_PASSWORD = properties.getProperty("db.password");
        } catch (IOException e) {
            logger.warning("Failed to read DB properties: " + e.getMessage());
        }
    }

    public void buildKeyStats(List<String> keyStats) {
        if (keyStats.isEmpty()) {
            logger.warning("Failed to write Key Statistics data as it's empty");
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            logger.info("Saving Key Statistics data...");
            String query = "insert into KEY_STATISTICS(BeginningNAV, EndingNAV, CumulativeReturn, `1MonthReturn`, `1MonthReturnDateRange`,`3MonthReturn`,`3MonthReturnDateRange`,BestReturn,BestReturnDate,WorstReturn,WorstReturnDate,DepositsWithdrawals) " +
                    "value (?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement statement = conn.prepareStatement(query);

            statement.setDouble(1, Double.parseDouble(keyStats.get(2)));
            statement.setDouble(2, Double.parseDouble(keyStats.get(3)));
            statement.setDouble(3, Double.parseDouble(keyStats.get(4)));
            statement.setDouble(4, Double.parseDouble(keyStats.get(5)));
            statement.setString(5, keyStats.get(6));
            statement.setDouble(6, Double.parseDouble(keyStats.get(7)));
            statement.setString(7, keyStats.get(8));
            statement.setDouble(8, Double.parseDouble(keyStats.get(9)));
            statement.setString(9, keyStats.get(10));
            statement.setDouble(10, Double.parseDouble(keyStats.get(11)));
            statement.setString(11, keyStats.get(12));
            statement.setDouble(12, Double.parseDouble(keyStats.get(13)));

            statement.executeUpdate();
        } catch (SQLException e) {
            logger.warning("Failed to save data to the database: " + e.getMessage());
        }
    }

    public void buildAllocationByAssetClass(List<String> allocByAssetClass) {
        if (allocByAssetClass.isEmpty()) {
            logger.warning("Failed to read [Allocation by Asset Class] data as it's empty");
            return;
        }
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            logger.info("Saving Allocation by Asset Class data... ");
            String query = "insert into ALLOCATION_BY_ASSET_CLASS(DATE, ETFS, STOCKS, CASH, NAV) " +
                    "value (?,?,?,?,?)";
            PreparedStatement statement = conn.prepareStatement(query);

            statement.setString(1, allocByAssetClass.get(2));
            statement.setDouble(2, Double.parseDouble(allocByAssetClass.get(3)));
            statement.setDouble(3, Double.parseDouble(allocByAssetClass.get(4)));
            statement.setDouble(4, Double.parseDouble(allocByAssetClass.get(5)));
            statement.setDouble(5, Double.parseDouble(allocByAssetClass.get(6)));

            statement.executeUpdate();
        } catch (SQLException e) {
            logger.warning("Failed to save data to the database: " + e.getMessage());
            return;
        }
    }

    public void buildHistPerfBenchComparison(List<String> histComparison) {
        if (histComparison.isEmpty()) {
            logger.warning("Failed to write Historical Performance Benchmark Comparison data as it's empty");
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            logger.info("Saving Historical Performance Benchmark Comparison... ");
            String query = "insert into HISTORICAL_PERFORMANCE_BENCHMARK_COMPARISON(MONTH, SPXTR, EFA, VT, ACCOUNT) " +
                    "value (?,?,?,?,?)";
            PreparedStatement statement = conn.prepareStatement(query);

            statement.setString(1, histComparison.get(2));
            statement.setDouble(2, parseNaNToZeros(histComparison, 4));
            statement.setDouble(3, parseNaNToZeros(histComparison, 6));
            statement.setDouble(4, parseNaNToZeros(histComparison, 8));
            statement.setDouble(5, parseNaNToZeros(histComparison, 10));

            statement.executeUpdate();
        } catch (SQLException e) {
            logger.warning("Failed to save data to the database: " + e.getMessage());
        }
    }

    public void buildConcentrationHoldings(List<String> holdings) {
        if (holdings.isEmpty()) {
            logger.warning("Failed to write Concentration Holdings data as it's empty");
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            logger.info("Saving Holdings data... ");
            String query = "insert into CONCENTRATION_HOLDINGS(SYMBOL, DESCRIPTION, SECTOR, LONG_VALUE, SHORT_VALUE, NET_VALUE) " +
                    "value (?,?,?,?,?,?)";
            PreparedStatement statement = conn.prepareStatement(query);

            statement.setString(1, holdings.get(3));
            statement.setString(2, holdings.get(4));
            statement.setString(3, holdings.get(5));
            statement.setDouble(4, Double.parseDouble(holdings.get(6)));
            statement.setDouble(5, Double.parseDouble(holdings.get(7)));
            statement.setDouble(6, Double.parseDouble(holdings.get(8)));

            statement.executeUpdate();
        } catch (SQLException e) {
            logger.warning("Failed to save data to the database: " + e.getMessage());
            return;
        }
    }


    public void buildAllocationPerformanceByRegion(List<String> allocByRegion) {
        if (allocByRegion.isEmpty()) {
            logger.warning("Failed to read [Allocation and Performance by Region] data as it's empty");
            return;
        }
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            logger.info("Saving Allocation and Performance by Region data... ");
            String query = "insert into ALLOCATION_PERFORMANCE_BY_REGION(region, ending_nav) " +
                    "value (?,?)";
            PreparedStatement statement = conn.prepareStatement(query);

            statement.setString(1, allocByRegion.get(2));
            statement.setDouble(2, Double.parseDouble(allocByRegion.get(3)));

            statement.executeUpdate();
        } catch (SQLException e) {
            logger.warning("Failed to save data to the database: " + e.getMessage());
            return;
        }
    }

    public void buildAllocationPerformanceBySector(List<String> allocByRegion) {
        if (allocByRegion.isEmpty()) {
            logger.warning("Failed to read [Allocation and Performance by Sector] data as it's empty");
            return;
        }
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            logger.info("Saving Allocation and Performance by Sector data... ");
            String query = "insert into ALLOCATION_PERFORMANCE_SECTOR(sector, ending_nav_usd, ending_nav_pct) " +
                    "value (?,?,?)";
            PreparedStatement statement = conn.prepareStatement(query);

            statement.setString(1, allocByRegion.get(2));
            statement.setDouble(2, Double.parseDouble(allocByRegion.get(3)));
            statement.setDouble(3, Double.parseDouble(allocByRegion.get(4)));

            statement.executeUpdate();
        } catch (SQLException e) {
            logger.warning("Failed to save data to the database: " + e.getMessage());
            return;
        }
    }

    public void buildTimePeriodBenchComp(List<String> benchComp) {
        if (benchComp.isEmpty()) {
            logger.warning("Failed to read [Time Period Benchmark Comparison] data as it's empty");
            return;
        }
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            logger.info("Saving Time Period Benchmark Comparison data... ");
            String query = "insert into TIME_PERIOD_BENCHMARK_COMPARISON(date, spxtr, efa, vt, account) " +
                    "value (?,?,?,?,?)";
            PreparedStatement statement = conn.prepareStatement(query);

            statement.setString(1, benchComp.get(2));
            statement.setDouble(2, Double.parseDouble(benchComp.get(4)));
            statement.setDouble(3, Double.parseDouble(benchComp.get(6)));
            statement.setDouble(4, Double.parseDouble(benchComp.get(8)));
            statement.setDouble(5, Double.parseDouble(benchComp.get(10)));

            statement.executeUpdate();
        } catch (SQLException e) {
            logger.warning("Failed to save data to the database: " + e.getMessage());
            return;
        }
    }

    public void buildRiskBenchmarkComparison(List<String> rickMeasuresBenchComp) {
        if (rickMeasuresBenchComp.isEmpty()) {
            logger.warning("Failed to read [Risk Measures Benchmark Comparison] data as it's empty");
            return;
        }
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            logger.info("Saving Risk Measures Benchmark Comparison data... ");
            String query = "insert into RISK_MEASURES_BENCHMARK_COMPARISON(risk_ratio_category, spxtr, efa, vt, account) " +
                    "value (?,?,?,?,?)";
            PreparedStatement statement = conn.prepareStatement(query);

            statement.setString(1, rickMeasuresBenchComp.get(2));

            // parse brackets inside the positive and negative periods values
            if (rickMeasuresBenchComp.get(2).equals("Positive Periods:") || rickMeasuresBenchComp.get(2).equals("Negative Periods:")) {
                statement.setDouble(2, Double.parseDouble(String.valueOf(rickMeasuresBenchComp.get(4).charAt(0))));
                statement.setDouble(3, Double.parseDouble(String.valueOf(rickMeasuresBenchComp.get(6).charAt(0))));
                statement.setDouble(4, Double.parseDouble(String.valueOf(rickMeasuresBenchComp.get(8).charAt(0))));
                statement.setDouble(5, Double.parseDouble(String.valueOf(rickMeasuresBenchComp.get(10).charAt(0))));
            } else {
                statement.setDouble(2, parseNaNToZeros(rickMeasuresBenchComp, 4));
                statement.setDouble(3, parseNaNToZeros(rickMeasuresBenchComp, 6));
                statement.setDouble(4, parseNaNToZeros(rickMeasuresBenchComp, 8));
                statement.setDouble(5, parseNaNToZeros(rickMeasuresBenchComp, 10));
            }

            statement.executeUpdate();
        } catch (SQLException e) {
            logger.warning("Failed to save data to the database: " + e.getMessage());
            return;
        }
    }

    private double parseNaNToZeros(List<String> list, int idx) {
        return Double.parseDouble(list.get(idx).equals("-") ? "0" : list.get(idx));
    }
}
