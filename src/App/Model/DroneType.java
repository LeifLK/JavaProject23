package App.Model;

/**
 * Represents the type of a drone, containing various attributes like manufacturer, type name, and specifications.
 * It serves as a data model in the application, holding the details about a specific type of drone.
 *
 * @author Amin
 */
public class DroneType {


    private int id;
    private String manufacturer;
    private String typename;
    private int weight;
    private int max_speed;
    private int battery_capacity;
    private int control_range;
    private int max_carriage;

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
    public int getMax_speed() {
        return max_speed;
    }

    /**
     * Gets the battery capacity of the drone.
     *
     * @return The battery capacity of the drone in mAh.
     */
    public int getBattery_capacity() {
        return battery_capacity;
    }

    /**
     * Gets the control range of the drone.
     *
     * @return The control range of the drone in meter.
     */
    public int getControl_range() {
        return control_range;
    }

    /**
     * Gets the maximum carriage weight that the drone can carry.
     *
     * @return The maximum carriage weight the drone can carry in grams.
     */
    public int getMax_carriage() {
        return max_carriage;
    }
}

