package App.Services;


import App.Model.DroneDynamics;
import App.Model.DroneType;
import App.Model.Drones;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.time.LocalDateTime;
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
       if (!dronesList.isEmpty()){return;}
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
       //Collections.sort(droneDynamicsList);
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
        if (droneDynamicsList.isEmpty())
        {
            System.out.println("Data is fetched from API...");
            String droneDynamicsJsonString = apiService.getDroneDynamics();
            List<String> jsonObjects = JsonParser.splitJsonString(droneDynamicsJsonString);
            this.droneDynamicsList = new ArrayList<>();
            for (String jsonObject : jsonObjects) {
                DroneDynamics droneDynamics = JsonParser.parseDroneDynamicsJson(jsonObject);
                String droneUrl = droneDynamics.getDroneUrl();
                int toFindID = findID(droneUrl);
                Drones drone = findDroneInList(toFindID);
                droneDynamics.setDrone(drone);
                droneDynamicsList.add(droneDynamics);
            }
        }else {
            System.out.println("list was filled"+droneDynamicsList.size());
        }
    }
    public void populateDroneTypeList() throws IOException, InterruptedException
    {
        if (!droneTypeList.isEmpty()){return;}
        String droneTypeJsonString = apiService.getDroneTypes();
        List<String> jsonObjects = JsonParser.splitJsonString(droneTypeJsonString);
        this.droneTypeList = new ArrayList<>();
        for (String jsonObject:jsonObjects)
        {
            DroneType droneType = JsonParser.parseDroneTypeJson(jsonObject);
            droneTypeList.add(droneType);
        }
    }

    public void printNextSubset(int pageSize, int page, List list) {
        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, list.size());

        if (startIndex < endIndex) {
            List<DroneDynamics> subset = list.subList(startIndex, endIndex);

            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX");
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MMM. d, yyyy, h:mm a");
            for (DroneDynamics droneDynamics : subset) {
               String formattedTimestamp = formatTimestamp(droneDynamics.getTimestamp(),inputFormatter,outputFormatter);

               // Print or process each droneDynamics object
                System.out.println("TimeStamp: "+formattedTimestamp);
                System.out.println("DroneID: "+droneDynamics.getDrone().getId());
                System.out.println("Manufacturer: "+droneDynamics.getDrone().getDronetype().getManufacturer());
                System.out.println("---------------------");
            }
        } else {
            System.out.println("No more items to print.");
        }
    }
   public String formatTimestamp(String timestamp, DateTimeFormatter inputFormatter, DateTimeFormatter outputFormatter) {
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(timestamp, inputFormatter);
        return zonedDateTime.format(outputFormatter);
    }
    public List<DroneDynamics> getDynamicsForDrone(int droneId) {
        List<DroneDynamics> result = new ArrayList<>();
        for (DroneDynamics dynamics : droneDynamicsList) {
            if (dynamics.getDrone().getId() == droneId) {
                result.add(dynamics);
            }
        }
        Collections.sort(result);
        return result;
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

    public void setDroneDynamicsList(List<DroneDynamics> droneDynamicsList) {
        this.droneDynamicsList = droneDynamicsList;
    }
}
