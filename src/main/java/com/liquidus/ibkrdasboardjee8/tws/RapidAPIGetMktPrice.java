package com.liquidus.ibkrdasboardjee8.tws;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * <p>Get a market price by making a GET request to the Rapid API endpoint.</p>
 * Doc: <a href="https://rapidapi.com/twelvedata/api/twelve-data1">GET Real-Time Price</a>
 */
public class RapidAPIGetMktPrice {
    Logger logger = Logger.getLogger(RapidAPIGetMktPrice.class.getName());
    private String apiKey;

    // loads API key from the RapidAPIKey.properties file
    public RapidAPIGetMktPrice() {
        try (InputStream input = RapidAPIGetMktPrice.class.getClassLoader()
                .getResourceAsStream("RapidAPIKey.properties")) {
            Properties properties = new Properties();
            if (input != null) {
                properties.load(input);
            } else {
                logger.warning("RapidAPIKey.properties file not found");
            }
            apiKey = properties.getProperty("api.key");
        } catch (IOException e) {
            logger.warning("Failed to read input: " + e.getMessage());
        }
    }

    /**
     * In case you have a custom Rapid API key
     *
     * @param apiKey your Rapid API
     */
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public double getMarketPrice(String ticker) {
        if (ticker.isEmpty()) {
            logger.warning("Passed ticket is probably null: " + ticker);
        }
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://twelve-data1.p.rapidapi.com/price?symbol="
                        + ticker + "&format=json&outputsize=30"))
                .header("X-RapidAPI-Key", apiKey)
                .header("X-RapidAPI-Host", "twelve-data1.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = null;
        try {
            response = HttpClient
                    .newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            logger.severe("Failed to get response data: " + e.getMessage());
        }

        assert response != null;

        // parse JSON string -> https://regex101.com/r/Ts4mXf/1
        List<String> list = List.of(response.body().split("[{\":}]"));

        // 5 is the index of the market price in the list of parsed JSON
        assert list.get(5) != null;

        return Double.parseDouble(list.get(5));
    }
}
