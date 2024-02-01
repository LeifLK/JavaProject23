package App;

import App.Services.DataStorage;
import App.UI.LandingPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Timer;
import java.util.TimerTask;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);
    private static DataStorage dataStorage;

    public static void setDataStorage(DataStorage newDataStorage) {
        dataStorage = newDataStorage;
    }

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
        /*
        long startTime = System.nanoTime();

        //dataStorage = DataStorage.loadNewDataStorage(true);
        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        totalTime = TimeUnit.SECONDS.convert(totalTime, TimeUnit.MICROSECONDS);
        System.out.println(totalTime + " microseconds");*/
/*
            System.out.println(dataStorage.getDronesList().get(1).getDronetypeUrl());
            System.out.println(dataStorage.getDronesList().get(1).getDronetype().getManufacturer());
            System.out.println(dataStorage.getDronesList().get(1).getDronetype().getTypename());

            System.out.println(dataStorage.getDroneTypeList().get(1).getManufacturer());
            System.out.println(dataStorage.getDroneTypeList().get(1).getTypename());
*/
        LandingPage landingPage = new LandingPage();
        landingPage.show();
        dataStorage = new DataStorage();


        Timer timer = new Timer();
        timer.schedule( new TimerTask() {
            public void run() {
                if  (landingPage != null) {
                    if (!dataStorage.isEmpty()) {
                        landingPage.dataLoaded();
                    }
                }
                refreshData();
                LOGGER.info("DataStorage tries to refresh");
            }
        }, 0, 60*1000);
    }
    public static void refreshData()
    {
        DataRefreshThread dataRefreshThread = new DataRefreshThread(newDataStorage -> {
            if (newDataStorage.isEmpty())
            {
                LOGGER.warn("new DataStorage could not be fetched, refreshing of Data not possible");
                return;
            }
            if (dataStorage.isEmpty() || !newDataStorage.isEqualTo(dataStorage))
            {
                if (dataStorage.isEmpty())
                    LOGGER.warn("current DataStorage is empty, will be replaced");
                else
                    LOGGER.warn("new DataStorage differentiates from current dataStorage");

                dataStorage = newDataStorage;
                LOGGER.warn("new DataStorage is implemented");
                dataStorage.saveDataStorage();
                LOGGER.warn("new DataStorage has been saved to file");
                return;
            }
        });
        dataRefreshThread.start();
    }
}

class DataRefreshThread extends Thread {
    private final Callback callback;

    public DataRefreshThread(Callback callback) {
        this.callback = callback;
    }

    public void run() {
        DataStorage newDataStorage = DataStorage.createNewDataStorageFromAPI();
        callback.onDataStorageReady(newDataStorage);
    }

    public interface Callback {
        void onDataStorageReady(DataStorage newDataStorage);
    }
}