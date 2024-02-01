package App.UI;


import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.*;
import java.util.List;

import App.Main;
import App.Model.DroneDynamics;
import App.Model.Drones;
import App.Services.DataStorage;


/**
 * The History consists of a comboBox, described as the DroneSelector, on in the top left corner and a timestamp in the top right corner.
 * Near the bottom you see a little info label a slider and a text-field (to input the tick) for the current time.
 * The middle gets a custom DroneDrawingPanel which in short, shows the Drones relative to its latitude and longitude.
 * It is called DroneDrawingPanel because in the first version drew the drones as graphics on the panel, which are now replaced by buttons, as they are more clickable.
 * @author Lennart Ochs
 *
 */
public class History extends JPanel implements UIPanel {
    private final JLabel timeLabel = new JLabel();
    private final JFormattedTextField timeInput = new JFormattedTextField();
    private List<List<DroneDynamics>> droneDynamicsPerDrone = new ArrayList<>();
    private JPanel comboBoxPanel = new JPanel();
    private mainFrame mainFrame;
    private DrawingPanel drawnDronePanel;
    private JSlider timeSlider;
    private JComboBox<Object> comboBox;
    private DataStorage dataStorage;

    void setComboxBoxToEmpty() {
        comboBox.setSelectedIndex(0);
        this.validate();
    }

    /**
     * Creates the History Panel consisting of TopPanel, DrawnDronePanel, and the TimeSliderPanel.
     * History hold the needed Drones/DroneDynamics for these elements, and sets them here.
     */
    public History() {
        this.setLayout(new BorderLayout());

        dataStorage = App.Main.getDataStorage();
        List<Drones> drones = dataStorage.getDronesList();
        for (Drones drone : drones) {
            droneDynamicsPerDrone.add(dataStorage.getDynamicsForDrone(drone.getId()));
        }

        int maxDroneDynamics = droneDynamicsPerDrone.getLast().size() - 1;
        createTopPanel();
        JPanel timeSliderPanel = createTimeSliderPanel(maxDroneDynamics, maxDroneDynamics);

        drawnDronePanel = getNewDrawingPanel(maxDroneDynamics);
        this.add(drawnDronePanel, BorderLayout.CENTER);
        this.add(timeSliderPanel, BorderLayout.SOUTH);
        this.validate();
    }
    /**
     * Refreshes the DataStorage and sets the Information represented new.
     * Specifically the Data on the DrawingPanel and the DroneSelector.
     */
    @Override
    public void refreshData() {
        this.dataStorage = Main.getDataStorage();
        droneDynamicsPerDrone = new ArrayList<>();
        List<Drones> drones = dataStorage.getDronesList();
        for (Drones drone : drones) {
            droneDynamicsPerDrone.add(dataStorage.getDynamicsForDrone(drone.getId()));
        }
        refreshDrawingPanel(timeSlider.getValue(), comboBox.getSelectedItem());
        refreshDroneSelector();
    }
    /**
     * @param mainFrame the mainFrame found in MainFrame.java
     *  This Class needs the Mainframe of the application for forwarding the User to the Dashboard of a specific Drone.
     *  This happens by clicking on the corresponding Popupmenu Item of a drone.
     */
    public void setMainFrame(mainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }
    public mainFrame getMainFrame() {
        return mainFrame;
    }

