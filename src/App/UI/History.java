package App.UI;


import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.*;
import java.util.List;

import App.Model.DroneDynamics;
import App.Model.Drones;
import App.Services.DataStorage;


public class History extends JPanel {
    static DataStorage dataStorage;
    public List<List<DroneDynamics>> droneDynamicsPerDrone = new ArrayList<>();

    JPanel timeSliderPanel = new JPanel();
    int maxDroneDynamics;

    public myframe mainframe;
    private JPanel drawnDronePanel;
    private JSlider timeSlider;
    private JComboBox<Object> comboBox;
    private final JLabel timeLabel = new JLabel();

    public void setFrame(myframe myframe) {
        mainframe = myframe;
    }

    public DroneDynamics getDroneDynamicsForDroneAtCurrentTime(int id)
    {
        for (List<DroneDynamics> droneDynamics : droneDynamicsPerDrone)
        {
            if (droneDynamics.getFirst().getDrone().getId() == id)
                return droneDynamics.get(timeSlider.getValue());
        }
        return null;
    }
    private JPanel initTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        initDroneSelector();
        topPanel.add(comboBoxPanel, BorderLayout.WEST);
        refreshTimeLabel(0);
        topPanel.add(timeLabel, BorderLayout.EAST);
        return topPanel;
    }

    private void initDroneSelector() {
        comboBox = new JComboBox<>();
        comboBox.insertItemAt("None", 0);
        for (List<DroneDynamics> droneDynamics : droneDynamicsPerDrone) {
            comboBox.addItem(droneDynamics.getFirst().getDrone());
        }
        comboBox.setSelectedIndex(0);
        comboBox.addActionListener(e -> {
            refreshDrawingPanel(timeSlider.getValue(), comboBox.getSelectedItem());
            System.out.println("Clicked on ComboboxItem");
            refreshTimeLabel(timeSlider.getValue());
        });
        comboBox.setRenderer(new droneDynamicsCellRenderer());
        comboBoxPanel.add(comboBox);
    }
    public void resetDroneSelection()
    {
        comboBox.setSelectedIndex(0);
    }
    public History() {
        this.setLayout(new BorderLayout());
        //this.setPreferredSize(new Dimension(600, 600));
        dataStorage = App.Main.getDataStorage();
        List<Drones> drones = dataStorage.getDronesList();
        for (Drones drone : drones) {
            droneDynamicsPerDrone.add(dataStorage.getDynamicsForDrone(drone.getId()));

        }
        maxDroneDynamics = droneDynamicsPerDrone.getLast().size() - 1;
        this.add(initTopPanel(), BorderLayout.NORTH);

        initTimeSliderPanel(maxDroneDynamics, maxDroneDynamics);
        initDroneDrawingPanel(maxDroneDynamics);


        this.add(drawnDronePanel, BorderLayout.CENTER);
        this.add(timeSliderPanel, BorderLayout.SOUTH);
        this.validate();
        //this.repaint();
    }

    private void refreshDrawingPanel(int timeInTicks) {
        this.remove(drawnDronePanel);
        initDroneDrawingPanel(timeInTicks);
        this.add(drawnDronePanel, BorderLayout.CENTER);
        this.validate();
    }

    private void refreshDrawingPanel(int timeInTicks, Object drone) {
        if (!(drone instanceof Drones)) {
            refreshDrawingPanel(timeInTicks);
            return;
        }
        this.remove(drawnDronePanel);
        initDroneDrawingPanel(timeInTicks, (Drones) drone);
        this.add(drawnDronePanel, BorderLayout.CENTER);
        this.validate();
    }
    private void refreshTimeLabel(int timeInTicks) {
        timeLabel.setText(droneDynamicsPerDrone.get(0).get(timeInTicks).getTimeStamp());
        this.validate();
    }

    private final JPanel comboBoxPanel = new JPanel();

    private void initTimeSliderPanel(int max, int currentTick) {

        timeSliderPanel.setLayout(new GridBagLayout());
        //timeSliderPanel.setBorder(BorderFactory.createEmptyBorder());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.05;
        JLabel label = new JLabel("Valid Ticks between 0 - " + max);
        timeSliderPanel.add(label, gbc);

        gbc.gridx++;
        gbc.weightx = 0.9;
        timeSlider = new JSlider(0, max, currentTick);
        timeSlider.addChangeListener(e -> reactToTimeSliderChange());
        timeSlider.setMajorTickSpacing(500);
        timeSlider.setMinorTickSpacing(100);
        timeSlider.setPaintLabels(true);
        timeSlider.setPaintTicks(true);
        timeSliderPanel.add(timeSlider, gbc);

        gbc.gridx++;
        gbc.weightx = 0.05;

        JFormattedTextField timeInput = new JFormattedTextField();
        timeInput.setValue(max);
        timeInput.addPropertyChangeListener("value", e -> inputTick((Integer) timeInput.getValue()));
        timeSliderPanel.setPreferredSize(new Dimension(400, 120));
        timeSliderPanel.setMaximumSize(new Dimension(550, 120));
        timeSliderPanel.add(timeInput, gbc);
    }

    private void inputTick(int value)
    {
        if (value < timeSlider.getMaximum())
            timeSlider.setValue(value);
    }

    private void reactToTimeSliderChange()
    {
        if (comboBox.getSelectedIndex() == 0) {
            refreshDrawingPanel(timeSlider.getValue());
            refreshTimeLabel(timeSlider.getValue());
        } else {
            refreshDrawingPanel(timeSlider.getValue(), comboBox.getSelectedItem());
            refreshTimeLabel(timeSlider.getValue());
        }
    }
    //TODO: Fix if single drone is selected
    public void initDroneDrawingPanel(int valueInTicks) {
        List<DroneDynamics> dronesToDraw = new ArrayList<>();

        for (List<DroneDynamics> droneDynamic : droneDynamicsPerDrone) {
            dronesToDraw.add(droneDynamic.get(valueInTicks));
        }
        drawnDronePanel = new DrawingPanel(dronesToDraw, this);
        //this.add(drawnDronePanel, BorderLayout.CENTER);

        drawnDronePanel.setMaximumSize(new Dimension(400, 400));
        this.validate();
    }

    public void initDroneDrawingPanel(int valueInTicks, Drones droneToMark) {
        List<DroneDynamics> dronesToDraw = new ArrayList<>();

        for (List<DroneDynamics> droneDynamic : droneDynamicsPerDrone) {
            dronesToDraw.add(droneDynamic.get(valueInTicks));
        }
        drawnDronePanel = new DrawingPanel(dronesToDraw, this, droneToMark);
        this.add(drawnDronePanel, BorderLayout.CENTER);

        drawnDronePanel.setMaximumSize(new Dimension(400, 400));
        this.validate();
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
    List<Position> dronePositions = new ArrayList<>();

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
            gbConstraints.gridx = position.y;
            gbConstraints.gridy = position.x;
            if (position.dronesAtPosition.contains(droneToHighlight))
                position.setBackground(Color.red);
            position.addButton();
            position.setHistory(mainPanel);
            this.add(position, gbConstraints);
        }
        this.validate();
    }

    DrawingPanel(List<DroneDynamics> dronesToDraw, History mainPanel) {
        this.removeAll();
        this.setLayout(new GridBagLayout());
        for (DroneDynamics droneDynamic : dronesToDraw) {
            addDroneDynamic(droneDynamic);
        }
        setPreferredSize(new Dimension(500, 500));
        setMaximumSize(new Dimension(500, 500));
        for (Position position : dronePositions) {
            GridBagConstraints gbConstraints = new GridBagConstraints();
            gbConstraints.gridx = position.y;
            gbConstraints.gridy = position.x;
            position.addButton();
            position.setHistory(mainPanel);
            this.add(position, gbConstraints);
        }
        this.validate();
    }

    private int modifyLatitude(double latitude, int coordinate_factor) {
        return (int) (((latitude * coordinate_factor) % 650) + 50) / 10;
    }

    private int modifyLongitude(double longitude, int coordinate_factor) {
        return (int) (((longitude * coordinate_factor) % 600) + 100) / 10;
    }

    public void addDroneDynamic(DroneDynamics droneDynamics) {
        int coordinate_factor = 1000000000;
        int relativeLatitude = modifyLatitude(Double.parseDouble(droneDynamics.getLatitude()), coordinate_factor);
        int relativeLongitude = modifyLongitude(Double.parseDouble(droneDynamics.getLongitude()), coordinate_factor);

        Position position = new Position(relativeLongitude, relativeLatitude);
        if (dronePositions.contains(position)) {
            int index = dronePositions.indexOf(position);
            dronePositions.get(index).addDrone(droneDynamics.getDrone());
        } else {
            position.addDrone(droneDynamics.getDrone());
            dronePositions.add(position);
        }
    }
}

