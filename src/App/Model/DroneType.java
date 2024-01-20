package App.Model;


public class DroneType {


    private int id;
    private String manufacturer;
    private String typename;
    private int weight;
    private int max_speed;
    private int battery_capacity;
    private int control_range;
    private int max_carriage;

    public DroneType() {
    }

    public int getId() {
        return id;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getTypename() {
        return typename;
    }

    public int getWeight() {
        return weight;
    }

    public int getMax_speed() {
        return max_speed;
    }

    public int getBattery_capacity() {
        return battery_capacity;
    }

    public int getControl_range() {
        return control_range;
    }

    public int getMax_carriage() {
        return max_carriage;
    }
}