    DroneDynamics getDroneDynamicsForDroneAtCurrentTime(int id) {
        for (List<DroneDynamics> droneDynamics : droneDynamicsPerDrone) {
            if (droneDynamics.getFirst().getDrone().getId() == id)
                return droneDynamics.get(timeSlider.getValue());
        }
        return null;
    }
    private void createTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        initDroneSelector();
        topPanel.add(comboBoxPanel, BorderLayout.WEST);
        refreshTimeLabel(0);
        topPanel.add(timeLabel, BorderLayout.EAST);
        this.add(topPanel, BorderLayout.NORTH);
    }
    private void initDroneSelector() {
        comboBoxPanel = new JPanel();
        comboBox = new JComboBox<>();
        refreshDroneSelector();
        comboBoxPanel.add(comboBox);
    }
    private void refreshDroneSelector() {
        comboBox.removeAllItems();
        comboBox.insertItemAt("None", 0);
        for (List<DroneDynamics> droneDynamics : droneDynamicsPerDrone) {
            comboBox.addItem(droneDynamics.getFirst().getDrone());
        }
        comboBox.setSelectedIndex(0);
        comboBox.addActionListener(e -> {
            refreshDrawingPanel(timeSlider.getValue(), comboBox.getSelectedItem());
            refreshTimeLabel(timeSlider.getValue());
        });
        comboBox.setRenderer(new droneCellRenderer());
    }
    private JPanel createTimeSliderPanel(int max, int currentTick) {

        JPanel timeSliderPanel = new JPanel();
        timeSliderPanel.setLayout(new GridBagLayout());
        //timeSliderPanel.setBorder(BorderFactory.createEmptyBorder());
        GridBagConstraints gbConstraints = new GridBagConstraints();
        gbConstraints.anchor = GridBagConstraints.LINE_START;
        gbConstraints.fill = GridBagConstraints.HORIZONTAL;
        gbConstraints.insets = new Insets(5, 5, 5, 5);

        gbConstraints.gridwidth = 1;
        gbConstraints.gridx = 0;
        gbConstraints.gridy = 0;
        gbConstraints.weightx = 0.05;
        JLabel label = new JLabel("Valid Ticks between 0 - " + max);
        timeSliderPanel.add(label, gbConstraints);

        gbConstraints.gridx++;
        gbConstraints.weightx = 0.9;
        timeSlider = new JSlider(0, max, currentTick);
        timeSlider.addChangeListener(e -> reactToTimeSliderChange());
        timeSlider.setMajorTickSpacing(500);
        timeSlider.setMinorTickSpacing(100);
        timeSlider.setPaintLabels(true);
        timeSlider.setPaintTicks(true);
        timeSliderPanel.add(timeSlider, gbConstraints);

        gbConstraints.gridx++;
        gbConstraints.weightx = 0.05;

        timeInput.setValue(max);
        timeInput.addPropertyChangeListener("value", e -> setTimeSliderValue((Integer) timeInput.getValue()));
        timeSliderPanel.setPreferredSize(new Dimension(400, 120));
        timeSliderPanel.setMaximumSize(new Dimension(550, 120));
        timeSliderPanel.add(timeInput, gbConstraints);
        return timeSliderPanel;
    }

    private DrawingPanel getNewDrawingPanel(int valueInTicks) {
        List<DroneDynamics> dronesToDraw = new ArrayList<>();

        for (List<DroneDynamics> droneDynamic : droneDynamicsPerDrone) {
            dronesToDraw.add(droneDynamic.get(valueInTicks));
        }

        drawnDronePanel = new DrawingPanel(dronesToDraw, this, null);
        //this.add(drawnDronePanel, BorderLayout.CENTER);

        drawnDronePanel.setMaximumSize(new Dimension(400, 400));
        return drawnDronePanel;
    }
    private void refreshDrawingPanel(int timeInTicks, Object drone) {
        this.remove(drawnDronePanel);
        List<DroneDynamics> dronesToDraw = new ArrayList<>();
        if (!(drone instanceof Drones)) {
            for (List<DroneDynamics> droneDynamic : droneDynamicsPerDrone) {
                dronesToDraw.add(droneDynamic.get(timeInTicks));
            }
            drawnDronePanel = new DrawingPanel(dronesToDraw, this, null);
        }
        else
        {
            int valueInTicks = timeSlider.getValue();
            for (List<DroneDynamics> droneDynamic : droneDynamicsPerDrone) {
                dronesToDraw.add(droneDynamic.get(valueInTicks));
            }
            drawnDronePanel = new DrawingPanel(dronesToDraw, this, (Drones) drone);
        }
        this.add(drawnDronePanel);
        this.validate();
    }

    private void refreshTimeLabel(int timeInTicks) {
        timeLabel.setText(droneDynamicsPerDrone.get(0).get(timeInTicks).getTimeStamp());
        this.validate();
    }
    private void setTimeSliderValue(int value) {
        if (value < timeSlider.getMaximum())
            timeSlider.setValue(value);
    }
    private void reactToTimeSliderChange() {
        int value = timeSlider.getValue();
        if (comboBox.getSelectedIndex() == 0) {
            refreshDrawingPanel(value, null);
            refreshTimeLabel(value);
        } else {
            refreshDrawingPanel(value, comboBox.getSelectedItem());
            refreshTimeLabel(value);
        }
        timeInput.setValue(value);
    }

}
/**
 * A new CellRenderer for a ComboBox representing Drones.
 * It shows the drones as Drone ID in the Text of the ComboBox, but holds the actual drones as its values.
 * Used in History and Dashboard
 * https://stackoverflow.com/questions/27049473/implementing-listcellrenderer
 * @author martinzed314 (copied by Lennart Ochs)
 *
 */
