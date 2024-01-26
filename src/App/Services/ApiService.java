package App.Services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;


public class ApiService {
    private String cachedDroneDynamics = null;
    private static final String BASE_URL = "https://dronesim.facets-labs.com/api/";
    private static final String TOKEN = "Token " + System.getenv("API_TOKEN");


    public String getDroneDynamics() throws IOException, InterruptedException {
        if (cachedDroneDynamics == null){
            cachedDroneDynamics = getAllPages("dronedynamics/?format=json&limit=5000");
        }
        return cachedDroneDynamics;
    }

    public String getDroneDynamic(int id) throws IOException, InterruptedException {
        String endpoint = BASE_URL + "dronedynamics/" + id + "/?format=json";
        return ApiRequest(endpoint);
    }

    public String getDrones() throws IOException, InterruptedException {
        return getAllPages("drones/?format=json&limit=1000");
    }
    public String getDrone(int id) throws IOException, InterruptedException {
        String endpoint = BASE_URL + "drones/" + id + "/?format=json";
        return ApiRequest(endpoint);
    }

    public String getDroneTypes() throws IOException, InterruptedException {
        return getAllPages("dronetypes/?format=json&limit=1000");
    }
    public String getDroneType(String url) throws IOException{
        return ApiRequest(url);
    }

    private String getAllPages(String endpoint) throws IOException {
        StringBuilder allResponses = new StringBuilder();
        String nextPageUrl = BASE_URL + endpoint;

        while (nextPageUrl != null) {
            String response = ApiRequest(nextPageUrl);
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray results = jsonResponse.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                allResponses.append(results.getJSONObject(i).toString());
            }

            nextPageUrl = jsonResponse.optString("next", null);
        }

        return allResponses.toString();
    }


    private String ApiRequest(String fullUrl) throws IOException {
        URL url = new URL(fullUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", TOKEN);
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);

        try {
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                return response.toString();
            } else {
                throw new IOException("Received non-OK response: " + responseCode);
            }
        } finally {
            connection.disconnect();
        }
    }
}