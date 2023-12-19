package App.Model;

import App.DroneTypeResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;


public class Drones {

    private int id;
    private String dronetype;
    private String created;
    private String serialnumber;
    private int carriage_weight;
    private String carriage_type;

    public Drones() {
    }

    public int getId() {
        return id;
    }

    public String getDronetype() {
        return dronetype;
    }

    public String getCreated() {
        return created;
    }

    public String getSerialnumber() {
        return serialnumber;
    }

    public int getCarriage_weight() {
        return carriage_weight;
    }

    public String getCarriage_type() {
        return carriage_type;
    }

}

