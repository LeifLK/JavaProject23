package App.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a drone, containing various attributes like serial number, carriage weight, and the type of drone.
 * This class serves as a data model in the application, holding the details about a specific drone.
 *
 * @author Amin
 */
public class Drones implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @JsonProperty("id")
    private int id;
    private DroneType droneType;
    @JsonProperty("created")
    private String created;
    @JsonProperty("serialnumber")
    private String serialNumber;
    @JsonProperty("carriage_weight")
    private int carriageWeight;
    @JsonProperty("carriage_type")
    private String carriageType;
    @JsonProperty("dronetype")
    private String droneTypeUrl;

    /**
     * Default constructor for creating an instance of Drones.
     */
    public Drones() {
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

    /**
     * Compares this drone to the specified object. The result is true if
     * the argument is not null and is a Drones object that has the same id, droneType,
     * created date, serialNumber, carriageWeight, carriageType, and droneTypeUrl as this object.
     *
     * @param obj the object to compare this {@code Drones} against
     * @return true if the given object represents a {@code Drones} equivalent to this drone, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Drones other = (Drones) obj;
        return id == other.id &&
                Objects.equals(droneType, other.droneType) &&
                Objects.equals(created, other.created) &&
                Objects.equals(serialNumber, other.serialNumber) &&
                carriageWeight == other.carriageWeight &&
                Objects.equals(carriageType, other.carriageType) &&
                Objects.equals(droneTypeUrl, other.droneTypeUrl);
    }

    /**
     * Generates a hash code for the Drones instance.
     * The hash code is generated based on the hash codes of the individual fields of the class.
     *
     * @return the hash code value for this Drones instance.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, droneType, created, serialNumber, carriageWeight, carriageType, droneTypeUrl);
    }
}

