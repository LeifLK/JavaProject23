package App.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.io.Serializable;

import java.io.Serializable;

/**
 * Represents the type of a drone, containing various attributes like manufacturer, type name, and specifications.
 * It serves as a data model in the application, holding the details about a specific type of drone.
 *
 * @author Amin
 */
public class DroneType implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;
    @JsonProperty("id")
    private int id;
    @JsonProperty("manufacturer")
    private String manufacturer;
    @JsonProperty("typename")
    private String typename;
    @JsonProperty("weight")
    private int weight;
    @JsonProperty("max_speed")
    private int maxSpeed;
    @JsonProperty("battery_capacity")
    private int batteryCapacity;
    @JsonProperty("control_range")
    private int controlRange;
    @JsonProperty("max_carriage")
    private int maxCarriage;

    /**
     * Default constructor for creating an instance of DroneType.
     */
    public DroneType() {
    }

    /**
     * Gets the ID of the drone type.
     *
     * @return The unique identifier of the drone type.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the manufacturer of the drone.
     *
     * @return The name of the manufacturer of the drone.
     */
    public String getManufacturer() {
        return manufacturer;
    }

    /**
     * Gets the type name of the drone.
     *
     * @return The type name of the drone.
     */
    public String getTypename() {
        return typename;
    }

    /**
     * Gets the weight of the drone.
     *
     * @return The weight of the drone in grams.
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Gets the maximum speed of the drone.
     *
     * @return The maximum speed of the drone in km/h.
     */
    public int getMaxSpeed() {
        return maxSpeed;
    }

    /**
     * Gets the battery capacity of the drone.
     *
     * @return The battery capacity of the drone in mAh.
     */
    public int getBatteryCapacity() {
        return batteryCapacity;
    }

    /**
     * Gets the control range of the drone.
     *
     * @return The control range of the drone in meter.
     */
    public int getControlRange() {
        return controlRange;
    }

    /**
     * Gets the maximum carriage weight that the drone can carry.
     *
     * @return The maximum carriage weight the drone can carry in grams.
     */
    public int getMaxCarriage() {
        return maxCarriage;
    }
}

