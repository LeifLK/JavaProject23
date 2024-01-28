package App.Services;

import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.logging.log4j.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * The {@code ApiService} provides services for fetching and handling data from the Drone API.
 * It includes methods for retrieving drone dynamics, drones, drone types, and specific drone type data.
 * It handles API requests and processes the responses.
 *
 * @author Leif
 */
public class ApiService {
    private static final Logger LOGGER = LogManager.getLogger(ApiService.class);
    private String cachedDroneDynamics = null;
    private static final String BASE_URL = "https://dronesim.facets-labs.com/api/";
    private static final String TOKEN = "Token " + System.getenv("API_TOKEN");

    /**
     * Retrieves drone dynamics data. If the data has not been previously fetched and cached, it fetches the data from the API.
     * If the data is already cached, it returns the cached data.
     *
     * @return A string containing the drone dynamics data in JSON format, or the cached data if it was previously fetched.
     */
    public String getDroneDynamics() {
        if (cachedDroneDynamics == null) {
            LOGGER.info("No Drone Dynamics, fetching data...");
            cachedDroneDynamics = getAllPages("dronedynamics/?format=json&limit=5000");
        } else {
            LOGGER.info("Drone Dynamics already fetched.");
        }
        return cachedDroneDynamics;
    }

    /**
     * Fetches and returns the drones data from the API. It retrieves all pages of drone data by making multiple API requests if necessary.
     *
     * @return A string of drones data in JSON format, from all pages.
     */
    public String getDrones() {
        LOGGER.info("Fetching drones data...");
        return getAllPages("drones/?format=json&limit=1000");
    }

    /**
     * Fetches and returns the drone types data from the API. It retrieves all pages of drone type data by making multiple API requests if necessary.
     *
     * @return A string of drone types data in JSON format, from all pages.
     */
    public String getDroneTypes() {
        LOGGER.info("Fetching drone types data...");
        return getAllPages("dronetypes/?format=json&limit=1000");
    }

    /**
     * Fetches and returns data for a specific drone type from the API. If the API request fails or returns an empty response, logs an error.
     *
     * @param url The URL from which to retrieve the drone type data.
     * @return A string representation of a specific drone type data in JSON format. If the API request fails, returns an empty string.
     */
    public String getDroneType(String url) {
        LOGGER.info("Fetching drone type data from URL: {}", url);
        String response = ApiRequest(url);
        if (response.isEmpty()) {
            LOGGER.error("Failed to fetch drone type data from URL: {}", url);
        }
        return response;
    }


    private String getAllPages(String endpoint) {
        StringBuilder allResponses = new StringBuilder();
        String nextPageUrl = BASE_URL + endpoint;

        while (nextPageUrl != null) {
            String response = ApiRequest(nextPageUrl);

            if (response.isEmpty()) {
                LOGGER.error("Failed to get response for the URL: {}", nextPageUrl);
                break;
            }

            JSONObject jsonResponse = new JSONObject(response);
            JSONArray results = jsonResponse.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                allResponses.append(results.getJSONObject(i).toString());
            }

            nextPageUrl = jsonResponse.optString("next", null);
        }
        return allResponses.toString();
    }

    private String ApiRequest(String fullUrl) {
        URL url = null;
        HttpURLConnection connection = null;
        StringBuilder response = new StringBuilder();

        try {
            url = new URL(fullUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", TOKEN);
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
            } else {
                LOGGER.error("Received non-OK response: {}", responseCode);
            }
            //Logger Test
            //throw new IOException("Moin Meister");
        } catch (IOException e) {
            LOGGER.error("IOException occurred during API request: {}", e.getMessage(), e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return response.toString();
    }
}