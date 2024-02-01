package App.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JLabel;

import App.Main;
import App.Model.DroneDynamics;
import App.Model.Drones;
import App.Services.DataStorage;
import org.jfree.chart.*;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

//TODO: Rename dashboard and mainframe to use capitalized classnames
public class dashboard extends JPanel implements UIPanel{

    private final JPanel dashboard = new JPanel();
    private final JPanel titleComboBox = new JPanel();
    private final JPanel droneInfo = new JPanel();
    private final JPanel info = new JPanel();
    private final JPanel bar_chart = new JPanel();
    private final JPanel pie_chart = new JPanel();
    private final JPanel line_chart = new JPanel();
    private final JLabel title = new JLabel("Dashboard");
    private JComboBox comboBox = new JComboBox<>();
    private DataStorage dataStorage;
    int currentDroneId = Main.getDataStorage().getDronesList().getFirst().getId();
    private int lowestDroneId = 71;
    private ActionListener actionListener;

    public JPanel getJPanel() {
        dashboard.validate();
        return dashboard;
    }
    public dashboard() {
        initialize();
    }
    private static void deleteAllChildren(Container container) {
        Component[] components = container.getComponents();
        for (Component component : components) {
            if (component instanceof Container) {
                deleteAllChildren((Container) component);
            }

            container.remove(component);
        }

        container.revalidate();
        container.repaint();
    }
    public void initialize() {

        deleteAllChildren(dashboard);
        dataStorage = Main.getDataStorage();

        //JLabels
        title.setBounds(0, 0, 300, 50);
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setVerticalAlignment(JLabel.TOP);
        title.setFont(new Font(null, Font.PLAIN, 25));


        JLabel manufacturer = createLabel("Manufacturer: " + dataStorage.getDronesList().get(currentDroneId - lowestDroneId).getDronetype().getManufacturer(), 0, 0);
        JLabel typename = createLabel("Typename: " + dataStorage.getDronesList().get(currentDroneId - lowestDroneId).getDronetype().getTypename(), 0, 15);
        JLabel serialnumber = createLabel("Serialnumber: " + dataStorage.getDronesList().get(currentDroneId - lowestDroneId).getSerialnumber(), 0, 30);
        JLabel carriageWeight = createLabel("Carriage Weight:  " + dataStorage.getDronesList().get(currentDroneId - lowestDroneId).getCarriageWeight(), 0, 45);
        JLabel carriageType = createLabel("Carriage Type: " + dataStorage.getDronesList().get(currentDroneId - lowestDroneId).getCarriageType(), 0, 60);
        JLabel createdDate = createLabel("Created: " + dataStorage.getDronesList().get(currentDroneId - lowestDroneId).getCreated(), 0, 75);
        JLabel averageSpeedLabel = createLabel("AverageSpeed in km/h: " + computeAverageSpeed(currentDroneId), 0, 90);

        //JPanels
        titleComboBox.setBounds(0, 0, 345, 80);
        titleComboBox.setLayout(new BorderLayout());
        titleComboBox.setBackground(Color.LIGHT_GRAY);

        droneInfo.setBounds(0, 80, 345, 310);
        droneInfo.setLayout(new GridLayout(7, 1));
        droneInfo.setBackground(Color.LIGHT_GRAY);

        info.setBounds(0, 0, 345, 390);
        info.setLayout(null);
        info.setBackground(Color.LIGHT_GRAY);

        bar_chart.setBounds(0, 0, 100, 100);
        bar_chart.setBackground(Color.LIGHT_GRAY);


        pie_chart.setBounds(0, 0, 100, 100);
        pie_chart.setBackground(Color.LIGHT_GRAY);

        line_chart.setBounds(0, 0, 100, 100);
        line_chart.setBackground(Color.LIGHT_GRAY);

        //JComboBox
        comboBox.setBackground(Color.CYAN);
        comboBox.setSize(90, 30);
        comboBox.setAlignmentX(250);
        comboBox.setAlignmentY(0);
        if (comboBox.getItemCount() == 0) {
            comboBox.setRenderer(new droneCellRenderer());

            for (Drones d : dataStorage.getDronesList()) {
                comboBox.addItem(d);
            }
            comboBox.addActionListener(e -> reloadPanel(comboBox.getSelectedItem()));
        }

        //Find Renderer in History.java
        //refreshComboBox();
        //comboBox.setSelectedIndex(currentDroneId - lowestDroneId);



        //JFreeChart
        JFreeChart barChart = createBarChart(currentDroneId);
        ChartPanel barPanel = new ChartPanel(barChart);
        barPanel.setPreferredSize(new Dimension(325, 385));

        JFreeChart pieChart = createPieChart(currentDroneId);
        ChartPanel piePanel = new ChartPanel(pieChart);
        piePanel.setPreferredSize(new Dimension(340, 345));

        JFreeChart lineChart = createLineChart(currentDroneId);
        ChartPanel linePanel = new ChartPanel(lineChart);
        linePanel.setPreferredSize(new Dimension(315, 350));

        // Dashboard
        dashboard.setSize(700, 800);
        titleComboBox.add(comboBox).setLocation(250, 10);
        titleComboBox.add(title, BorderLayout.CENTER);
        titleComboBox.setLayout(new BorderLayout());
        info.add(titleComboBox);
        droneInfo.add(manufacturer);
        droneInfo.add(typename);
        droneInfo.add(serialnumber);
        droneInfo.add(carriageWeight);
        droneInfo.add(carriageType);
        droneInfo.add(createdDate);
        droneInfo.add(averageSpeedLabel);
        info.add(droneInfo);
        dashboard.add(info);
        bar_chart.add(barPanel);
        dashboard.add(bar_chart);
        pie_chart.add(piePanel);
        dashboard.add(pie_chart);
        line_chart.add(linePanel);
        dashboard.add(line_chart);
        dashboard.setLayout(new GridLayout(2, 2, 10, 10));
        dashboard.setBackground(Color.DARK_GRAY);
        dashboard.setForeground(Color.BLUE);
    }
    @Override
    public void refreshData() {
        this.dataStorage = Main.getDataStorage();
        //this.refreshComboBox();
        Object before = comboBox.getSelectedItem();
        comboBox.removeAllItems();
        for (Drones d : dataStorage.getDronesList()) {
            comboBox.addItem(d);
        }
        //comboBox.addActionListener(e -> reloadPanel(comboBox.getSelectedItem()));
        comboBox.setSelectedItem(before);
    }

