package com.liquidus.ibkrdasboardjee8.rest.dashboard.util;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Responsible for managing .csv file uploaded via {@link com.liquidus.ibkrdasboardjee8.rest.dashboard.FileUploadResource#uploadFile(MultipartFormDataInput)}
 */
public class CsvFileUtils {
    Logger logger = Logger.getLogger(CsvFileUtils.class.getName());
    private String fileLocation;

    public CsvFileUtils() {
    }

    /**
     * Creates reports directory in home and saves file mock file there as a placeholder
     *
     * @param fileName name of the file from the received request
     * @return location of a file
     */
    public String saveFileToHome(String fileName) {
        // skip quotes
        String fileNameWithoutQuotes = fileName.split("\"")[1];

        String directory = System.getProperty("user.home") + "/reports/";
        File dir = new File(directory);
        if (!dir.exists()) {
            dir.mkdir();
        }

        String uploadedFileLocation = directory + fileNameWithoutQuotes;

        // save file
        File fileObject = new File(uploadedFileLocation);
        if (fileObject.exists()) {
            fileObject.delete();
        }

        // saving file location
        this.fileLocation = uploadedFileLocation;
        return uploadedFileLocation;
    }

    /**
     * Save data from the {@link InputStream} to a file at uploadedFileLocation
     *
     * @param uploadedInputStream  file's input stream data
     * @param uploadedFileLocation location of a written file
     */
    public void saveToFile(InputStream uploadedInputStream, String uploadedFileLocation) {
        try {

            OutputStream out = new FileOutputStream(uploadedFileLocation);
            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = uploadedInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            logger.severe("File could not been found at: " + e.getMessage());
        } catch (IOException e) {
            logger.warning("Error to process reading from file: " + e.getMessage());
        }
        logger.info("File has been saved to: " + uploadedFileLocation);
    }

    public void parseCsvFile() {
        try (BufferedReader bf = new BufferedReader(new FileReader(this.fileLocation))) {
            String line = "";

            while ((line = bf.readLine()) != null) {
                List<String> row = new ArrayList<>(List.of(line.split("\n")));
                // parse each row and fill the tables with the content
                parseRowsData(row);
            }
        } catch (FileNotFoundException e) {
            logger.warning("No such file fount at: " + e.getMessage());
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.warning("Failed to read file: " + e.getMessage());
        }
    }

    /**
     * Parses uploaded report's row data. If report contains such row, then save data to the MySQL table
     *
     * @param row is a header from a report (Example: Key Statistics, Historical Performance Benchmark Comparison, etc)
     */
    private void parseRowsData(List<String> row) {
        // instantiate to read DB properties
        SaveDataToDatabase saveDataToDatabase = new SaveDataToDatabase();
        for (String r : row) {
            if (r.contains("Data")) {
                if (r.contains("Key Statistics")) {
                    saveDataToDatabase.buildKeyStats(List.of(r.split(",")));
                } else if (r.contains("Historical Performance Benchmark Comparison") && r.contains("20230")) {
                    saveDataToDatabase.buildHistPerfBenchComparison(List.of(r.split(",")));
                } else if (r.contains("Concentration") && r.contains("Holdings")) {
                    saveDataToDatabase.buildConcentrationHoldings(List.of(r.split(",")));
                } else if (r.contains("Allocation by Asset Class")) {
                    saveDataToDatabase.buildAllocationByAssetClass(List.of(r.split(",")));
                } else if (r.contains("Allocation and Performance by Region")) {
                    if (!r.contains("Total")) {
                        saveDataToDatabase.buildAllocationPerformanceByRegion(List.of(r.split(",")));
                    }
                } else if (r.contains("Allocation and Performance by Sector")) {
                    saveDataToDatabase.buildAllocationPerformanceBySector(List.of(r.split(",")));
                } else if (r.contains("Time Period Benchmark Comparison")) {
                    saveDataToDatabase.buildTimePeriodBenchComp(List.of(r.split(",")));
                } else if (r.contains("Risk Measures Benchmark Comparison")
                        && !r.contains("Peak-To-Valley:")
                        && !r.contains("Recovery:")) {
                    saveDataToDatabase.buildRiskBenchmarkComparison(List.of(r.split(",")));
                }
            }
        }
    }

    public String getFileLocation() {
        return fileLocation;
    }
}
