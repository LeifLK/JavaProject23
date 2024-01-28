package App.Services;

import App.Model.DroneDynamics;
import App.Model.DroneType;
import App.Model.Drones;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.logging.log4j.*;

import java.io.IOException;
import java.util.List;

/**
 * The {@code JsonParser} class provides utility methods for parsing JSON data into Java objects.
 * It utilizes the Jackson library for deserialization and handles potential errors during the parsing process.
 *
 * @author Amin
 */
public class JsonParser {

    private static final Logger LOGGER = LogManager.getLogger(JsonParser.class);

    /**
     * Parses a JSON string into a {@code Drones} object. If parsing fails, logs the error and returns null.
     *
     * @param jsonString The JSON string to be parsed.
     * @return The {@code Drones} object parsed from the JSON string, or null if parsing fails.
     */
    public static Drones parseDronesJson(String jsonString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
            return objectMapper.readValue(jsonString, Drones.class);
        } catch (IOException e) {
            LOGGER.error("Failed to parse Drones JSON: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Parses a JSON string into a {@code DroneDynamics} object. If parsing fails, logs the error and returns null.
     *
     * @param jsonString The JSON string to be parsed. This method expects the string to use "droneUrl" as a key instead of "drone".
     * @return The {@code DroneDynamics} object parsed from the JSON string, or null if parsing fails.
     */
    public static DroneDynamics parseDroneDynamicsJson(String jsonString) {
        try {
            jsonString = jsonString.replace("\"drone\"", "\"droneUrl\"");
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
            return objectMapper.readValue(jsonString, DroneDynamics.class);
        } catch (IOException e) {
            LOGGER.error("Failed to parse DroneDynamics JSON: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Parses a JSON string into a {@code DroneType} object. If parsing fails, logs the error and returns null.
     *
     * @param jsonString The JSON string to be parsed.
     * @return The {@code DroneType} object parsed from the JSON string, or null if parsing fails.
     */
    public static DroneType parseDroneTypeJson(String jsonString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonString, DroneType.class);
        } catch (IOException e) {
            LOGGER.error("Failed to parse DroneType JSON: {}", e.getMessage());
            return null;
        }
    }

    public static List<String> splitJsonString(String jsonString){
        return JsonUtils.splitJsonObjects(jsonString);
    }
}
