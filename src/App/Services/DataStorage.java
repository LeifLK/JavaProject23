package App.Services;


import App.Model.DroneDynamics;
import App.Model.DroneType;
import App.Model.Drones;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
    private int findID(String Url)
    {
        int IDindex = "http://dronesim.facets-labs.com/api/drones/".length();
        if (IDindex > 0)
        {
            String IDString = Url.substring(IDindex, IDindex+2);
            int id = Integer.parseInt(IDString);
            return id;
        }
        return -1;
    }
    private Drones findDroneInList(int ID)
    {
        for (int i = 0; i < dronesList.size(); i++)
        {
            if (dronesList.get(i).getId() == ID)
                return dronesList.get(i);
        }
        return null;
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
            int toFindID = findID(droneUrl);
            Drones drone = findDroneInList(toFindID);
            droneDynamics.setDrone(drone);
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
        Collections.sort(droneDynamicsList);
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
