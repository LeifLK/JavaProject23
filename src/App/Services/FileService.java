package App.Services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The FileService class provides utilities for saving and loading lists to and from files.
 * It uses Java's Object Input/Output Stream for serialization and deserialization of objects.
 *
 * @author Leif
 */
public class FileService {
    private static final Logger LOGGER = LogManager.getLogger(FileService.class);

    /**
     * Loads a list of objects from a file. The objects in the list should be serializable.
     *
     * @param filePath the path of the file from which the list is to be loaded
     * @param <T>      the type of the objects in the list
     * @return a list of objects of type T if the file exists and is not empty, otherwise returns an empty list.
     */
    public <T> List<T> loadListFromFile(String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.length() > 0) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                return (List<T>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                LOGGER.error("Error reading from file: {}", filePath, e);
            }
        }
        return new ArrayList<>();
    }

    /**
     * Checks if a file exists at the specified file path.
     *
     * @param filePath The path of the file to be checked.
     * @return true if the file exists, false otherwise.
     */
    public boolean fileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * Saves a list of objects to a file. The objects in the list should be serializable.
     *
     * @param filePath the path of the file to which the list is to be saved
     * @param data     the list of objects to save to the file
     */
    public void saveListToFile(String filePath, Object data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(data);
        } catch (IOException e) {
            LOGGER.error("Error writing to file: {}", filePath, e);
        }
    }
}
