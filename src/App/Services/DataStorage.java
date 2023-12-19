package App.Services;

import App.DroneDynamicsRespones;
import App.DroneResponse;
import App.DroneTypeResponse;
import App.Model.DroneDynamics;
import App.Model.DroneType;
import App.Model.Drones;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataStorage {
    private List<Drones> dronesList;
    private List<DroneType> droneTypeList;
    private List<DroneDynamics> droneDynamicsList;
    private ApiService apiService;

    public DataStorage(){
        this.dronesList = new ArrayList<>();
        this.droneTypeList = new ArrayList<>();
        this.droneDynamicsList = new ArrayList<>();
        this.apiService = new ApiService();
    }
    public void popluateDroneList() throws IOException, InterruptedException {
        //Get the JSON data from the API
        String droneJsonString = apiService.getDrones();
        // Parse the JSON data into Drone objects
        DroneResponse droneResponse = JsonParser.parseDronesJson(droneJsonString);
        // Add Object to List
        dronesList.addAll(droneResponse.getResults());
    }
    public void popluateDroneTypeList() throws IOException, InterruptedException {
        //Get the JSON data from the API
        String droneTypeJsonString = apiService.getDroneTypes();
        // Parse the JSON data into Drone objects
        DroneTypeResponse droneTypeResponse = JsonParser.parseDroneTypeJson(droneTypeJsonString);
        // Add Object to List
        droneTypeList.addAll(droneTypeResponse.getResults());
    }
    public void popluateDroneDynamicsList() throws IOException, InterruptedException {
        //Get the JSON data from the API
        String droneDynamicsJsonString = apiService.getDroneDynamics();
        // Parse the JSON data into Drone objects
        DroneDynamicsRespones droneDynamicsRespones = JsonParser.parseDroneDynamicsJson(droneDynamicsJsonString);
        // Add Object to List
        droneDynamicsList.addAll(droneDynamicsRespones.getResults());
    }

    public List<Drones> getDronesList() {
        return dronesList;
    }

    public List<DroneType> getDroneTypeList() {
        return droneTypeList;
    }

    public List<DroneDynamics> getDroneDynamicsList() {
        return droneDynamicsList;
    }
}
