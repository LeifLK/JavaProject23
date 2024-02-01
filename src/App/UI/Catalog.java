package App.UI;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Method;

import App.Main;
import App.Model.DroneType;
import App.Services.DataStorage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Catalog extends JPanel implements UIPanel {
    public JPanel getJPanel() {
        return this;
    }

    List<Attribute> attributeList = new ArrayList<>();

    public void refreshAttributes(DroneType currentDroneType) {
        for (Attribute attribute : attributeList) {
            attribute.refresh(currentDroneType);
        }
    }
    public void initialize() {
        //Clear and Reset Panel
        this.removeAll();
        attributeList = new ArrayList<>();
        this.setLayout(new GridBagLayout());

        GridBagConstraints gbConstraints = new GridBagConstraints();
        gbConstraints.fill = GridBagConstraints.HORIZONTAL;
        gbConstraints.ipadx = 80;
        gbConstraints.ipady = 20;
        gbConstraints.gridy = 1;
        gbConstraints.anchor = GridBagConstraints.SOUTHWEST;

        DroneType currentDroneType = dataStorage.getDroneTypeList().get(index);
        attributeList.add(new Attribute("Manufacturer:", currentDroneType, "getManufacturer"));
        attributeList.add(new Attribute("Typename:", currentDroneType, "getTypename"));
        attributeList.add(new Attribute("ID:", currentDroneType, "getId"));
        attributeList.add(new Attribute("Battery Capacity:", currentDroneType, "getBatteryCapacity"));
        attributeList.add(new Attribute("Control Range:", currentDroneType, "getControlRange"));
        attributeList.add(new Attribute("Max Carriage:", currentDroneType, "getMaxCarriage"));
        attributeList.add(new Attribute("Max Speed:", currentDroneType, "getMaxSpeed"));
        attributeList.add(new Attribute("Weight:", currentDroneType, "getWeight"));

        for (Attribute attribute : attributeList) {
            this.add(attribute, gbConstraints);
            gbConstraints.gridy++;
        }

        //Back&Forward Buttons
        gbConstraints.gridy++;
        JButton backwardsButton = new JButton("Back");
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(backwardsButton, BorderLayout.WEST);
        backwardsButton.addActionListener(e -> previousPage());

        JButton forwardButton = new JButton("Forward");
        panel.add(forwardButton, BorderLayout.CENTER);
        forwardButton.addActionListener(e -> nextPage());
        this.add(panel, gbConstraints);
        this.validate();
    }

    @Override
    public void refreshData() {
        this.dataStorage = Main.getDataStorage();
        maxAmountOfDrones = dataStorage.getDroneTypeList().size();
    }

    private void previousPage() {
        if (index > 0) {
            index--;
            refreshAttributes(dataStorage.getDroneTypeList().get(index));
        }
    }

    private void nextPage() {
        if (index < maxAmountOfDrones - 1) {
            index++;
            refreshAttributes(dataStorage.getDroneTypeList().get(index));
        }
    }

    int index = 0;

    int maxAmountOfDrones;
    DataStorage dataStorage;

    public Catalog() {
        dataStorage = App.Main.getDataStorage();
        maxAmountOfDrones = dataStorage.getDroneTypeList().size();
        initialize();
    }
}

 class Attribute extends JPanel {
    JLabel attributeIdentifierLabel;
    Method attributeGetter;
    JLabel attributeValueLabel;
    DroneType droneTypeToRepresent;
    private static final Logger LOGGER = LogManager.getLogger(Catalog.class);

    Attribute(String attributeIdentifier, DroneType droneType, String getMethodIdentifier) {
        this.setLayout(new BorderLayout());

        try {
            Object value = DroneType.class.getMethod(getMethodIdentifier).invoke(droneType);
            droneTypeToRepresent = droneType;
            attributeGetter = DroneType.class.getMethod(getMethodIdentifier);
            attributeIdentifierLabel = new JLabel(attributeIdentifier);
            attributeValueLabel = new JLabel(String.valueOf(value));
            this.add(attributeIdentifierLabel, BorderLayout.WEST);
            this.add(attributeValueLabel, BorderLayout.EAST);
            this.validate();
        } catch (InvocationTargetException e) {
            LOGGER.warn("Failed to load Attributes from DroneType");
        } catch (IllegalAccessException e) {
            LOGGER.warn("Not allowed to load Attributes from DroneType");
        } catch (NoSuchMethodException e) {
            LOGGER.warn("Method used to get DroneType Attributes not found");
        }
    }

    void refresh(DroneType newDroneType) {
        try {
            droneTypeToRepresent = newDroneType;
            Object value = attributeGetter.invoke(droneTypeToRepresent);
            attributeValueLabel.setText(String.valueOf(value));
            this.validate();
        } catch (InvocationTargetException invocationTargetException) {
            System.out.println("InvocationTargetException in refresh for DroneType");
        } catch (IllegalAccessException illegalAccessException) {
            System.out.println("IllegalAccessException in refresh for DroneType");
        }
    }
}