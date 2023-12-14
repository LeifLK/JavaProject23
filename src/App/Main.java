package App;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try{
            API api = new API();
            // Call the method from the App.API-class to get the JSON string
            String jsonString = api.getDroneTypes();

            //Call the method from App.JsonParser to deserialize the JSON string
            // We use a App.DroneTypeResponse Objects because we need to access the list of App.DroneType
            DroneTypeResponse response = JsonParser.parseDroneTypeJson(jsonString);

            List<DroneType> dronesList = response.getResults();



            for (DroneType drone: dronesList) {
                System.out.println("Drone ID: "+ drone.getId());
                System.out.println("Drone Manufacturer: "+ drone.getManufacturer());
                System.out.println("Drone Typename: "+ drone.getTypename());
                System.out.println("Drone weight: " + drone.getWeight());
                System.out.println("Drone Max-speed: " + drone.getMax_speed());
                System.out.println("Drone Battery-Capacity: " + drone.getBattery_capacity());
                System.out.println("Drone Control-range: " + drone.getControl_range());
                System.out.println("Drone Max-Carriage: " + drone.getMax_carriage());
                System.out.println("---------------------------------------------");

            }


        }
        catch (IOException e){
            e.printStackTrace();
        }



    }
}