    public void reloadPanel(Object value)
    {
        if (value != null) {
            currentDroneId = ((Drones)comboBox.getSelectedItem()).getId();
            initialize();
        }
    }
    /*public void reloadPanelFromHistory(Object value) {
        if (value != null) {
            comboBox.setSelectedItem(value);
        }
    }
*/
    public static JLabel createLabel(String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, 200, 50);
        label.setFont(new Font(null, Font.PLAIN, 12));
        return label;
    }

    private static JFreeChart createBarChart(int DroneId) {
        CategoryDataset dataset = createDatasetforBar(DroneId);


        JFreeChart barChart = ChartFactory.createBarChart(
                "Speed ",
                "Time",
                "Speed in km/h",
                dataset);
        CategoryPlot plot = (CategoryPlot) barChart.getPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        plot.setBackgroundPaint(Color.white);
        plot.setRangeGridlinePaint(Color.lightGray);

        renderer.setMaximumBarWidth(0.2);
        renderer.setSeriesPaint(0, Color.ORANGE);

        return barChart;
    }

    private static CategoryDataset createDatasetforBar(int DroneId) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        DataStorage dataStorage = Main.getDataStorage();


        List<DroneDynamics> dataList = dataStorage.getDynamicsForDrone(DroneId);


        double speed = dataList.getLast().getSpeed();
        double max_speed = dataList.getLast().getDrone().getDronetype().getMaxSpeed();
        String category2 = "Max Speed";
        String category = "Current Speed";
        dataset.addValue(speed, "Speed", category);
        dataset.addValue(max_speed, "Speed", category2);

        return dataset;
    }

    private static JFreeChart createPieChart(int DroneId) {
        PieDataset piedataset = createDatasetforPie(DroneId);
        JFreeChart pieChart = ChartFactory.createPieChart(
                "Battery Status",
                piedataset);

        PiePlot plot = (PiePlot) pieChart.getPlot();
        plot.setBackgroundPaint(Color.white);


        plot.setSectionPaint("charged Battery %", Color.GREEN);
        plot.setSectionPaint("drained Battery %", Color.RED);


        return pieChart;
    }

    private static PieDataset createDatasetforPie(int DroneId) {

        DefaultPieDataset dataset = new DefaultPieDataset();
        DataStorage dataStorage = Main.getDataStorage();


        List<DroneDynamics> dataList = dataStorage.getDynamicsForDrone(DroneId);

        int battery = dataList.getLast().getBatteryStatus();
        dataset.setValue("charged Battery %", battery);
        dataset.setValue("drained Battery %", 100);


        return dataset;
    }

    private static JFreeChart createLineChart(int DroneId) {
        CategoryDataset linedataset = createDatasetforLine(DroneId);

        return ChartFactory.createLineChart(
                "Speed Chart",
                "Time",
                "Speed km/h",
                linedataset
        );

    }

    private static CategoryDataset createDatasetforLine(int DroneId) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        DataStorage dataStorage = Main.getDataStorage();
        List<DroneDynamics> dataList = dataStorage.getDynamicsForDrone(DroneId);

        for (int i = 0; i < 100; i++) {
            double speed = dataList.get(dataList.size() - 1 - i).getSpeed();
            if (speed > 0) {
                String category = dataList.get(dataList.size() - 1 - i).getTimeStamp();
                dataset.addValue(speed, "Speed", category);
            }
        }
        return dataset;
    }
    private double computeAverageSpeed(int DroneId) {

        DataStorage dataStorage = Main.getDataStorage();
        double total = 0;
        int counter = 0;
        List<DroneDynamics> dataList = dataStorage.getDynamicsForDrone(DroneId);

        for (int i = 0; i < 100 && i < dataList.size(); i++) {
            double speed = dataList.get(dataList.size() - 1 - i).getSpeed();
            if (speed > 0) {
                total += speed;
                counter++;
            }
        }
        if (counter > 0) {
            return total / counter;
        } else {
            return 0;
        }
    }
}
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

