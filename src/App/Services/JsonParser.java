package App.Services;

import App.Model.DroneDynamics;
import App.Model.DroneType;
import App.Model.Drones;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class JsonParser {

    // Parse Method for each JSON-file
    //Deserialize JSON-String to class-Object
    public static Drones parseDronesJson(String jsonString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonString, Drones.class);
    }
    public static DroneDynamics parseDroneDynamicsJson(String jsonString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(jsonString, DroneDynamics.class);
    }

    public static DroneType parseDroneTypeJson(String jsonString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(jsonString, DroneType.class);
    }

    public static List<String> splitJsonString(String jsonString){
        return JsonUtils.splitJsonObjects(jsonString);
    }

}
