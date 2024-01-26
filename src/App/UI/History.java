package App.UI;


import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

import App.Model.DroneDynamics;
import App.Model.Drones;
import App.Services.DataStorage;


public class History extends JPanel{
    static DataStorage dataStorage;
    List<List<DroneDynamics>> droneDynamicsPerDrone = new ArrayList<>();
    public History() {
        //initComponents();
        this.setLayout(new BorderLayout());
        dataStorage = App.Main.getDataStorage();
        List<Drones> drones = dataStorage.getDronesList();
        DrawingPanel drawnDrone = new DrawingPanel();
        for (Drones drone: drones)
        {
            droneDynamicsPerDrone.add(dataStorage.getDynamicsForDrone(drone.getId()));
            drawnDrone.addDrone(droneDynamicsPerDrone.getLast().get(0));
        }
        this.add(drawnDrone);
    }
}
class DrawingPanel extends JPanel {
    List<Color> differentColors = new ArrayList<>();
    int colorIndex = 0;
    Map<Coordinate, Integer> droneCoordinates = new HashMap<>();

    DrawingPanel() {
        setPreferredSize(new Dimension(700, 800));
        differentColors.add(Color.decode("#FF0000"));
        differentColors.add(Color.decode("#E60000"));
        differentColors.add(Color.decode("#CC0000"));
        differentColors.add(Color.decode("#b30000"));
        differentColors.add(Color.decode("#990000"));
    }
    private int modifyLatitude(double latitude, int coordinate_factor)
    {
        return (int) ((latitude * coordinate_factor) % 650) + 50;
    }
    private int modifyLongitude(double longitude, int coordinate_factor)
    {
        return (int) ((longitude * coordinate_factor) % 700) + 100;
    }

    public void addDrone(DroneDynamics droneDynamics)
    {
        int coordinate_factor = 1000000000;
        int relativeLatitude = modifyLatitude(Double.parseDouble(droneDynamics.getLatitude()), coordinate_factor);
        int relativeLongitude = modifyLongitude(Double.parseDouble(droneDynamics.getLongitude()), coordinate_factor);
        Coordinate coordinate = new Coordinate(relativeLongitude, relativeLatitude);

        if (droneCoordinates.size() == 0)
        {
            droneCoordinates.put(coordinate, 1);
        }
        if (droneCoordinates.containsKey(coordinate)) {
            droneCoordinates.put(coordinate, droneCoordinates.get(coordinate) + 1);
        }
        else
            droneCoordinates.put(coordinate, 1);
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Coordinate coordinate : droneCoordinates.keySet())
        {
            //System.out.println(droneCoordinates.get(i).x);
            //System.out.println(droneCoordinates.get(i).y);
            paint(g, coordinate.x, coordinate.y, droneCoordinates.get(coordinate));
        }
    }
    public void paint(Graphics g, int x, int y, int count)
    {
        //g.drawRect(x, y, 100, 100);
        g.setColor(differentColors.get(colorIndex));
        colorIndex++;
        if (colorIndex >= differentColors.size())
            colorIndex=0;

        g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        //-5 and +2 for visual offset
        g.drawString(String.valueOf(count), x-5, y+2);
        g.fillRect(x, y, 5, 5);
    }
}
class Coordinate {
    Integer x;
    Integer y;
    public Coordinate(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate other = (Coordinate) o;
        return (x.intValue() == other.x.intValue() && y.intValue() == other.y.intValue());
    }

    @Override
    public int hashCode()
    {
        String xHash = String.valueOf(x.hashCode());
        String yHash = String.valueOf(y.hashCode());
        return Integer.parseInt(xHash + yHash);
    }
}
