package App;

import App.Model.Drones;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public class DroneResponse {
    private int count;
    private String next;
    private String previous;
    private List<Drones> results;

    public DroneResponse() {
    }

    public List<Drones> getResults() {
        return results;
    }
}
