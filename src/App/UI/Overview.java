package App.UI;

import App.Main;
import App.Model.Drones;
import App.Services.DataStorage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

//The Overview class represents a panel displaying an overview of drones.

public class Overview extends JPanel implements UIPanel{

    private JTable droneTable;
    private DefaultTableModel droneTableModel;
    private DataStorage dataStorage;
    private final JPanel overview = new JPanel();

    public JPanel getJPanel() {
        overview.validate();
        return overview;
    }

    public Overview() {
        this.initialize();
    }

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

    @Override
    public void refreshData() {
        dataStorage = Main.getDataStorage();
        updateDroneTable();
    }

    private void addComponentsToPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(new JScrollPane(droneTable), BorderLayout.CENTER);
        overview.add(centerPanel, BorderLayout.CENTER);
    }

    private void configureTableAppearance() {
        droneTable.setEnabled(false);
        JTableHeader tableHeader = droneTable.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        droneTable.setAutoResizeMode(0);
        setColumnWidths();
    }

    private void setColumnWidths() {
        droneTable.getColumnModel().getColumn(0).setPreferredWidth(30);   // ID
        droneTable.getColumnModel().getColumn(1).setPreferredWidth(120);  // Manufacturer
        droneTable.getColumnModel().getColumn(2).setPreferredWidth(150);  // Typename
        droneTable.getColumnModel().getColumn(3).setPreferredWidth(130);  // Serialnumber
        droneTable.getColumnModel().getColumn(4).setPreferredWidth(200);  // Created
        droneTable.getColumnModel().getColumn(5).setPreferredWidth(60);   // Status
        droneTable.getColumnModel().getColumn(6).setPreferredWidth(200);  // Last Seen
    }

    private void updateDroneTable() {
        dataStorage = Main.getDataStorage();
        for (int i = droneTableModel.getRowCount()-1; i >= 0 ; i--) {
            droneTableModel.removeRow(i);
        }
        for (Drones currentDrone : dataStorage.getDronesList()) {
            populateTableRow(currentDrone);
        }
    }

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