class droneCellRenderer extends DefaultListCellRenderer {
    public Component getListCellRendererComponent(
            JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {
        if (value instanceof Drones) {
            value = "Drone " + ((Drones) value).getId();
        }
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        return this;
    }
}

/**
 * The Panel which displays the current DroneDynamics of all Drones passed to it.
 * It uses a GridBagLayout to add these Positions (see below) to different coordinates on the screen.
 *
 * @author Lennart Ochs
 */

class DrawingPanel extends JPanel {
    private final List<Position> dronePositions = new ArrayList<>();
    /**
     * @param dronesToDraw A list of DroneDynamics, the position (Latitude & Longitude) of these droneDynamics are modified and represented by positions in a Grid bagLayout.
     * @param mainPanel the HistoryPanel, get passed on to the positions for redirecting to the dashboard
     * @param droneToHighlight a single Drone to Mark in Map. Null is a viable option, if no drone is to be marked.
     * It uses addDroneDynamic to create the Position-Objects as needed.
     * Afterward it goes through the Positions to configure these Positions and adds them to this Panel.
     * (if droneToHighlight != null it marks the corresponding Position red.
     */
    DrawingPanel(List<DroneDynamics> dronesToDraw, History mainPanel, Drones droneToHighlight) {
        this.removeAll();
        this.setLayout(new GridBagLayout());
        for (DroneDynamics droneDynamic : dronesToDraw) {
            addDroneDynamic(droneDynamic);
        }
        setPreferredSize(new Dimension(500, 500));
        setMaximumSize(new Dimension(500, 500));
        for (Position position : dronePositions) {
            GridBagConstraints gbConstraints = new GridBagConstraints();

            gbConstraints.gridx = position.getYCoordinate();
            gbConstraints.gridy = position.getXCoordinate();
            if (droneToHighlight != null)
                if (position.dronesAtPosition.contains(droneToHighlight))
                    position.setBackground(Color.red);
            position.addButton();
            position.setHistory(mainPanel);
            this.add(position, gbConstraints);
        }
        this.validate();
    }

    /**
     * Uses magic numbers to fit the DroneDynamics Latitude value to a screen coordinate
     * @param latitude the Latitude to modify
     * @param coordinate_factor = 1000000000 in every call, as the coordinates Latitude & Longitude are too similar between the Drones as to differentiate them otherwise.
     * @return an int fitting to a GridBagLayout position
     */
    private int modifyLatitude(double latitude, int coordinate_factor) {
        return (int) (((latitude * coordinate_factor) % 650) + 50) / 10;
    }
    /**
     * Uses magic numbers to fit the DroneDynamics Longitude value to a screen coordinate
     * @param longitude the Longitude to modify
     * @param coordinate_factor = 1000000000 in every call, as the coordinates Latitude & Longitude are too similar between the Drones as to differentiate them otherwise.
     * @return an int fitting to a GridBagLayout position
     */
    private int modifyLongitude(double longitude, int coordinate_factor) {
        return (int) (((longitude * coordinate_factor) % 600) + 100) / 10;
    }

    /**
     * @param droneDynamic
     * Creates a Position from droneDynamic by modifying Latitude and Longitude.
     * If the position is equal to an already existing Position it adds the DroneDynamics to said Position instead.
     */
    private void addDroneDynamic(DroneDynamics droneDynamic) {
        int coordinate_factor = 1000000000;
        int relativeLatitude = modifyLatitude(Double.parseDouble(droneDynamic.getLatitude()), coordinate_factor);
        int relativeLongitude = modifyLongitude(Double.parseDouble(droneDynamic.getLongitude()), coordinate_factor);

        Position position = new Position(relativeLongitude, relativeLatitude);
        if (dronePositions.contains(position)) {
            int index = dronePositions.indexOf(position);
            dronePositions.get(index).addDrone(droneDynamic.getDrone());
        } else {
            position.addDrone(droneDynamic.getDrone());
            dronePositions.add(position);
        }
    }
}
/**
 * This Class represents the Position of one or more drones.
 * It consists of a Button which opens either:
 * 1. The Popupmenu for showing the droneDynamics of the current Time (if the Position has a single Drone)
 * 2. The Popupmenu for showing all Drones located on this Position, which in turn redirects to Popup 1.
 * It overrides the equals and hashCode Method for usage of the contains method in the List of Position in the DrawnDronePanel.
 * @author Lennart Ochs
 */
class Position extends JPanel {
    private final Integer x;
    private final Integer y;
    private History history;
    List<Drones> dronesAtPosition = new ArrayList<>();

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
        setPreferredSize(new Dimension(40, 40));
    }

    public int getYCoordinate() {
        return y;
    }

    public int getXCoordinate() {
        return x;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position other = (Position) o;
        return (x.intValue() == other.x.intValue() && y.intValue() == other.y.intValue());
    }
    @Override
    public int hashCode() {
        String xHash = String.valueOf(x.hashCode());
        String yHash = String.valueOf(y.hashCode());
        return Integer.parseInt(xHash + yHash);
    }
    void setHistory(History history) {
        this.history = history;
    }
    void addDrone(Drones drone) {
        dronesAtPosition.add(drone);
    }


    void addButton() {
        JButton jbutton = new JButton(String.valueOf(dronesAtPosition.size()));
        jbutton.setBorder(new LineBorder(Color.BLACK));
        jbutton.addActionListener(e -> this.positionClicked());
        setTooltips(jbutton);
        this.add(jbutton);
    }

