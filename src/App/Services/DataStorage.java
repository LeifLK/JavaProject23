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
    private static final Logger LOGGER = LogManager.getLogger(JsonUtils.class);
    private List<Drones> dronesList;
    private List<DroneType> droneTypeList;
    private List<DroneDynamics> droneDynamicsList;
    private ApiService apiService;

    /**
     * Constructs a new instance of DataStorage.
     * Initializes lists to store drones, drone types, and drone dynamics.
     */
    public DataStorage() {
        this.dronesList = new ArrayList<>();
        this.droneTypeList = new ArrayList<>();
        this.droneDynamicsList = new ArrayList<>();
        this.apiService = new ApiService();
    }

    /**
     * Populates the drone list by fetching data from the API.
     * If the drone list is already populated, the method returns early.
     * Otherwise, it fetches drone data, parses it, and populates the list.
     */
    public void populateDroneList() {
        if (!dronesList.isEmpty()) {
            LOGGER.info("Drone list is already populated. Size: {}", dronesList.size());
            return;
        }
        String droneJsonString = apiService.getDrones();
        if (droneJsonString.isEmpty()) {
            LOGGER.warn("No drone data was fetched from the API.");
            return;
        }
        List<String> jsonObjects = JsonParser.splitJsonString(droneJsonString);
        this.dronesList = new ArrayList<>();
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
        Collections.sort(droneDynamicsList);
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
     * Populates the drone dynamics list by fetching data from the API.
     * If the list is empty, it fetches data, parses it, and populates the list.
     * If the list is already populated, logs the current size and does not refetch the data.
     */
    public void populateDroneDynamicsList() {
        if (droneDynamicsList.isEmpty()) {
            LOGGER.info("Data is being fetched from the API...");
            String droneDynamicsJsonString = apiService.getDroneDynamics();
            List<String> jsonObjects = JsonParser.splitJsonString(droneDynamicsJsonString);
            this.droneDynamicsList = new ArrayList<>();
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
        } else {
            LOGGER.info("List was already filled, size: {}", droneDynamicsList.size());
        }
    }

    /**
     * Populates the drone type list by fetching data from the API.
     * If the list is empty, it fetches data, parses it, and populates the list.
     * If the list is already populated, logs the current size and does not refetch the data.
     */
    public void populateDroneTypeList() {
        if (!droneTypeList.isEmpty()) {
            LOGGER.info("Drone type list is already populated. Size: {}", droneTypeList.size());
            return;
        }
        LOGGER.info("Fetching drone type data from the API...");
        String droneTypeJsonString = apiService.getDroneTypes();
        List<String> jsonObjects = JsonParser.splitJsonString(droneTypeJsonString);
        this.droneTypeList = new ArrayList<>();
        for (String jsonObject : jsonObjects) {
            DroneType droneType = JsonParser.parseDroneTypeJson(jsonObject);
            if (droneType == null) {
                LOGGER.error("Failed to parse DroneType JSON: {}", jsonObject);
                continue;
            }
            droneTypeList.add(droneType);
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
                String formattedTimestamp = formatTimestamp(droneDynamics.getTimestamp(), inputFormatter, outputFormatter);

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
