package App.Services;
import java.util.List;
import java.util.ArrayList;

public class JsonUtils {

    public static List<String> splitJsonObjects(String jsonString) {
        List<String> jsonObjects = new ArrayList<>();
        int startIndex = 0;
        int endIndex;

        while ((endIndex = jsonString.indexOf('}', startIndex)) != -1) {
            String jsonObject = jsonString.substring(startIndex, endIndex + 1);
            jsonObjects.add(jsonObject);

            startIndex = endIndex + 1;
        }
        return jsonObjects;
    }
}