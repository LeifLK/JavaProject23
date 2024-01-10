import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
public class catalog {
    public static JLabel resizeImage(String imagePath) {
        try {
            BufferedImage image = ImageIO.read(new File(imagePath));
            Image resizedImage = image.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(resizedImage);
            return new JLabel(resizedIcon);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JPanel catalog = new JPanel();
    public JPanel getJPanel(){

        return catalog;
    }

    myframe mainFrame;
    public void setframe(myframe myframe){

        mainFrame = myframe;
    }
    public void createJPanel(String dataObject) {
        //Get texts from Object


        catalog.removeAll();
        catalog.setLayout(new GridBagLayout());

        JLabel titleLabel = new JLabel(dataObject);
        JLabel manufacturerLabel = new JLabel("Manufacturer:");
        JLabel typeLabel = new JLabel("Type:");
        JLabel attribute1Label = new JLabel("Attribute1");
        JLabel attribute2Label = new JLabel("Attribute2");

        JLabel value1Label = new JLabel("Value1");
        JLabel value2Label = new JLabel("Value2");
        JLabel imgLabel = new JLabel();
        try {
            imgLabel = resizeImage("C:\\Users\\andre\\Downloads\\altairsomething.jpg");
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

        catalog.add(titleLabel, c1);
        if (imgLabel != null)
            catalog.add(imgLabel, c1);
        c2.gridy++;
        catalog.add(manufacturerLabel, c2);
        c2.gridy++;
        catalog.add(typeLabel, c2);
        c2.gridy++;

        catalog.add(attribute1Label, c2);
        catalog.add(value1Label, c2);
        c2.gridy++;
        catalog.add(attribute2Label, c2);
        catalog.add(value2Label, c2);

        //back/forward Button
        c2.gridy++;
        JButton backwardsButton = new JButton("Back");
        catalog.add(backwardsButton, c2);
        backwardsButton.addActionListener(e -> previousPage());
        JButton forwardButton = new JButton("Forward");
        catalog.add(forwardButton, c2);
        forwardButton.addActionListener((ActionEvent e) -> nextPage());
        catalog.setSize(700, 800);
        catalog.setVisible(true);
    }

    public void previousPage() {
        if (index > 0) {
            mainFrame.reload("Catalog");
            index--;
            createJPanel("Just a string representing" + index + " DroneObject");


        }
    }
    public void nextPage()
    {
        if (!(index >= maxAmountOfDrones))
        {
            mainFrame.reload("Catalog");
            index++;
            createJPanel("Just a string representing" + index + " DroneObject");

        }
    }
    static int index = 0;

    static int maxAmountOfDrones = 0;

    public catalog() {
        //Get Objects from API/JSON Converter
        //Get max Amount of Drones from API/JSON Converter

        maxAmountOfDrones = 15;
        createJPanel("Just a string representing" + index + " DroneObject");

    }
}

