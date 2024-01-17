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

            dataStorage.populateDroneList();
            dataStorage.populateDroneTypeList();
            dataStorage.populateDroneDynamicsList();


            List<DroneDynamics> droneDynamicsList = dataStorage.getDroneDynamicsList();
            for (DroneDynamics dronedynamics: droneDynamicsList) {
                System.out.println("Serialnumber: " + dronedynamics.getDrone().getSerialnumber());
                System.out.println("Last Seen: " +dronedynamics.getLast_seen());
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