class Position extends JPanel {
    Integer x;
    Integer y;
    History history;

    void setHistory(History history) {
        this.history = history;
    }

    List<Drones> dronesAtPosition = new ArrayList<>();

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
        setPreferredSize(new Dimension(40, 40));
    }

    public void addDrone(Drones drone) {
        dronesAtPosition.add(drone);
    }

    private void setTooltips(JButton button) {
        if (dronesAtPosition.size() == 1) {
            button.setToolTipText("Show here located Drone (ID: " + dronesAtPosition.getFirst().getId() + ") and its dynamics");
        } else {
            StringBuilder dronesStr = new StringBuilder();
            for (Drones d : dronesAtPosition) {
                dronesStr.append(" ");
                dronesStr.append(d.getId()).append(";");
            }
            button.setToolTipText("Show here located Drones (IDs:" + dronesStr + ") and its dynamics");
        }
    }
    //Please Ignore
    private PopupMenu droneDynamicToPopupMenu(DroneDynamics droneDynamic)
    {

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
        PopupMenu popup = new PopupMenu();
        for (int i = 0; i < droneInfoIdentifiers.size(); i++) {
            MenuItem item = new MenuItem();
            item.setLabel(droneInfoIdentifiers.get(i) + droneInfoValues.get(i));
            popup.add(item);
        }
        MenuItem item = new MenuItem();
        item.setLabel("Click to show Dashboard of drone " + "(ID: " + droneDynamic.getDrone().getId()+ ")");
        if (history != null) {
            item.addActionListener(e -> history.mainframe.loadDashboardAt(droneDynamic.getDrone().getId()));
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
        } else
        {
            showPopupForDrone(dronesAtPosition.getFirst());
        }
    }
    //history.mainframe.loadDashboardAt(dronesAtPosition.get(0).getId());
    public void showPopupForDrone(Drones drone)
    {
        DroneDynamics dynamics = history.getDroneDynamicsForDroneAtCurrentTime(drone.getId());
        if (dynamics != null)
        {
            PopupMenu popupMenu = droneDynamicToPopupMenu(dynamics);
            this.getParent().add(popupMenu);
            popupMenu.show(this,0,0);
        }
    }

    public void addButton() {
        JButton jbutton = new JButton(String.valueOf(dronesAtPosition.size()));
        jbutton.setBorder(new LineBorder(Color.BLACK));
        jbutton.addActionListener(e -> this.positionClicked());
        setTooltips(jbutton);
        this.add(jbutton);
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
}
