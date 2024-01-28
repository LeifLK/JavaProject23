package App.Model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents the dynamic information of a drone, including its current status,
 * position, battery status, and alignment details. This class provides a way to
 * model the dynamic data retrieved from the drone or the drone API.
 *
 * @author Amin
 */
public class DroneDynamics implements Comparable<DroneDynamics> {
    @JsonProperty("droneUrl")
    private String droneUrl;
    @JsonIgnore
    private Drones Drones;
    private String timestamp;
    private int speed;
    private String align_roll;
    private String align_pitch;
    private String align_yaw;
    private String longitude;
    private String latitude;
    private int battery_status;
    private String last_seen;
    private String status;


    /**
     * Default constructor for creating an instance of DroneDynamics.
     */
    public DroneDynamics() {
    }

    /**
     * Gets the associated drone object.
     *
     * @return The associated {@code Drones} object.
     */
    public Drones getDrone() {
        return Drones;
    }

    /**
     * Gets the timestamp of when the drone dynamics were recorded.
     *
     * @return The timestamp as a string.
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * Gets the current speed of the drone.
     *
     * @return The speed of the drone.
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Gets the roll alignment of the drone.
     *
     * @return The roll alignment as a string.
     */
    public String getAlign_roll() {
        return align_roll;
    }

    /**
     * Gets the pitch alignment of the drone.
     *
     * @return The pitch alignment as a string.
     */
    public String getAlign_pitch() {
        return align_pitch;
    }

    /**
     * Gets the yaw alignment of the drone.
     *
     * @return The yaw alignment as a string.
     */
    public String getAlign_yaw() {
        return align_yaw;
    }

    /**
     * Gets the longitude of the drone's current position.
     *
     * @return The longitude as a string.
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * Gets the latitude of the drone's current position.
     *
     * @return The latitude as a string.
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * Gets the current battery status of the drone.
     *
     * @return The battery status as an integer.
     */
    public int getBattery_status() {
        return battery_status;
    }

    /**
     * Gets the last seen timestamp of the drone.
     *
     * @return The last seen timestamp as a string.
     */
    public String getLast_seen() {
        return last_seen;
    }

    /**
     * Gets the current status of the drone.
     *
     * @return The status as a string.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Gets the URL associated with this drone type.
     *
     * @return The URL as a string.
     */
    public String getDroneUrl() {
        return droneUrl;
    }

    /**
     * Sets the associated drone object.
     *
     * @param Drones The {@code Drones} object to associate.
     */
    public void setDrone(Drones Drones) {
        this.Drones = Drones;
    }

    /**
     * Compares this {@code DroneDynamics} object with another to order them chronologically based on the timestamp.
     *
     * @param other The other {@code DroneDynamics} object to compare with.
     * @return A negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     */
   @Override
    public int compareTo(DroneDynamics other) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX");
        //Parsing String TimeStamp to LocalDateTime Format for comparison
        LocalDateTime thisDateTime = LocalDateTime.parse(this.timestamp,inputFormatter);
        LocalDateTime otherDateTime = LocalDateTime.parse(other.timestamp,inputFormatter);

        return thisDateTime.compareTo(otherDateTime);
    }
}

