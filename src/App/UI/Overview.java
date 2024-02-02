package App.UI;

import App.Main;
import App.Model.Drones;
import App.Services.DataStorage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

/**
 * The Overview class represents a panel in the user interface that displays an overview of drones.
 * It includes a table showing relevant information about each drone, such as ID,
 * manufacturer, typename, serial number, creation date, status, and last seen.
 * @author Ahmad Hamd Al Ali
 */

public class Overview extends JPanel implements UIPanel{

    private JTable droneTable;
    private DefaultTableModel droneTableModel;
    private DataStorage dataStorage;
    private final JPanel overview = new JPanel();

    /**
     * Returns the JPanel containing the overview.
     */

    public JPanel getJPanel() {
        overview.validate();
        return overview;
    }

    /**
     * Constructs a new Overview instance and initializes the user interface components.
     */

    public Overview() {
        this.initialize();
    }

    /**
     * Initializes the user interface components, including the drone table and its appearance.
     */

    public void initialize() {
        overview.removeAll();

        dataStorage = Main.getDataStorage();
        String[] columns = {"ID", "Manufacturer", "Typename", "Serialnumber", "Created", "Status", "Last Seen"};
        droneTableModel = new DefaultTableModel(columns, 0);
        droneTable = new JTable(droneTableModel);
        configureTableAppearance();
        for (Drones currentDrone : dataStorage.getDronesList()) {
            populateTableRow(currentDrone);
        }
        addComponentsToPanel();
    }

    /**
     * Refreshes the data in the overview by updating the drone table.
     */

    @Override
    public void refreshData() {
        dataStorage = Main.getDataStorage();
        updateDroneTable();
    }

    /**
     * Adds the drone table to the center of the panel.
     */

    private void addComponentsToPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(new JScrollPane(droneTable), BorderLayout.CENTER);
        overview.add(centerPanel, BorderLayout.CENTER);
    }

    /**
     * Configures the appearance of the drone table, such as disabling editing,
     * setting column widths, and disabling column reordering.
     */

    private void configureTableAppearance() {
        droneTable.setEnabled(false);
        JTableHeader tableHeader = droneTable.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        droneTable.setAutoResizeMode(0);
        setColumnWidths();
    }

    /**
     * Sets the preferred widths for each column in the drone table.
     */

    private void setColumnWidths() {
        droneTable.getColumnModel().getColumn(0).setPreferredWidth(30);   // ID
        droneTable.getColumnModel().getColumn(1).setPreferredWidth(120);  // Manufacturer
        droneTable.getColumnModel().getColumn(2).setPreferredWidth(150);  // Typename
        droneTable.getColumnModel().getColumn(3).setPreferredWidth(130);  // Serialnumber
        droneTable.getColumnModel().getColumn(4).setPreferredWidth(200);  // Created
        droneTable.getColumnModel().getColumn(5).setPreferredWidth(60);   // Status
        droneTable.getColumnModel().getColumn(6).setPreferredWidth(200);  // Last Seen
    }

    /**
     * Updates the drone table by removing existing rows and populating it with the latest drone data.
     */

    private void updateDroneTable() {
        dataStorage = Main.getDataStorage();
        for (int i = droneTableModel.getRowCount()-1; i >= 0 ; i--) {
            droneTableModel.removeRow(i);
        }
        for (Drones currentDrone : dataStorage.getDronesList()) {
            populateTableRow(currentDrone);
        }
    }

/**
 * Populates a row in the drone table with information from the given drone.
 */

    private void populateTableRow(Drones currentDrone) {
        int id = currentDrone.getId();
        String manufacturer = currentDrone.getDronetype().getManufacturer();
        String typename = currentDrone.getDronetype().getTypename();
        String serialNumber = currentDrone.getSerialnumber();
        String created = currentDrone.getCreated();
        String status = dataStorage.getDynamicsForDrone(id).getLast().getStatus();
        String lastUpdate = dataStorage.getDynamicsForDrone(id).getLast().getLastSeen();

        droneTableModel.addRow(new Object[]{id, manufacturer, typename, serialNumber, created, status, lastUpdate});
    }
}
