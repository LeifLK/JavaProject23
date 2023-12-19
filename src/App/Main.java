package App;

import App.Model.Drones;
import App.Services.DataStorage;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try{
            DataStorage dataStorage = new DataStorage();

            dataStorage.popluateDroneList();

            List<Drones> dronesList = dataStorage.getDronesList();

            for (Drones drones: dronesList) {
                System.out.println("Id: "+drones.getId());
            }

        }
        catch (IOException e){
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}