    /**
     * Please follow the comments in the implementation
     * @param droneDynamic the droneDynamic for which to show Data
     * @return a PopupMenu filled with all Data from droneDynamics, connects the Last Item of the Popupmenu to history to show the Dashboard of the selected Drone.
     */
    private PopupMenu droneDynamicToPopupMenu(DroneDynamics droneDynamic) {
        //Ignore: boring & bad
        List<String> droneInfoIdentifiers = new ArrayList<>();
        droneInfoIdentifiers.add("DroneUrl: ");
        droneInfoIdentifiers.add("Timestamp: ");
        droneInfoIdentifiers.add("Speed: ");
        droneInfoIdentifiers.add("Align Roll: ");
        droneInfoIdentifiers.add("Align Pitch: ");
        droneInfoIdentifiers.add("Align Yaw: ");
        droneInfoIdentifiers.add("Longitude: ");
        droneInfoIdentifiers.add("Latitude: ");
        droneInfoIdentifiers.add("Battery_status: ");
        droneInfoIdentifiers.add("Last_seen: ");
        droneInfoIdentifiers.add("Status: ");
        List<String> droneInfoValues = new ArrayList<>();
        droneInfoValues.add(String.valueOf(droneDynamic.getDroneUrl()));
        droneInfoValues.add(String.valueOf(droneDynamic.getTimeStamp()));
        droneInfoValues.add(String.valueOf(droneDynamic.getSpeed()));
        droneInfoValues.add(String.valueOf(droneDynamic.getAlignRoll()));
        droneInfoValues.add(String.valueOf(droneDynamic.getAlignPitch()));
        droneInfoValues.add(String.valueOf(droneDynamic.getAlignYaw()));
        droneInfoValues.add(String.valueOf(droneDynamic.getLongitude()));
        droneInfoValues.add(String.valueOf(droneDynamic.getLatitude()));
        droneInfoValues.add(String.valueOf(droneDynamic.getBatteryStatus()));
        droneInfoValues.add(String.valueOf(droneDynamic.getLastSeen()));
        droneInfoValues.add(String.valueOf(droneDynamic.getStatus()));
        //stop ignore if necessary

        PopupMenu popup = new PopupMenu();
        for (int i = 0; i < droneInfoIdentifiers.size(); i++) {
            MenuItem item = new MenuItem();
            item.setLabel(droneInfoIdentifiers.get(i) + droneInfoValues.get(i));
            popup.add(item);
        }
        MenuItem item = new MenuItem();
        item.setLabel("Click to show Dashboard of drone " + "(ID: " + droneDynamic.getDrone().getId() + ")");
        if (history != null) {
            item.addActionListener(e -> {
                history.getMainFrame().showDashboard(droneDynamic.getDrone());
                history.setComboxBoxToEmpty();
            });
            popup.add(item);
        }
        return popup;
    }

    /**
     * Shows a specifc Popup as generated by droneDynamicToPopupMenu
     * @param drone which drone was clicked at
     */
    private void showPopupForDrone(Drones drone) {
        DroneDynamics dynamics = history.getDroneDynamicsForDroneAtCurrentTime(drone.getId());
        if (dynamics != null) {
            PopupMenu popupMenu = droneDynamicToPopupMenu(dynamics);
            this.getParent().add(popupMenu);
            popupMenu.show(this, 0, 0);
        }
    }
    /**
     * Shows Popups 1 or 2 as described.
     * (Repetition)
     *  1. The Popupmenu for showing the droneDynamics of the current Time (if the Position has a single Drone)
     *  2. The Popupmenu for showing all Drones located on this Position, which in turn redirects to Popup 1.
     */
    private void positionClicked() {
        if (dronesAtPosition.size() > 1) {
            PopupMenu popup = new PopupMenu();
            for (Drones drone : dronesAtPosition) {
                MenuItem item = new MenuItem();
                item.setLabel("Show Dynamics of ID: " + drone.getId());
                item.addActionListener(e -> showPopupForDrone(drone));
                popup.add(item);
            }
            this.getParent().add(popup);
            popup.show(this, 10, 10);
        } else {
            showPopupForDrone(dronesAtPosition.getFirst());
        }
    }
    private void setTooltips(JButton button) {
        if (dronesAtPosition.size() == 1) {
            button.setToolTipText("Show here located Drone's dynamics (ID: " + dronesAtPosition.getFirst().getId() + ")");
        } else {
            StringBuilder dronesStr = new StringBuilder();
            for (Drones d : dronesAtPosition) {
                dronesStr.append(" ");
                dronesStr.append(d.getId()).append(";");
            }
            button.setToolTipText("Show here located Drone's dynamics (ID: \"" + dronesStr + ")");
        }
    }

}
