import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class API {
    private static final String BASE_URL = "https://dronesim.facets-labs.com/api/";
    private static final String TOKEN = "Token 96abe845d26eafd5c6d920a152a52a5185b4bc24";


    public String getDroneDynamics() throws IOException {
        return ApiRequest("dronedynamics/?format=json");
    }

    public String getDroneDynamics(int id) throws IOException {
        String endpoint = "dronedynamics/" + id + "/?format=json";
        return ApiRequest(endpoint);
    }

    public String getDrones() throws IOException {
        return ApiRequest("drones/?format=json");
    }

    public String getDrones(int id) throws IOException {
        String endpoint = "drones/" + id + "/?format=json";
        return ApiRequest(endpoint);
    }

    public String getDroneTypes() throws IOException {
        return ApiRequest("dronetypes/?format=json");
    }

    public String getDroneTypes(int id) throws IOException {
        String endpoint = "dronetypes/" + id + "/?format=json";
        return ApiRequest(endpoint);
    }



    private String ApiRequest(String endpoint) throws IOException {
        URL url = new URL(BASE_URL + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", TOKEN);
        connection.setConnectTimeout(5000); // 5 seconds
        connection.setReadTimeout(5000); // 5 seconds

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
                // Handle non-200 responses
                throw new IOException("Received non-OK response: " + responseCode);
            }
        } finally {
            connection.disconnect();
        }
    }
}