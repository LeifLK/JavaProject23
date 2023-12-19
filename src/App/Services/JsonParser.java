package App.Services;

import App.DroneDynamicsRespones;
import App.DroneResponse;
import App.DroneTypeResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;


public class JsonParser {

    // Parse Method for each JSON-file
    //Deserialize JSON-String to class-Object
    public static DroneResponse parseDronesJson(String jsonString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(jsonString, DroneResponse.class);
    }
    public static DroneDynamicsRespones parseDroneDynamicsJson(String jsonString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(jsonString, DroneDynamicsRespones.class);
    }

    public static DroneTypeResponse parseDroneTypeJson(String jsonString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(jsonString, DroneTypeResponse.class);
    }

}
