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

public class History extends JPanel implements UIPanel {
    private List<List<DroneDynamics>> droneDynamicsPerDrone = new ArrayList<>();
    //private final JPanel timeSliderPanel = new JPanel();
    private final JLabel timeLabel = new JLabel();
    private final JFormattedTextField timeInput = new JFormattedTextField();
    private JPanel comboBoxPanel = new JPanel();
    private mainFrame mainmainFrame;
    private DrawingPanel drawnDronePanel;
    private JSlider timeSlider;
    private JComboBox<Object> comboBox;
    private DataStorage dataStorage;
    private JPanel topPanel;
    void setComboxBoxToEmpty() {
        comboBox.setSelectedIndex(0);
        this.validate();
    }
    public History() {
        this.initialize();
    }

    public void initialize()
    {
        this.removeAll();
        /*if (timeSliderPanel != null)
            timeSliderPanel.removeAll();
        if (topPanel != null)
            topPanel.removeAll();*/
        this.setLayout(new BorderLayout());
        dataStorage = App.Main.getDataStorage();
        List<Drones> drones = dataStorage.getDronesList();
        for (Drones drone : drones) {
            droneDynamicsPerDrone.add(dataStorage.getDynamicsForDrone(drone.getId()));
        }

        int maxDroneDynamics = droneDynamicsPerDrone.getLast().size() - 1;
        createTopPanel();
        JPanel timeSliderPanel = createTimeSliderPanel(maxDroneDynamics, maxDroneDynamics);

        drawnDronePanel = getNewDrawingPanel(maxDroneDynamics, null);
        this.add(drawnDronePanel, BorderLayout.CENTER);
        this.add(timeSliderPanel, BorderLayout.SOUTH);
        this.validate();
    }

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

    public void setMainFrame(mainFrame mainFrame) {
        mainmainFrame = mainFrame;
    }

    public mainFrame getMainFrame() {
        return mainmainFrame;
    }

    private void createTopPanel() {
        topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        initDroneSelector();
        topPanel.add(comboBoxPanel, BorderLayout.WEST);
        refreshTimeLabel(0);
        topPanel.add(timeLabel, BorderLayout.EAST);
        this.add(topPanel, BorderLayout.NORTH);
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
    private DrawingPanel getNewDrawingPanel(int valueInTicks, Drones droneToMark) {
        List<DroneDynamics> dronesToDraw = new ArrayList<>();

        for (List<DroneDynamics> droneDynamic : droneDynamicsPerDrone) {
            dronesToDraw.add(droneDynamic.get(valueInTicks));
        }

        drawnDronePanel = new DrawingPanel(dronesToDraw, this, droneToMark);
        //this.add(drawnDronePanel, BorderLayout.CENTER);

        drawnDronePanel.setMaximumSize(new Dimension(400, 400));
        return drawnDronePanel;
    }
    private void initDroneSelector() {
        System.out.println("INitted ;Drone Comboxbox history");
        comboBoxPanel = new JPanel();
        comboBox = new JComboBox<>();
        comboBox.insertItemAt("None", 0);
        for (List<DroneDynamics> droneDynamics : droneDynamicsPerDrone) {
            comboBox.addItem(droneDynamics.getFirst().getDrone());
        }
        comboBox.setSelectedIndex(0);
        comboBox.addActionListener(e -> {
                    refreshDrawingPanel(timeSlider.getValue(), comboBox.getSelectedItem());
                    refreshTimeLabel(timeSlider.getValue());
                });
        comboBox.setRenderer(new droneDynamicsCellRenderer());
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
        comboBox.setRenderer(new droneDynamicsCellRenderer());
    }
    private void refreshDrawingPanel(int timeInTicks, Object drone) {
        this.remove(drawnDronePanel);
        if (!(drone instanceof Drones)) {
            List<DroneDynamics> dronesToDraw = new ArrayList<>();
            int valueInTicks = timeSlider.getValue();
            for (List<DroneDynamics> droneDynamic : droneDynamicsPerDrone) {
                dronesToDraw.add(droneDynamic.get(valueInTicks));
            }
            drawnDronePanel = new DrawingPanel(dronesToDraw, this, null);
            this.add(drawnDronePanel);
        }
        else
        {
            List<DroneDynamics> dronesToDraw = new ArrayList<>();
            int valueInTicks = timeSlider.getValue();
            for (List<DroneDynamics> droneDynamic : droneDynamicsPerDrone) {
                dronesToDraw.add(droneDynamic.get(valueInTicks));
            }
            drawnDronePanel = new DrawingPanel(dronesToDraw, this, (Drones) drone);
            this.add(drawnDronePanel);
        }
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
    DroneDynamics getDroneDynamicsForDroneAtCurrentTime(int id) {
        for (List<DroneDynamics> droneDynamics : droneDynamicsPerDrone) {
            if (droneDynamics.getFirst().getDrone().getId() == id)
                return droneDynamics.get(timeSlider.getValue());
        }
        return null;
    }
}

class droneDynamicsCellRenderer extends DefaultListCellRenderer {

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

class DrawingPanel extends JPanel {
    private List<Position> dronePositions = new ArrayList<>();

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
    public void refresh(List<DroneDynamics> dronesToDraw, History mainPanel, Drones droneToHighlight) {
        this.removeAll();
        dronePositions = new ArrayList<>();
        this.setLayout(new GridBagLayout());
        for (DroneDynamics droneDynamic : dronesToDraw) {
            addDroneDynamic(droneDynamic);
        }
        System.out.println("Drone Positions = " + dronePositions.size());
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
    }

    private int modifyLatitude(double latitude, int coordinate_factor) {
        return (int) (((latitude * coordinate_factor) % 650) + 50) / 10;
    }

    private int modifyLongitude(double longitude, int coordinate_factor) {
        return (int) (((longitude * coordinate_factor) % 600) + 100) / 10;
    }

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

    void showPopupForDrone(Drones drone) {
        DroneDynamics dynamics = history.getDroneDynamicsForDroneAtCurrentTime(drone.getId());
        if (dynamics != null) {
            PopupMenu popupMenu = droneDynamicToPopupMenu(dynamics);
            this.getParent().add(popupMenu);
            popupMenu.show(this, 0, 0);
        }
    }

    void addButton() {
        JButton jbutton = new JButton(String.valueOf(dronesAtPosition.size()));
        jbutton.setBorder(new LineBorder(Color.BLACK));
        jbutton.addActionListener(e -> this.positionClicked());
        setTooltips(jbutton);
        this.add(jbutton);
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

    private PopupMenu droneDynamicToPopupMenu(DroneDynamics droneDynamic) {
        //ignore while(true)
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
        droneInfoValues.add(String.valueOf(droneDynamic.getAlign_yaw()));
        droneInfoValues.add(String.valueOf(droneDynamic.getLongitude()));
        droneInfoValues.add(String.valueOf(droneDynamic.getLatitude()));
        droneInfoValues.add(String.valueOf(droneDynamic.getBatteryStatus()));
        droneInfoValues.add(String.valueOf(droneDynamic.getLastSeen()));
        droneInfoValues.add(String.valueOf(droneDynamic.getStatus()));
        //break ignore;
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
}
