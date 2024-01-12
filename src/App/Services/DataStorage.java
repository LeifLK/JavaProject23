package App.Services;


import App.Services.JsonUtils;
import App.Model.DroneDynamics;
import App.Model.DroneType;
import App.Model.Drones;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
   /* public void popluateDroneList() throws IOException, InterruptedException {
        //Get the JSON data from the API
        String droneJsonString = apiService.getDrones();
        List<String> jsonObjects = JsonParser.splitJsonString(droneJsonString);

        System.out.println(jsonObjects);


        this.dronesList.clear();

        List<Drones> dronesList = new ArrayList<>();
        for (String jsonObject: jsonObjects) {
            Drones drone = JsonParser.parseDronesJson(jsonObject);
            dronesList.add(drone);
        }

    }

    */
    public void popluateDroneTypeList() throws IOException, InterruptedException {
        //Get the JSON data from the API
        String droneTypeJsonString = apiService.getDroneTypes();
        // Parse the JSON data into Drone objects
        DroneType droneTypeResponse = JsonParser.parseDroneTypeJson(droneTypeJsonString);
        // Add Object to List
        //droneTypeList.addAll(droneType.getResults());
    }
    public void popluateDroneDynamicsList() throws IOException, InterruptedException {
        //Get the JSON data from the API
        String droneDynamicsJsonString = apiService.getDroneDynamics();
        // Parse the JSON data into Drone objects
        DroneDynamics droneDynamics = JsonParser.parseDroneDynamicsJson(droneDynamicsJsonString);
        // Add Object to List
       // droneDynamicsList.addAll(droneDynamics.getResults());
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
