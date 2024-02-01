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

    public static DataStorage getDataStorage() {
        return dataStorage;
    }

    public static void main(String[] args) {


        LandingPage landingPage = new LandingPage();
        landingPage.show();
        dataStorage = new DataStorage();

        enableLandingPage(landingPage);

        Timer refreshTimer = new Timer();
        refreshTimer.schedule( new TimerTask() {
            public void run() {
                refreshData();
                enableLandingPage(landingPage);
            }
        }, 0, 60*1000);
    }

    private static void enableLandingPage(LandingPage landingPage)
    {
        if  (landingPage != null) {
            if (!dataStorage.isEmpty()) {
                landingPage.dataLoaded();
            }
        }
    }
    public static void refreshData()
    {
        LOGGER.info("DataStorage tries to refresh");
        DataRefreshThread dataRefreshThread = new DataRefreshThread(newDataStorage -> {
            if (newDataStorage.isEmpty())
            {
                LOGGER.warn("new DataStorage could not be fetched, refreshing of Data not possible");
                return;
            }
            if (dataStorage.isEmpty())
            {
                LOGGER.info("current DataStorage is empty, will be replaced");
                dataStorage = newDataStorage;
                LOGGER.info("new DataStorage is implemented");
                dataStorage.saveDataStorage();
                LOGGER.info("new DataStorage has been saved to file");
            }
            if (!newDataStorage.isEqualTo(dataStorage))
            {
                LOGGER.info("new DataStorage differentiates from current dataStorage");
                dataStorage = newDataStorage;
                LOGGER.info("new DataStorage is implemented");
                dataStorage.saveDataStorage();
                LOGGER.info("new DataStorage has been saved to file");
            }
            else
                LOGGER.info("new DataStorage equals DataStorage on file");
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