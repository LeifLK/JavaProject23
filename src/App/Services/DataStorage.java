package App.Services;


import App.Model.DroneDynamics;
import App.Model.DroneType;
import App.Model.Drones;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


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
    private ApiService apiService;
    private FileService fileService;

    /**
     * Constructs a new instance of DataStorage.
     * Initializes lists to store drones, drone types, and drone dynamics.
     */
    public DataStorage() {
        this.fileService = new FileService();
        this.apiService = new ApiService();
        this.dronesList = fileService.loadListFromFile(DRONES_FILE_PATH);
        this.droneTypeList = fileService.loadListFromFile(DRONE_TYPES_FILE_PATH);
        this.droneDynamicsList = fileService.loadListFromFile(DRONE_DYNAMICS_FILE_PATH);
    }

    /**
     * Populates the drone list by fetching data from the API or loading it from a local file.
     * The method first checks if the drone list is already populated and returns early if it is.
     * If the drone list is not populated, it attempts to load the list from a local file.
     * If the local file does not exist or is empty, the method fetches drone data from the API,
     * parses the JSON data, populates the drone list, and saves the populated list to the local file for future use.
     */
    public void populateDroneList() {
        if (!dronesList.isEmpty()) {
            LOGGER.info("Drone list is already populated. Size: {}", dronesList.size());
            return;
        }
        this.dronesList = fileService.loadListFromFile(DRONES_FILE_PATH);
        if (!dronesList.isEmpty()) {
            LOGGER.info("Loaded drone list from local cache. Size: {}", dronesList.size());
            return;
        }
        LOGGER.info("No local drone data found. Fetching from API...");
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
            fileService.saveListToFile(DRONES_FILE_PATH, dronesList);
            LOGGER.info("Drone list fetched from API and saved locally.");
        }
    }


    private int findID(String Url) {
        int IDindex = "http://dronesim.facets-labs.com/api/drones/".length();
        if (IDindex > 0) {
            String IDString = Url.substring(IDindex, IDindex + 2);
            int id = Integer.parseInt(IDString);
            return id;
        }
        return -1;
    }

    private Drones findDroneInList(int ID) {
        for (int i = 0; i < dronesList.size(); i++) {
            if (dronesList.get(i).getId() == ID)
                return dronesList.get(i);
        }
        return null;
    }

    /**
     * Populates the drone dynamics list by fetching data from the API or loading it from a local file.
     * <p>
     * If the local file is empty or does not exist, the method fetches drone dynamics data from the API.
     * It then parses the JSON response, associates each DroneDynamics instance with its corresponding Drones instance,
     * populates the drone dynamics list with the parsed and associated data, and saves the list to the local file for future use.
     * If the drone dynamics list is successfully fetched and parsed, the method logs the size of the populated list.
     * <p>
     * If the local file already contains drone dynamics data, the method logs that it has loaded the data from the local cache
     * and returns early, preventing unnecessary API calls.
     * <p>
     * The method also handles potential parsing errors, logging any issues encountered during the parsing of individual drone dynamics JSON objects.
     * It also manages the association of DroneDynamics instances with their corresponding Drones instances based on URLs and IDs.
     */
    public void populateDroneDynamicsList() {
        // Attempt to load the drone dynamics list from the file
        this.droneDynamicsList = fileService.loadListFromFile(DRONE_DYNAMICS_FILE_PATH);

        if (droneDynamicsList.isEmpty()) {
            LOGGER.info("No local drone dynamics data found. Fetching from API...");
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

            // Save the fetched and processed list to the file
            fileService.saveListToFile(DRONE_DYNAMICS_FILE_PATH, droneDynamicsList);
            LOGGER.info("Drone dynamics list fetched from API and saved locally.");
        } else {
            LOGGER.info("Loaded drone dynamics list from local cache. Size: {}", droneDynamicsList.size());
        }
    }


    /**
     * Populates the drone type list by fetching data from the API or loading it from a local file.
     * <p>
     * If the local file is empty or does not exist, the method fetches drone type data from the API.
     * It then parses the JSON response, populates the drone type list with the parsed data, and saves the list to the local file for future use.
     * If the drone type list is successfully fetched and parsed, the method logs the size of the populated list.
     * <p>
     * If the local file already contains drone type data, the method logs that it has loaded the data from the local cache
     * and returns early, preventing unnecessary API calls.
     * <p>
     * The method also handles potential parsing errors, logging any issues encountered during the parsing of individual drone type JSON objects.
     */
    public void populateDroneTypeList() {
        // Attempt to load the drone type list from the file
        this.droneTypeList = fileService.loadListFromFile(DRONE_TYPES_FILE_PATH);

        if (droneTypeList.isEmpty()) {
            LOGGER.info("No local drone type data found. Fetching from API...");
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

            // Save the fetched and processed list to the file
            fileService.saveListToFile(DRONE_TYPES_FILE_PATH, droneTypeList);
            LOGGER.info("Drone type list fetched from API and saved locally.");
        } else {
            LOGGER.info("Loaded drone type list from local cache. Size: {}", droneTypeList.size());
        }
    }


    /**
     * Prints a subset of drone dynamics from a list, based on the specified page size and page number.
     * Formats the timestamp and displays relevant information for each drone dynamics object.
     *
     * @param pageSize the number of items to display per page
     * @param page     the current page number
     * @param list     the list of drone dynamics
     */
    public void printNextSubset(int pageSize, int page, List list) {
        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, list.size());

        if (startIndex < endIndex) {
            List<DroneDynamics> subset = list.subList(startIndex, endIndex);

            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX");
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MMM. d, yyyy, h:mm a");
            for (DroneDynamics droneDynamics : subset) {
                String formattedTimestamp = formatTimestamp(droneDynamics.getTimeStamp(), inputFormatter, outputFormatter);

                // Print or process each droneDynamics object
                System.out.println("TimeStamp: " + formattedTimestamp);
                System.out.println("DroneID: " + droneDynamics.getDrone().getId());
                System.out.println("Manufacturer: " + droneDynamics.getDrone().getDronetype().getManufacturer());
                System.out.println("---------------------");
            }
        } else {
            System.out.println("No more items to print.");
        }
    }

    /**
     * Formats a timestamp string from one format to another using the specified formatters.
     *
     * @param timestamp       the timestamp string to format
     * @param inputFormatter  the formatter of the input timestamp string
     * @param outputFormatter the formatter of the output timestamp string
     * @return the formatted timestamp string
     */
    public String formatTimestamp(String timestamp, DateTimeFormatter inputFormatter, DateTimeFormatter outputFormatter) {
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(timestamp, inputFormatter);
        return zonedDateTime.format(outputFormatter);
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

    /**
     * Retrieves the list of {@code Drones}.
     * This list contains all drones that have been fetched from the API.
     *
     * @return the list of {@code Drones}.
     */
    public List<Drones> getDronesList() {
        return dronesList;
    }

    /**
     * Retrieves the list of {@code DroneType}.
     * This list contains all drone types that have been fetched from the API.
     *
     * @return the list of {@code DroneType}.
     */
    public List<DroneType> getDroneTypeList() {
        return droneTypeList;
    }

    /**
     * Retrieves the list of {@code DroneDynamics}.
     * This list contains all drone dynamics that have been fetched from the API.
     *
     * @return the list of {@code DroneDynamics}.
     */
    public List<DroneDynamics> getDroneDynamicsList() {
        return droneDynamicsList;
    }

    /**
     * Sets the list of {@code DroneDynamics}.
     * This method allows replacing the current list of drone dynamics with a new one.
     *
     * @param droneDynamicsList the new list of {@code DroneDynamics} to be set.
     */
    public void setDroneDynamicsList(List<DroneDynamics> droneDynamicsList) {
        this.droneDynamicsList = droneDynamicsList;
    }
}
