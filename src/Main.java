
import java.io.IOException;
public class Main {
    public static void main(String[] args) {
        API api = new API();
        int testId = 59660;
        int testId2 = 51;

        try {
            String droneDynamics = api.getDroneDynamics();
            System.out.println("Drone Dynamics: " + droneDynamics);
        } catch (IOException e) {
            System.err.println("Error fetching Drone Dynamics: " + e.getMessage());
            e.printStackTrace();
        }

        try {
            String droneDynamicsWithId = api.getDroneDynamics(testId);
            System.out.println("Drone Dynamics with ID " + testId + ": " + droneDynamicsWithId);
        } catch (IOException e) {
            System.err.println("Error fetching Drone Dynamics with ID " + testId + ": " + e.getMessage());
            e.printStackTrace();
        }

        try {
            String drones = api.getDrones();
            System.out.println("Drones: " + drones);
        } catch (IOException e) {
            System.err.println("Error fetching Drones: " + e.getMessage());
            e.printStackTrace();
        }

        try {
            String dronesWithId = api.getDrones(testId2);
            System.out.println("Drones with ID " + testId2 + ": " + dronesWithId);
        } catch (IOException e) {
            System.err.println("Error fetching Drones with ID " + testId2 + ": " + e.getMessage());
            e.printStackTrace();
        }

        try {
            String droneTypes = api.getDroneTypes();
            System.out.println("Drone Types: " + droneTypes);
        } catch (IOException e) {
            System.err.println("Error fetching Drone Types: " + e.getMessage());
            e.printStackTrace();
        }

        try {
            String droneTypesWithId = api.getDroneTypes(testId2);
            System.out.println("Drone Types with ID " + testId2 + ": " + droneTypesWithId);
        } catch (IOException e) {
            System.err.println("Error fetching Drone Types with ID " + testId2 + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}