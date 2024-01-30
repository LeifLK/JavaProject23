package App;

import App.Services.DataStorage;
import App.UI.LandingPage;

import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final DataStorage dataStorage = new DataStorage();

    public static DataStorage getDataStorage() {
        return dataStorage;
    }

    public static void main(String[] args) {


        // api request test logger !!!!!!!!!!!!!!!!
        //ApiService apiService = new ApiService();
        //String s = apiService.ApiRequest("https://dronesim.facets-labs.com/api/dronedynamicsbla/?format=json&limit=5000");
        //String p = apiService.getAllPages("https://dronesim.facets-labs.com/api/dronedynamicsbla/?format=json&limit=5000")
        //dataStorage = new DataStorage();
        //Time Test
        long startTime = System.nanoTime();
        dataStorage.populateDroneList();
        dataStorage.populateDroneTypeList();
        dataStorage.populateDroneDynamicsList();
        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        totalTime = TimeUnit.SECONDS.convert(totalTime, TimeUnit.MICROSECONDS);
        System.out.println(totalTime + " microseconds");
/*
            System.out.println(dataStorage.getDronesList().get(1).getDronetypeUrl());
            System.out.println(dataStorage.getDronesList().get(1).getDronetype().getManufacturer());
            System.out.println(dataStorage.getDronesList().get(1).getDronetype().getTypename());

            System.out.println(dataStorage.getDroneTypeList().get(1).getManufacturer());
            System.out.println(dataStorage.getDroneTypeList().get(1).getTypename());
*/
        try {
            LandingPage landingPage = new LandingPage();
            landingPage.show();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

            /*JFrame myframe = new myframe();
            myframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);*/


        //dataStorage.setDroneDynamicsList(droneDynamicsList);

        //dataStorage.printNextSubset(25,1,drone71DynamicsList);
        //dataStorage.printNextSubset(25,2,drone71DynamicsList);

    }
}
