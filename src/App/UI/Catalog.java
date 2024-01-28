package App.UI;

import javax.swing.*;
import java.awt.*;

import App.Model.DroneType;
import App.Services.DataStorage;
public class Catalog {

    public JPanel catalog = new JPanel();
    public JPanel getJPanel(){
        return catalog;
    }

    myframe mainFrame;
    public void setFrame(myframe myframe){
        mainFrame = myframe;
    }

    private void addJLabel(String identifier, String value, GridBagConstraints constraints)
    {

        JLabel attributeIdentifierLabel = new JLabel(identifier);
        JLabel valueLabel = new JLabel(value);
        attributeIdentifierLabel.setHorizontalAlignment(SwingConstants.LEFT);
        catalog.add(attributeIdentifierLabel, constraints);
        valueLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        catalog.add(valueLabel, constraints);
        constraints.gridy++;
    }

    public void createJPanel(int index) {
        //Clear and Reset Panel
        catalog.removeAll();
        catalog.setLayout(new GridBagLayout());

        GridBagConstraints gbConstraints = new GridBagConstraints();
        gbConstraints.fill = GridBagConstraints.HORIZONTAL;
        gbConstraints.ipadx = 40;
        gbConstraints.ipady = 20;
        gbConstraints.gridy = 1;
        gbConstraints.anchor = GridBagConstraints.SOUTHWEST;

        DroneType currentDroneType = dataStorage.getDroneTypeList().get(index);
        addJLabel("Manufacturer:", currentDroneType.getManufacturer(), gbConstraints);
        addJLabel("Typename:", currentDroneType.getTypename(), gbConstraints);
        //Add Empty field for nicer visuals
        addJLabel("" ,"", gbConstraints);
        addJLabel("ID:", String.valueOf(currentDroneType.getId()), gbConstraints);
        addJLabel("Battery Capacity:", String.valueOf(currentDroneType.getBatteryCapacity()), gbConstraints);
        addJLabel("Control Range:",  String.valueOf(currentDroneType.getControlRange()), gbConstraints);
        addJLabel("Max Carriage:", String.valueOf(currentDroneType.getMaxCarriage()), gbConstraints);
        addJLabel("Max Speed:", String.valueOf(currentDroneType.getMaxSpeed()), gbConstraints);
        addJLabel("Weight:", String.valueOf(currentDroneType.getWeight()), gbConstraints);

        //Back&Forward Buttons
        gbConstraints.gridy++;
        JButton backwardsButton = new JButton("Back");
        catalog.add(backwardsButton, gbConstraints);
        backwardsButton.addActionListener(e -> previousPage());
        JButton forwardButton = new JButton("Forward");
        catalog.add(forwardButton, gbConstraints);
        forwardButton.addActionListener(e -> nextPage());
        catalog.setPreferredSize(new Dimension(700, 800));
        catalog.setVisible(true);
    }

    public void previousPage()
    {
        if (index > 0) {
            mainFrame.reloadCatalog();
            index--;
            createJPanel(index);


        }
    }
    public void nextPage()
    {
        if (index < maxAmountOfDrones -1)
        {
            mainFrame.reloadCatalog();
            index++;
            createJPanel(index);

        }
    }
    static int index = 0;

    static int maxAmountOfDrones = 0;
    static DataStorage dataStorage;
    public Catalog() {
        dataStorage = App.Main.getDataStorage();
        maxAmountOfDrones = dataStorage.getDroneTypeList().size();
        createJPanel(index);
    }
}

