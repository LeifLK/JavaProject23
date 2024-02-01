package App;

import App.Services.DataStorage;
import App.UI.LandingPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.MalformedURLException;
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

        dataStorage = new DataStorage();

        try {
            LandingPage landingPage = new LandingPage();
            landingPage.show();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        Timer timer = new Timer();
        timer.schedule( new TimerTask() {
            public void run() {
                refreshData();
                //TODO: Fix logging message
                LOGGER.info("dataStorage has tried to refresh");
            }
        }, 10000, 60*500); // 0, 60*1000);
    }
    public static void refreshData()
    {
        DataRefreshThread dataRefreshThread = new DataRefreshThread(newDataStorage -> {
            if (!newDataStorage.isEmpty() && !dataStorage.isEmpty()) {
                if (!newDataStorage.isEqualTo(dataStorage))
                {
                    dataStorage = newDataStorage;
                    LOGGER.info("dataStorage has been updated");
                    dataStorage.saveDataStorage();
                    System.out.println("New  Data storage has elements = " + dataStorage.getDronesList().size());
                    LOGGER.info("new dataStorage has been saved");
                }
            }
            if (newDataStorage.isEmpty())
            {
                LOGGER.warn("new DataStorage could not be fetched from API");
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