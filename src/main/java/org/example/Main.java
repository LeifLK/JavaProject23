package org.example;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        ApiService apiService = new ApiService();

        try {
            // Test fetching all drone dynamics
            System.out.println("All the Drone Dynamics:");
            String droneDynamics = apiService.getDroneDynamics();
            System.out.println(droneDynamics);

            // Test fetching specific drone dynamic by ID
            System.out.println("\nDrone Dynamic with ID 59660:");
            String specificDroneDynamic = apiService.getDroneDynamics(59660);
            System.out.println(specificDroneDynamic);

            // Test fetching all drones
            System.out.println("\nAll Drones:");
            String drones = apiService.getDrones();
            System.out.println(drones);

            // Test fetching specific drone by ID
            System.out.println("\nDrone with ID 51:");
            String specificDrone = apiService.getDrones(51);
            System.out.println(specificDrone);

            // Test fetching all drone types
            System.out.println("\nAll Drone Types:");
            String droneTypes = apiService.getDroneTypes();
            System.out.println(droneTypes);

            // Test fetching specific drone type by ID
            System.out.println("\nDrone Type with ID 51:");
            String specificDroneType = apiService.getDroneTypes(51);
            System.out.println(specificDroneType);

        } catch (IOException e) {
            System.out.println("An IO error occurred: " + e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println("The operation was interrupted: " + e.getMessage());
            // Restore the interrupted status
            Thread.currentThread().interrupt();
        }
    }
}
