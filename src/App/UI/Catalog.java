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

/**
 * The Catalog is used to display all DroneTypes. It holds and shows the Attributes(see Attribute Class below)
 * of a DroneType. These Attributes get changed to those of another DroneType by clicking on the buttons included in this Panel.
 * The Catalog implements UIPanel to refresh the DataStorage and show potentially added DroneTypes.
 * @author Lennart Ochs
 */

public class Catalog extends JPanel implements UIPanel {
    private int index = 0;
    private int maxAmountOfDrones;
    private DataStorage dataStorage;
    private List<Attribute> attributeList = new ArrayList<>();
    private void refreshAttributes(DroneType currentDroneType) {
        for (Attribute attribute : attributeList) {
            attribute.refresh(currentDroneType);
        }
    }

    public Catalog() {
        dataStorage = App.Main.getDataStorage();
        maxAmountOfDrones = dataStorage.getDroneTypeList().size();
        initialize();
    }

    @Override
    public void refreshData() {
        this.dataStorage = Main.getDataStorage();
        maxAmountOfDrones = dataStorage.getDroneTypeList().size();
    }

    /**
     * The initialize Method initializes the Panel and sets the Attributes to show the DroneType
     * positioned at the current index on the DroneTypeList in the DataStorage
     *
     */
    private void initialize() {
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

    /**
     * Previous and next Page refresh the Attributes if the index is viable
     */
    private void previousPage() {
        if (index > 0) {
            index--;
            refreshAttributes(dataStorage.getDroneTypeList().get(index));
        }
    }
    /**
     * See previousPage Documentation
     */
    private void nextPage() {
        if (index < maxAmountOfDrones -1) {
            index++;
            refreshAttributes(dataStorage.getDroneTypeList().get(index));
        }
    }


}


/**
 * Attributes used in Catalog
 * It uses a BorderLayout to show the Identifier of the Name to the left and its value on the right side.
 * Most complexity comes from dynamic calls to the DroneType. (See the Constructor for further information)
 * @author Lennart Ochs
 */
 class Attribute extends JPanel {
    private static final Logger LOGGER = LogManager.getLogger(Catalog.class);
    private Method attributeGetter;
    private JLabel attributeValueLabel;
    private DroneType droneTypeToRepresent;
    /**
     * Attributes used in Catalog
     * @param attributeIdentifier Is used to show the users what value is shown in this Attribute.
     * @param droneType The actual DroneType from which the values get shown.
     * @param getMethodIdentifier The name of the Method used to get the Value from the DroneType.
     *                            It is to be read as GetMethod-Identifier, not as get-MethodIdentifier.
     * We get the value from invoking the droneType parameter, on the method described in the getMethodIdentifier, in the DroneType class.
     * If the invoking fails an exception will be Logged.
     */
    Attribute(String attributeIdentifier, DroneType droneType, String getMethodIdentifier) {
        this.setLayout(new BorderLayout());
        try {
            Object value = DroneType.class.getMethod(getMethodIdentifier).invoke(droneType);
            droneTypeToRepresent = droneType;
            attributeGetter = DroneType.class.getMethod(getMethodIdentifier);
            JLabel attributeIdentifierLabel = new JLabel(attributeIdentifier);
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
    /**
     * Attributes used in Catalog
     *
     * @param newDroneType The new DroneType from which the values get shown.
     *
     * We keep the Attribute Identifier and GetMethod, we just refresh the value on the attributeValueLabel with a new call to the GetMethod on the new DroneType.
     * If the invoking fails an exception will be Logged.
     */
    public void refresh(DroneType newDroneType) {
        try {
            droneTypeToRepresent = newDroneType;
            Object value = attributeGetter.invoke(droneTypeToRepresent);
            attributeValueLabel.setText(String.valueOf(value));
            this.validate();
        } catch (InvocationTargetException e) {
            LOGGER.warn("Failed to load Attributes from DroneType");
        } catch (IllegalAccessException e) {
            LOGGER.warn("Not allowed to load Attributes from DroneType");
        }
    }
}