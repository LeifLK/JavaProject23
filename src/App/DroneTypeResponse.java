package App;

import java.util.List;

public class DroneTypeResponse {
    private int count;
    private String next;
    private String previous;
    private List<DroneType> results;

    public DroneTypeResponse() {
    }

    public int getCount() {
        return count;
    }

    public String getNext() {
        return next;
    }

    public String getPrevious() {
        return previous;
    }

    public List<DroneType> getResults() {
        return results;
    }
}
