package App.Services;


import App.Model.DroneDynamics;
import App.Model.DroneType;
import App.Model.Drones;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataStorage
{
    private List<Drones> dronesList;
    private List<DroneType> droneTypeList;
    private List<DroneDynamics> droneDynamicsList;
    private ApiService apiService;

    public DataStorage()
    {
        this.dronesList = new ArrayList<>();
        this.droneTypeList = new ArrayList<>();
        this.droneDynamicsList = new ArrayList<>();
        this.apiService = new ApiService();
    }
   public void populateDroneList() throws IOException, InterruptedException
   {
        //Get the JSON data from the API
        String droneJsonString = apiService.getDrones();
        //Split the JSONstring into individual Jsonobjects
        List<String> jsonObjects = JsonParser.splitJsonString(droneJsonString);
        //this. is to make sure to update the classmember droneList
        this.dronesList = new ArrayList<>();
        //Iterates through JsonObjects list and deserializes every object
        for (String jsonObject: jsonObjects)
        {
            Drones drone = JsonParser.parseDronesJson(jsonObject);

            // Getting the dronetypeurl
            String droneTypeUrl = drone.getDronetypeUrl();
            // Getting the Jsonstring fromt said dronetypeurl
            String droneTypeJsonString = apiService.getDroneType(droneTypeUrl);
            // Parsing the Jsonstring to droneType object
            DroneType droneType = JsonParser.parseDroneTypeJson(droneTypeJsonString);
            drone.setDroneType(droneType);
            dronesList.add(drone);
        }
    }
    public void populateDroneDynamicsList() throws IOException, InterruptedException
    {
        String droneDynamicsJsonString = apiService.getDroneDynamics();
        List<String> jsonObjects = JsonParser.splitJsonString(droneDynamicsJsonString);
        this.droneDynamicsList = new ArrayList<>();
        for (String jsonObject:jsonObjects)
        {
            DroneDynamics droneDynamics = JsonParser.parseDroneDynamicsJson(jsonObject);
            String droneUrl = droneDynamics.getDroneUrl();
            System.out.println(droneUrl);
            String droneJsonString = apiService.getDrone(droneUrl);
            Drones drones = JsonParser.parseDronesJson(droneJsonString);
            droneDynamics.setDrone(drones);
            droneDynamicsList.add(droneDynamics);
        }
    }
    public void populateDroneTypeList() throws IOException, InterruptedException
    {
        String droneTypeJsonString = apiService.getDroneTypes();
        List<String> jsonObjects = JsonParser.splitJsonString(droneTypeJsonString);
        this.droneTypeList = new ArrayList<>();
        for (String jsonObject:jsonObjects)
        {
            DroneType droneType = JsonParser.parseDroneTypeJson(jsonObject);
            droneTypeList.add(droneType);
        }
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
