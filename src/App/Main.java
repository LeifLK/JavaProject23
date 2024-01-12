package App;

import App.Model.DroneDynamics;
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

            dataStorage.popluateDroneDynamicsList();



            List<DroneDynamics> droneDynamics = dataStorage.getDroneDynamicsList();

            for (DroneDynamics dronedynamic: droneDynamics) {
                System.out.println("Timestamp:"+dronedynamic.getTimestamp());
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