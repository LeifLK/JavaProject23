package App.UI;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
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


public class dashboard extends JPanel {

    private JPanel dashboard = new JPanel();
    private JPanel title_ComboBox = new JPanel();
    private JPanel drone_info = new JPanel();
    private JPanel info = new JPanel();
    private JPanel bar_chart = new JPanel();
    private JPanel pie_chart = new JPanel();
    private JPanel line_chart = new JPanel();
    private JLabel Title = new JLabel("Dashboard");
    private JLabel typename = new JLabel("Type:");
    private JLabel serialnumber = new JLabel("Serialnumber");
    private JLabel alignRoll = new JLabel("roll:");
    private JLabel alignPitch = new JLabel("pitch:");
    private JLabel alignYaw = new JLabel("yaw:");

    private JComboBox<String> comboBox;
    int currentDroneId = Main.getDataStorage().getDronesList().getFirst().getId();

    public JPanel getJPanel() {
        dashboard.validate();
        return dashboard;
    }

    public dashboard() {
        createDashboard();
    }

    private static void deleteAllChildren(Container container) {
        Component[] components = container.getComponents();
        for (Component component : components) {
            if (component instanceof Container) {
                // If the component is a container, call the method recursively
                deleteAllChildren((Container) component);
            }

            // Remove the component from its parent
            container.remove(component);
        }

        // Repaint the container after removing all children
        container.revalidate();
        container.repaint();
    }

    public void createDashboard() {

        deleteAllChildren(dashboard);
        DataStorage dataStorage = Main.getDataStorage();
        //JLabels
        Title.setBounds(0, 0, 300, 50);
        Title.setHorizontalAlignment(JLabel.CENTER);
        Title.setVerticalAlignment(JLabel.TOP);
        Title.setFont(new Font(null, Font.PLAIN, 25));

        int lowestDroneId = 71;
        typename = createLabel("Typename: " + dataStorage.getDronesList().get(currentDroneId - lowestDroneId).getDronetype().getTypename(), 0, 20);
        serialnumber = createLabel("Serialnumber: " + dataStorage.getDronesList().get(currentDroneId - lowestDroneId).getSerialnumber(), 0, 40);
        alignRoll = createLabel("AlignRoll:  " + dataStorage.getDroneDynamicsList().get(currentDroneId - lowestDroneId).getAlign_roll(), 0, 60);
        alignPitch.setText("AlignPitch: " + dataStorage.getDronesList().get(currentDroneId - lowestDroneId).getSerialnumber());
        alignYaw.setText("AlignYaw: " + dataStorage.getDronesList().get(currentDroneId - lowestDroneId).getSerialnumber());


        title_ComboBox.setBounds(0, 0, 345, 80);
        title_ComboBox.setLayout(new BorderLayout());
        title_ComboBox.setBackground(Color.LIGHT_GRAY);

        drone_info.setBounds(0, 80, 345, 310);
        drone_info.setLayout(new GridLayout(5, 1));
        drone_info.setBackground(Color.LIGHT_GRAY);


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
        List<String> droneIds = new ArrayList<>();
        for (Drones drone : dataStorage.getDronesList()) {
            droneIds.add("DroneId: " + drone.getId());
        }

        Vector<String> vector = new Vector<>(droneIds);
        comboBox = new JComboBox<>(vector);
        comboBox.setBackground(Color.CYAN);
        comboBox.setSize(90, 30);
        comboBox.setAlignmentX(250);
        comboBox.setAlignmentY(0);
        comboBox.setSelectedIndex(currentDroneId-lowestDroneId);
        comboBox.addActionListener(e -> reloadPanel(comboBox.getSelectedItem()));


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
        title_ComboBox.add(comboBox).setLocation(250, 10);
        title_ComboBox.add(Title, BorderLayout.CENTER);
        title_ComboBox.setLayout(new BorderLayout());
        info.add(title_ComboBox);
        drone_info.add(typename);
        drone_info.add(serialnumber);
        drone_info.add(alignRoll);
        drone_info.add(alignPitch);
        drone_info.add(alignYaw);
        info.add(drone_info);
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
        dashboard.setVisible(true);
        dashboard.validate();

    }

    public void reloadPanel(Object value) {
        if (value != null) {
            String valueStr = (String) value;
            currentDroneId = Integer.parseInt(valueStr.replace("DroneId: ", ""));
            createDashboard();

        }

    }

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
        double max_speed = dataList.getLast().getDrone().getDronetype().getMax_speed();
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

        int battery = dataList.getLast().getBattery_status();
        dataset.setValue("charged Battery %", battery);
        dataset.setValue("drained Battery %", 100);


        return dataset;
    }

    private static JFreeChart createLineChart(int DroneId) {
        CategoryDataset linedataset = createDatasetforLine(DroneId);

        JFreeChart lineChart = ChartFactory.createLineChart(
                "Speed Chart",
                "Time",
                "Speed km/h",
                linedataset
        );
        //lineChart.addLegend("Average speed: " + averageSpeed);

        return lineChart;


    }

    private static CategoryDataset createDatasetforLine(int DroneId) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        DataStorage dataStorage = Main.getDataStorage();

        double total = 0;

        List<DroneDynamics> dataList = dataStorage.getDynamicsForDrone(DroneId);


        for (int i = 0; i < 100; i++) {
            double speed = dataList.get(dataList.size() - 1 - i).getSpeed();
            if (speed > 0) {

                String category = dataList.get(dataList.size() - 1 - i).getTimestamp();
                dataset.addValue(speed, "Speed", category);
                total += speed;

            }

        }
        double averageSpeed = total / 100;
        return dataset;

    }

}

