package App.Services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code JsonUtils} class provides utility methods for processing JSON data.
 * This class is designed to assist in handling common JSON operations more efficiently.
 *
 * @author Amin
 */
public class JsonUtils {

    private static final Logger LOGGER = LogManager.getLogger(JsonUtils.class);

    /**
     * Splits a single string containing multiple JSON objects into a list of JSON object strings.
     * This method assumes that each JSON object in the string is well formatted and is delimited by '{' and '}'.
     * It logs the start and completion of the process, including the number of JSON objects found.
     *
     * @param jsonString The string containing concatenated JSON objects.
     * @return A list of strings, where each string is a JSON object.
     */
    public static List<String> splitJsonObjects(String jsonString) {
        LOGGER.debug("Splitting JSON objects from string");
        List<String> jsonObjects = new ArrayList<>();
        int startIndex = 0;
        int endIndex;

        while ((endIndex = jsonString.indexOf('}', startIndex)) != -1) {
            String jsonObject = jsonString.substring(startIndex, endIndex + 1);
            jsonObjects.add(jsonObject);

            startIndex = endIndex + 1;
        }
        LOGGER.debug("Splitting completed. Total objects found: {}", jsonObjects.size());
        return jsonObjects;
    }
}