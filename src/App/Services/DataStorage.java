package App.Services;


import App.Model.DroneDynamics;
import App.Model.DroneType;
import App.Model.Drones;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


/**
 * The {@code DataStorage} class is responsible for managing and storing data related to drones.
 * It provides functionality to populate lists of drones, drone types, and drone dynamics
 * by fetching data from an API and parsing it into respective model objects.
 *
 * @author Amin
 */
public class DataStorage {
    private static final Logger LOGGER = LogManager.getLogger(DataStorage.class);
    private static final String DRONES_FILE_PATH = "dronesList.ser";
    private static final String DRONE_TYPES_FILE_PATH = "droneTypesList.ser";
    private static final String DRONE_DYNAMICS_FILE_PATH = "droneDynamicsList.ser";

    private List<Drones> dronesList;
    private List<DroneType> droneTypeList;
    private List<DroneDynamics> droneDynamicsList;
    private final ApiService apiService;
    private final FileService fileService;

    /**
     * Constructs a new instance of DataStorage.
     * This constructor initializes lists to store drones, drone types, and drone dynamics.
     * It attempts to load the data from local files if they exist, otherwise, it fetches the data from the API.
     * If any of the lists (drones, drone types, or drone dynamics) remain empty after attempting to load from both the file and the API,
     * it throws a DataStorageNotAbleToPopulate exception.
     * If new data is fetched from the API, it saves this data locally for future use.
     */
    public DataStorage() {
        this.fileService = new FileService();
        this.apiService = new ApiService();
        boolean toSave = false;
        try {
            if (fileService.fileExists(DRONES_FILE_PATH)) {
                populateDroneListFromFile();
                LOGGER.info("Loading drone data from File");
            } else {
                populateDroneListFromAPI();
                toSave = true;
            }
            if (fileService.fileExists(DRONE_TYPES_FILE_PATH)) {
                populateDroneTypeListFromFile();
                LOGGER.info("Loading drone type from File");
            } else {
                populateDroneTypeListFromAPI();
                toSave = true;
            }
            if (fileService.fileExists(DRONE_DYNAMICS_FILE_PATH)) {
                populateDroneDynamicsListFromFile();
                LOGGER.info("Loading drone dynamics from File");
            } else {
                populateDroneDynamicsListFromAPI();
                toSave = true;
            }
            if (dronesList.isEmpty() || droneTypeList.isEmpty() || droneDynamicsList.isEmpty())
                throw new DataStorageNotAbleToPopulate("DataStorage not fully loaded, neither from File nor API");
            if (toSave)
                saveDataStorage();
            LOGGER.info("New data saved to files successfully.");
        } catch (DataStorageNotAbleToPopulate error) {
            LOGGER.warn("DataStorage is incomplete", error);
        }
    }

    /**
     * Saves the current state of the DataStorage to .ser files.
     * This includes saving the lists of drones, drone types, and drone dynamics.
     * The data is saved to predefined file paths.
     */
    public void saveDataStorage() {
        try {
            fileService.saveListToFile(DRONES_FILE_PATH, dronesList);
            LOGGER.info("Drone list saved successfully to " + DRONES_FILE_PATH);

            fileService.saveListToFile(DRONE_TYPES_FILE_PATH, droneTypeList);
            LOGGER.info("Drone type list saved successfully to " + DRONE_TYPES_FILE_PATH);

            fileService.saveListToFile(DRONE_DYNAMICS_FILE_PATH, droneDynamicsList);
            LOGGER.info("Drone dynamics list saved successfully to " + DRONE_DYNAMICS_FILE_PATH);
        } catch (Exception e) {
            LOGGER.error("Failed to save data storage: ", e);
        }
    }

    /**
     * Checks if any of the lists (drones, drone types, or drone dynamics) in the DataStorage are empty.
     *
     * @return true if any of the lists is empty, false otherwise.
     */
    public boolean isEmpty() {
        boolean isEmpty = dronesList.isEmpty() || droneTypeList.isEmpty() || droneDynamicsList.isEmpty();
        if (isEmpty) {
            LOGGER.warn("One or more lists in DataStorage are empty.");
        }
        return isEmpty;
    }

    /**
     * Creates a new instance of DataStorage with fresh data fetched from the API.
     * This method does not write to .ser files, it just populates the new instance with the latest data.
     *
     * @return A new instance of DataStorage with fresh data.
     */
    public static DataStorage createNewDataStorageFromAPI() {
        LOGGER.info("Loading new data storage with fresh data from the API.");

        // Create a new instance of DataStorage
        DataStorage newDataStorage = new DataStorage();

        // Fetching new data for drones, drone types, and drone dynamics
        newDataStorage.populateDroneListFromAPI();
        newDataStorage.populateDroneTypeListFromAPI();
        newDataStorage.populateDroneDynamicsListFromAPI();
        return newDataStorage;
    }


