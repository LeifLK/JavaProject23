package App.Model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Represents the dynamic information of a drone, including its current status,
 * position, battery status, and alignment details. This class provides a way to
 * model the dynamic data retrieved from the drone or the drone API.
 *
 * @author Amin
 */
public class DroneDynamics implements Comparable<DroneDynamics>, Serializable {
    @Serial
    private static final long serialVersionUID = 3L;
    @JsonProperty("droneUrl")
    private String droneUrl;
    @JsonIgnore
    private Drones Drones;
    @JsonProperty("timestamp")
    private String timeStamp;
    @JsonProperty("speed")
    private int speed;
    @JsonProperty("align_roll")
    private String alignRoll;
    @JsonProperty("align_pitch")
    private String alignPitch;
    @JsonProperty("align_yaw")
    private String alignYaw;
    @JsonProperty("longitude")
    private String longitude;
    @JsonProperty("latitude")
    private String latitude;
    @JsonProperty("battery_status")
    private int batteryStatus;
    @JsonProperty("last_seen")
    private String lastSeen;
    @JsonProperty("status")
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
    public String getTimeStamp() {
        return timeStamp;
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
    public String getAlignRoll() {
        return alignRoll;
    }

    /**
     * Gets the pitch alignment of the drone.
     *
     * @return The pitch alignment as a string.
     */
    public String getAlignPitch() {
        return alignPitch;
    }

    /**
     * Gets the yaw alignment of the drone.
     *
     * @return The yaw alignment as a string.
     */
    public String getAlignYaw() {
        return alignYaw;
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
    public int getBatteryStatus() {
        return batteryStatus;
    }

    /**
     * Gets the last seen timestamp of the drone.
     *
     * @return The last seen timestamp as a string.
     */
    public String getLastSeen() {
        return lastSeen;
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
        LocalDateTime thisDateTime = LocalDateTime.parse(this.timeStamp, inputFormatter);
        LocalDateTime otherDateTime = LocalDateTime.parse(other.timeStamp, inputFormatter);

        return thisDateTime.compareTo(otherDateTime);
    }

    /**
     * Checks if this DroneDynamics instance is equal to another object.
     * If all fields are equal, the two objects are considered equal.
     *
     * @param obj the reference object with which to compare.
     * @return true if this object is the same as the obj argument; false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        DroneDynamics other = (DroneDynamics) obj;
        return speed == other.speed &&
                batteryStatus == other.batteryStatus &&
                Objects.equals(droneUrl, other.droneUrl) &&
                Objects.equals(Drones, other.Drones) &&
                Objects.equals(timeStamp, other.timeStamp) &&
                Objects.equals(alignRoll, other.alignRoll) &&
                Objects.equals(alignPitch, other.alignPitch) &&
                Objects.equals(alignYaw, other.alignYaw) &&
                Objects.equals(longitude, other.longitude) &&
                Objects.equals(latitude, other.latitude) &&
                Objects.equals(lastSeen, other.lastSeen) &&
                Objects.equals(status, other.status);
    }

    /**
     * Generates a hash code for this DroneDynamics instance.
     * The hash code is generated based on the hash codes of the individual fields of the class.
     *
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(droneUrl, Drones, timeStamp, speed, alignRoll, alignPitch, alignYaw, longitude, latitude, batteryStatus, lastSeen, status);
    }
}


