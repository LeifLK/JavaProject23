package App;

import App.Model.Drones;
import App.Services.ApiService;
import App.Services.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try{

            ApiService ap = new ApiService();
            String Jsonstring = ap.getDrones();

            List<String> jsonStrings = JsonParser.splitJsonString(Jsonstring);

            List<Drones> dronesList = new ArrayList<>();
            for (String droneJson:jsonStrings) {
                Drones drones = JsonParser.parseDronesJson(droneJson);
                dronesList.add(drones);
            }

            for (Drones drone : dronesList) {
                System.out.println(drone.getId());
                System.out.println(drone.getSerialnumber());
                System.out.println(drone.getCarriage_type());
                System.out.println(drone.getDronetype());
                System.out.println(drone.getCreated());
                System.out.println("-----------------------------------");
            }
        }
        catch (IOException e){
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}