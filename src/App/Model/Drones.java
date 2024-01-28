package App.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a drone, containing various attributes like serial number, carriage weight, and the type of drone.
 * This class serves as a data model in the application, holding the details about a specific drone.
 *
 * @author Amin
 */
public class Drones {

    private int id;
    private DroneType droneType;
    private String created;
    private String serialNumber;
    private int carriageWeight;
    private String carriageType;
    @JsonProperty("dronetype")
    private String droneTypeUrl;

    /**
     * Default constructor for creating an instance of Drones.
     */
    public Drones(){
    }

    /**
     * Gets the ID of the drone.
     *
     * @return The unique identifier of the drone.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the type of the drone.
     *
     * @return The {@code DroneType} object representing the type of the drone.
     */
    public DroneType getDronetype() {
        return droneType;
    }

    /**
     * Gets the creation date of the drone.
     *
     * @return The creation date of the drone.
     */
    public String getCreated() {
        return created;
    }

    /**
     * Gets the serial number of the drone.
     *
     * @return The serial number of the drone.
     */
    public String getSerialnumber() {
        return serialNumber;
    }

    /**
     * Gets the carriage weight of the drone.
     *
     * @return The carriage weight of the drone in grams.
     */
    public int getCarriageWeight() {
        return carriageWeight;
    }

    /**
     * Gets the carriage type of the drone.
     *
     * @return The carriage type of the drone.
     */
    public String getCarriageType() {
        return carriageType;
    }

    /**
     * Gets the URL of the drone type.
     * This URL points to the specific drone type in the API.
     *
     * @return The URL string of the drone type.
     */
    public String getDroneTypeUrl() {
        return droneTypeUrl;
    }

    /**
     * Sets the type of the drone.
     *
     * @param droneType The {@code DroneType} object to set as the drone's type.
     */
    public void setDroneType(DroneType droneType) {
        this.droneType = droneType;
    }

}

