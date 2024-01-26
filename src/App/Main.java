package App;

import App.Model.DroneDynamics;
import App.Services.DataStorage;
import java.io.IOException;
import java.util.List;

import App.UI.*;

public class Main {
    private static DataStorage dataStorage = new DataStorage();

    public static DataStorage getDataStorage()
    {
        return dataStorage;
    }

    public static void main(String[] args) {
        try{

            //dataStorage = new DataStorage();

            dataStorage.populateDroneList();
            dataStorage.populateDroneTypeList();
            dataStorage.populateDroneDynamicsList();

/*
            System.out.println(dataStorage.getDronesList().get(1).getDronetypeUrl());
            System.out.println(dataStorage.getDronesList().get(1).getDronetype().getManufacturer());
            System.out.println(dataStorage.getDronesList().get(1).getDronetype().getTypename());

            System.out.println(dataStorage.getDroneTypeList().get(1).getManufacturer());
            System.out.println(dataStorage.getDroneTypeList().get(1).getTypename());
*/
            List<DroneDynamics> drone71DynamicsList = dataStorage.getDynamicsForDrone(71);

            LandingPage landingPage = new LandingPage();
            landingPage.show();

            /*JFrame myframe = new myframe();
            myframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);*/



            //dataStorage.setDroneDynamicsList(droneDynamicsList);

            //dataStorage.printNextSubset(25,1,drone71DynamicsList);
            //dataStorage.printNextSubset(25,2,drone71DynamicsList);



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
