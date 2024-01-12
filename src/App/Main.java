package App;

import App.Model.DroneDynamics;
import App.Model.DroneType;
import App.Model.Drones;
import App.Services.ApiService;
import App.Services.DataStorage;
import App.Services.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try{


            DataStorage dataStorage = new DataStorage();

            dataStorage.popluateDroneList();

            List<Drones> dronesList = dataStorage.getDronesList();

            for (Drones drone:dronesList) {
                System.out.println(drone.getId());
                System.out.println(drone.getDronetype().getManufacturer());
                System.out.println(drone.getSerialnumber());
                System.out.println(drone.getCarriage_weight());
                System.out.println(drone.getCreated());
                System.out.println("----------------------");
            }




        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }
}