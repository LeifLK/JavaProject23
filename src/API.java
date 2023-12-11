import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


    public class API {

        private static final String USER_AGENT = "";
        private static final String ENDPOINT_URL = "https://dronesim.facets-labs.com/api/drones/?format=json";
        private static final String TOKEN = "Token 96abe845d26eafd5c6d920a152a52a5185b4bc24";

        public static void main(String[] args) {
            System.out.println("Test started...");


            try {
              URL url = new URL(ENDPOINT_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Authorization", TOKEN);
                connection.setRequestMethod("GET");
                connection.setRequestProperty("User-Agent", USER_AGENT); //kann man vllt weg lassen
                int responseCode = connection.getResponseCode();

                System.out.println("Response Code " + responseCode);
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                System.out.println(response.toString()); // Process your responseß
            } catch (MalformedURLException e) {
                System.err.println("Malformed URL: " + e.getLocalizedMessage());
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("General IO Exception: " + e.getLocalizedMessage());
                e.printStackTrace();
            }
        }
}
