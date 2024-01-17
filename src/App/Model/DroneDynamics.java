package App.Model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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



    public DroneDynamics() {
    }
    public Drones getDrone() {return Drones;}

    public String getTimestamp() {return timestamp;}

    public int getSpeed() {return speed;}

    public String getAlign_roll() {return align_roll;}

    public String getAlign_pitch() {return align_pitch;}

    public String getAlign_yaw() {return align_yaw;}

    public String getLongitude() {return longitude;}

    public String getLatitude() {return latitude;}

    public int getBattery_status() {return battery_status;}

    public String getLast_seen() {return last_seen;}

    public String getStatus() {return status;}

    public String getDroneUrl() {/*System.out.println(droneUrl)*/; return droneUrl;}

    public void setDrone(Drones Drones) {this.Drones = Drones;}

    @Override
    public int compareTo(DroneDynamics other)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try
        {
            Date thisDate = dateFormat.parse(this.timestamp);
            Date otherDate = dateFormat.parse(other.timestamp);

            return thisDate.compareTo(otherDate);
        } catch (ParseException e)
        {
            e.printStackTrace();
            return 0;
        }


    }
}