    /**
     * Populates the drone list by fetching data from the API.
     * This method makes an API request to fetch the drones data.
     * If the data is successfully fetched, it parses the JSON response and
     * populates the drone list with the parsed data.
     * <p>
     * The method also fetches the associated drone type data for each drone and sets it.
     * <p>
     * If no data is fetched from the API or if any error occurs during the parsing of the JSON data
     * or fetching associated drone type data, appropriate log messages are generated to indicate the issue.
     * <p>
     * If the drone list is successfully fetched and parsed from the API and the drone types are set,
     * the method logs the success message.
     */
    public void populateDroneListFromAPI() {
        dronesList = new ArrayList<>();
        LOGGER.info("Fetching DroneList from API...");
        String droneJsonString = apiService.getDrones();

        if (droneJsonString.isEmpty()) {
            LOGGER.warn("No drone data was fetched from the API.");
            return;
        }

        List<String> jsonObjects = JsonParser.splitJsonString(droneJsonString);
        for (String jsonObject : jsonObjects) {
            Drones drone = JsonParser.parseDronesJson(jsonObject);
            if (drone == null) {
                LOGGER.error("Failed to parse drone JSON: {}", jsonObject);
                continue;
            }
            String droneTypeUrl = drone.getDroneTypeUrl();
            String droneTypeJsonString = apiService.getDroneType(droneTypeUrl);
            DroneType droneType = JsonParser.parseDroneTypeJson(droneTypeJsonString);
            if (droneType == null) {
                LOGGER.error("Failed to parse drone type JSON: {}", droneTypeJsonString);
                continue;
            }
            drone.setDroneType(droneType);
            dronesList.add(drone);
        }
        if (!dronesList.isEmpty()) {
            LOGGER.info("Drone list successfully fetched from API.");
        }
    }

    /**
     * Populates the drone list by loading data from a local file.
     * The method first attempts to load the drone list from a file specified by {@code DRONES_FILE_PATH}.
     * If the file is not found or is empty, it logs a message indicating that no local data was found.
     * If the drone list is successfully loaded from the file, it logs the size of the loaded list.
     */
    public void populateDroneListFromFile() {
        this.dronesList = fileService.loadListFromFile(DRONES_FILE_PATH);

        if (dronesList.isEmpty()) {
            LOGGER.info("No local drone data found.");
        } else {
            LOGGER.info("Loaded drone list from local cache. Size: {}", dronesList.size());
        }
    }

    private int findID(String Url) {
        int IDindex = "http://dronesim.facets-labs.com/api/drones/".length();
        String IDString = Url.substring(IDindex, IDindex + 2);
        return Integer.parseInt(IDString);
    }

    private Drones findDroneInList(int ID) {
        for (Drones drone : dronesList) {
            if (drone.getId() == ID)
                return drone;
        }
        return null;
    }

    /**
     * Populates the drone dynamics list by loading data from a local file.
     * The method attempts to load the drone dynamics list from a file specified by {@code DRONE_DYNAMICS_FILE_PATH}.
     * If the file is not found or is empty, it logs a message indicating that no local drone dynamics data was found.
     * If the drone dynamics list is successfully loaded from the file, it logs the size of the loaded list.
     */
    public void populateDroneDynamicsListFromFile() {
        this.droneDynamicsList = fileService.loadListFromFile(DRONE_DYNAMICS_FILE_PATH);

        if (droneDynamicsList.isEmpty()) {
            LOGGER.info("No local drone dynamics data found.");
        } else {
            LOGGER.info("Loaded drone dynamics list from local cache. Size: {}", droneDynamicsList.size());
        }
    }

    /**
     * Populates the drone dynamics list by fetching data from the API.
     * This method makes an API request to fetch the drone dynamics data.
     * If the data is successfully fetched, it parses the JSON response and
     * populates the drone dynamics list with the parsed data. Each drone dynamics
     * object is also associated with its corresponding drone object.
     * <p>
     * If no data is fetched from the API or if any error occurs during the parsing of the JSON data,
     * appropriate log messages are generated to indicate the issue.
     * <p>
     * If the drone dynamics list is successfully fetched and parsed from the API,
     * the method logs the success message.
     */
    public void populateDroneDynamicsListFromAPI() {
        droneDynamicsList = new ArrayList<>();
        LOGGER.info("Fetching drone dynamics data from API...");
        String droneDynamicsJsonString = apiService.getDroneDynamics();
        if (droneDynamicsJsonString.isEmpty()) {
            LOGGER.warn("No drone dynamics data was fetched from the API.");
            return;
        }
        List<String> jsonObjects = JsonParser.splitJsonString(droneDynamicsJsonString);
        for (String jsonObject : jsonObjects) {
            DroneDynamics droneDynamics = JsonParser.parseDroneDynamicsJson(jsonObject);
            if (droneDynamics == null) {
                LOGGER.error("Failed to parse DroneDynamics JSON: {}", jsonObject);
                continue;
            }
            String droneUrl = droneDynamics.getDroneUrl();
            int toFindID = findID(droneUrl);
            Drones drone = findDroneInList(toFindID);
            droneDynamics.setDrone(drone);
            droneDynamicsList.add(droneDynamics);
        }

        if (!droneDynamicsList.isEmpty()) {
            //fileService.saveListToFile(DRONE_DYNAMICS_FILE_PATH, droneDynamicsList);
            LOGGER.info("Drone dynamics list successfully fetched from API.");
        }
    }

