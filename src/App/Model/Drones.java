package App.Model;

import App.Services.ApiService;
import App.Services.DataStorage;
import App.Services.JsonParser;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Drones {

    private int id;
    private DroneType droneType;
    private String created;
    private String serialnumber;
    private int carriage_weight;
    private String carriage_type;
    @JsonProperty("dronetype")
    private String dronetypeUrl;

    public Drones(){

    }
    public int getId() {
        return id;
    }

    public DroneType getDronetype() {
        return droneType;
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

    public String getDronetypeUrl(){return dronetypeUrl;}

    public void setDroneType(DroneType droneType) {
        this.droneType = droneType;
    }

}

