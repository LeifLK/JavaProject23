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
    List<List<DroneDynamics>> droneDynamicsPerDrone = new ArrayList<>();

    JPanel timeSliderPanel = new JPanel();
    int maxDroneDynamics;

    public myframe mainframe;

    public void setFrame(myframe myframe) {
        mainframe = myframe;
    }

    public History() {
        //initComponents();
        //this.setLayout(new BorderLayout());
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(400, 400));
        this.setMaximumSize(new Dimension(400, 400));
        dataStorage = App.Main.getDataStorage();
        List<Drones> drones = dataStorage.getDronesList();

        for (Drones drone : drones) {
            droneDynamicsPerDrone.add(dataStorage.getDynamicsForDrone(drone.getId()));

        }
        maxDroneDynamics = droneDynamicsPerDrone.getLast().size() - 1;

        drawAllDronesAtTime(maxDroneDynamics);
        JLabel timeLabel = new JLabel(droneDynamicsPerDrone.get(0).get(maxDroneDynamics).getTimeStamp());
        this.add(timeLabel, BorderLayout.PAGE_START);
        this.add(initTimeSliderPanel(maxDroneDynamics, maxDroneDynamics), BorderLayout.SOUTH);
    }

    private JPanel initTimeSliderPanel(int max, int currentTick) {
        JSlider timeSlider = new JSlider(0, max, currentTick);
        timeSlider.setMaximumSize(new Dimension(250, 100));
        timeSlider.setPaintTicks(true);
        timeSlider.setMajorTickSpacing(10);
        timeSlider.setPreferredSize(new Dimension(250, 100));
        timeSlider.addChangeListener(e -> drawAllDronesAtTime(timeSlider.getValue()));

        timeSliderPanel.setLayout(new BoxLayout(timeSliderPanel, BoxLayout.PAGE_AXIS));
        timeSliderPanel.add(timeSlider);
        timeSlider.setToolTipText("Select Timeslot");
        return timeSliderPanel;
    }

    //TODO: Fix if single drone is selected
    public void drawAllDronesAtTime(int valueInTicks) {
        this.removeAll();
        List<DroneDynamics> dronesToDraw = new ArrayList<>();

        for (List<DroneDynamics> droneDynamic : droneDynamicsPerDrone) {
            dronesToDraw.add(droneDynamic.get(valueInTicks));
        }
        DrawingPanel drawnDronePanel = new DrawingPanel(dronesToDraw);

        JLabel timeLabel = new JLabel(droneDynamicsPerDrone.get(0).get(valueInTicks).getTimeStamp());
        this.add(timeLabel, BorderLayout.PAGE_START);
        this.add(drawnDronePanel);
        this.add(timeSliderPanel, BorderLayout.SOUTH);
        for (Component component : drawnDronePanel.getComponents()) {
            if (component instanceof Position pos) {
                pos.setHistory(this);
            }
        }
        this.validate();
    }
}

class DrawingPanel extends JPanel {
    List<Position> dronePositions = new ArrayList<>();

    DrawingPanel(List<DroneDynamics> dronesToDraw) {
        this.setLayout(new GridBagLayout());
        for (DroneDynamics droneDynamic : dronesToDraw) {
            addDroneDynamic(droneDynamic);
        }
        setPreferredSize(new Dimension(600, 700));
        for (Position position : dronePositions) {
            GridBagConstraints gbConstraints = new GridBagConstraints();
            gbConstraints.gridx = position.y;
            gbConstraints.gridy = position.x;
            position.addButton();
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
            dronePositions.get(index).amountAtCurrentPosition += 1;
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
    int amountAtCurrentPosition = 1;
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
            button.setToolTipText("Show here located Drone (ID: " + dronesAtPosition.getFirst().getId() + ")");
        } else {
            StringBuilder dronesStr = new StringBuilder();
            for (Drones d : dronesAtPosition) {
                dronesStr.append(" ");
                dronesStr.append(d.getId()).append(";");
            }
            button.setToolTipText("Show here located Drones (IDs:" + dronesStr + ")");
        }
    }

    private void positionClicked() {
        if (amountAtCurrentPosition > 1) {
            PopupMenu popup = new PopupMenu();
            for (Drones drone : dronesAtPosition) {
                MenuItem item = new MenuItem();
                item.setLabel("ID: " + drone.getId());
                if (history != null)
                    item.addActionListener(e -> history.mainframe.loadDashboardAt(drone.getId()));
                popup.add(item);
            }
            this.getParent().add(popup);
            popup.show(this, 10, 10);
        } else
            history.mainframe.loadDashboardAt(dronesAtPosition.get(0).getId());
    }

    public void addButton() {
        JButton jbutton = new JButton(String.valueOf(amountAtCurrentPosition));
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
