package App;

import App.Model.DroneDynamics;
import App.Services.DataStorage;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try{

            DataStorage dataStorage = new DataStorage();

            dataStorage.populateDroneList();
            dataStorage.populateDroneTypeList();
            dataStorage.populateDroneDynamicsList();


            List<DroneDynamics> drone71DynamicsList = dataStorage.getDynamicsForDrone(71);



            //dataStorage.setDroneDynamicsList(droneDynamicsList);

            dataStorage.printNextSubset(25,1,drone71DynamicsList);
            dataStorage.printNextSubset(25,2,drone71DynamicsList);



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