    /**
     * Populates the drone type list by loading data from a local file.
     * This method attempts to load the drone type list from a specified file path.
     * If the file is found and the data is successfully loaded, it populates the drone type list
     * and logs the size of the loaded list.
     * If the file is not found, or if it's empty, a log message is generated to indicate
     * that no local drone type data was found.
     */
    public void populateDroneTypeListFromFile() {
        this.droneTypeList = fileService.loadListFromFile(DRONE_TYPES_FILE_PATH);

        if (droneTypeList.isEmpty()) {
            LOGGER.info("No local drone type data found.");
        } else {
            LOGGER.info("Loaded drone type list from local cache. Size: {}", droneTypeList.size());
        }
    }

    /**
     * Populates the drone type list by fetching data from the API.
     * This method makes an API request to fetch the drone types data.
     * If the data is successfully fetched, it parses the JSON response and
     * populates the drone type list with the parsed data.
     * <p>
     * If no data is fetched from the API or if any error occurs during the parsing of the JSON data,
     * appropriate log messages are generated to indicate the issue.
     * <p>
     * If the drone type list is successfully fetched and parsed from the API,
     * the method logs the success message.
     */
    public void populateDroneTypeListFromAPI() {
        droneTypeList = new ArrayList<>();
        LOGGER.info("Fetching drone type data from API...");
        String droneTypeJsonString = apiService.getDroneTypes();

        if (droneTypeJsonString.isEmpty()) {
            LOGGER.warn("No drone type data was fetched from the API.");
            return;
        }
        List<String> jsonObjects = JsonParser.splitJsonString(droneTypeJsonString);
        for (String jsonObject : jsonObjects) {
            DroneType droneType = JsonParser.parseDroneTypeJson(jsonObject);
            if (droneType == null) {
                LOGGER.error("Failed to parse DroneType JSON: {}", jsonObject);
                continue;
            }
            droneTypeList.add(droneType);
        }

        if (!droneTypeList.isEmpty()) {
            LOGGER.info("Drone type list successfully fetched from API.");
        }
    }

    /**
     * Quickly compares this DataStorage instance with another to determine if they are likely to be equal.
     * This is a less accurate method that only compares the sizes of the lists and the last elements in the lists.
     *
     * @param other The other DataStorage instance to compare with.
     * @return true if the sizes of the lists and the last elements are equal, false otherwise.
     */
    public boolean isEqualTo(DataStorage other) {
        if (other == null) {
            return false;
        }
        return compareListSizeAndLastElement(this.dronesList, other.dronesList) &&
                compareListSizeAndLastElement(this.droneTypeList, other.droneTypeList) &&
                compareListSizeAndLastElement(this.droneDynamicsList, other.droneDynamicsList);
    }

    private <T> boolean compareListSizeAndLastElement(List<T> list1, List<T> list2) {
        if (list1.size() != list2.size()) {
            return false;
        }
        if (list1.isEmpty() && list2.isEmpty()) {
            return true;
        }
        // Comparing the last elements
        return list1.get(list1.size() - 1).equals(list2.get(list2.size() - 1));
    }

    /**
     * Compares this DataStorage instance with another object to determine if they are equal.
     * This method checks if the provided object is an instance of DataStorage and then compares
     * the drone list, drone type list, and drone dynamics list of both DataStorage instances.
     *
     * @param obj The object to compare this DataStorage instance with.
     * @return true if the provided object is a DataStorage instance and if the drone list,
     * drone type list, and drone dynamics list of both instances are equal. Otherwise, false.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        DataStorage other = (DataStorage) obj;
        return dronesList.equals(other.dronesList) &&
                droneTypeList.equals(other.droneTypeList) &&
                droneDynamicsList.equals(other.droneDynamicsList);
    }

    /**
     * Generates a hash code for this DataStorage instance.
     * This method uses the hash codes of the drone list, drone type list, and drone dynamics list
     * to generate a single hash code for the DataStorage instance by combining them in a consistent manner.
     *
     * @return An integer hash code value for this object, derived from the hash codes of the drone list,
     * drone type list, and drone dynamics list.
     */
    @Override
    public int hashCode() {
        return Objects.hash(dronesList, droneTypeList, droneDynamicsList);
    }

    /**
     * Retrieves a list of drone dynamics for a specific drone, identified by its ID.
     * Sorts the list of dynamics before returning it.
     *
     * @param droneId the ID of the drone
     * @return a sorted list of {@code DroneDynamics} for the specified drone
     */
    public List<DroneDynamics> getDynamicsForDrone(int droneId) {
        List<DroneDynamics> result = new ArrayList<>();
        for (DroneDynamics dynamics : droneDynamicsList) {
            if (dynamics.getDrone().getId() == droneId) {
                result.add(dynamics);
            }
        }
        Collections.sort(result);
        return result;
    }

    public List<Drones> getDronesList() {
        return dronesList;
    }

    public List<DroneType> getDroneTypeList() {
        return droneTypeList;
    }

    public List<DroneDynamics> getDroneDynamicsList() {
        return droneDynamicsList;
    }
}
