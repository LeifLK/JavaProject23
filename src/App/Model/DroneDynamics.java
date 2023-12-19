package App.Model;

import java.util.List;
public class DroneDynamics {
    public String drone;
    public String timestamp;
    public int speed;
    public String align_roll;
    public String align_pitch;
    public String align_yaw;
    public String longitude;
    public String latitude;
    public int battery_status;
    public String last_seen;
    public String status;

    public DroneDynamics() {
    }

    public String getDrone() {
        return drone;
    }
    public String getTimestamp() {
        return timestamp;
    }

    public int getSpeed() {
        return speed;
    }

    public String getAlign_roll() {
        return align_roll;
    }

    public String getAlign_pitch() {
        return align_pitch;
    }

    public String getAlign_yaw() {
        return align_yaw;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public int getBattery_status() {
        return battery_status;
    }

    public String getLast_seen() {
        return last_seen;
    }

    public String getStatus() {
        return status;
    }
}

