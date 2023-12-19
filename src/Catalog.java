import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
public class Catalog {
    public static JLabel resizeImage(String imagePath) {
        try {
            BufferedImage image = ImageIO.read(new File(imagePath));
            Image resizedImage = image.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(resizedImage);
            JLabel label = new JLabel(resizedIcon);
            return label;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void displayJframe(String dataObject) {
        //Get texts from Object

        frame.setLayout(new GridBagLayout());

        frame.getContentPane().removeAll();

        JLabel titleLabel = new JLabel(dataObject);
        JLabel manufacturerLabel = new JLabel("Manufacturer:");
        JLabel typeLabel = new JLabel("Type:");
        JLabel attribute1Label = new JLabel("Attribute1");
        JLabel attribute2Label = new JLabel("Attribute2");

        JLabel value1Label = new JLabel("Value1");
        JLabel value2Label = new JLabel("Value2");
        JLabel imgLabel = new JLabel();
        try {
            imgLabel = resizeImage("C:\\Users\\Lennart\\IdeaProjects\\JavaProject23\\altairsomething.jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }


        GridBagConstraints c1 = new GridBagConstraints();
        c1.fill = GridBagConstraints.HORIZONTAL;
        c1.ipady = 40;
        c1.anchor = GridBagConstraints.NORTH;

        GridBagConstraints c2 = new GridBagConstraints();
        c2.fill = GridBagConstraints.HORIZONTAL;
        c2.ipadx = 10;
        c2.ipady = 25;
        c2.gridy = 1;
        c2.anchor = GridBagConstraints.SOUTHWEST;

        frame.add(titleLabel, c1);
        frame.add(imgLabel, c1);
        c2.gridy++;
        frame.add(manufacturerLabel, c2);
        c2.gridy++;
        frame.add(typeLabel, c2);
        c2.gridy++;

        frame.add(attribute1Label, c2);
        frame.add(value1Label, c2);
        c2.gridy++;
        frame.add(attribute2Label, c2);
        frame.add(value2Label, c2);

        //back/forward Button
        c2.gridy++;
        JButton backwardsButton = new JButton("Back");
        frame.add(backwardsButton, c2);
        backwardsButton.addActionListener(e -> {
            previousPage();
        });
        JButton forwardButton = new JButton("Forward");
        frame.add(forwardButton, c2);
        forwardButton.addActionListener(e -> {
            nextPage();
        });
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void previousPage() {
        if (index > 0) {
            index--;
            displayJframe("Just a string representing" + Integer.toString(index) + " DroneObject");
        }
    }
    public static void nextPage()
    {
        if (!(index >= maxAmountOfDrones))
        {
            index++;
            displayJframe("Just a string representing" + Integer.toString(index) + " DroneObject");
        }
    }
    static int index = 0;
    static int maxAmountOfDrones = 0;
    static JFrame frame = new JFrame("Title");
    public static void main(String[] args) {
        //Get Objects from API/JSON Converter
        //Get max Amount of Drones from API/JSON Converter

        maxAmountOfDrones = 15;
        displayJframe("Just a string representing" + Integer.toString(index) + " DroneObject");

        }
    }