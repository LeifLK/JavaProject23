package App.UI;

import App.Model.Drones;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static App.UI.Catalog.dataStorage;

public class Overview extends JPanel {

    private JTable droneTable;
    private DefaultTableModel droneTableModel;

    public JPanel overview = new JPanel();

    public JPanel getJPanel() {
        overview.validate();
        return overview;
    }

    public Overview() {

        initializeDroneTable();

        JButton showDronesButton = new JButton("Show Drones");
        showDronesButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
        showDronesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // method to update drone information
                updateDroneTable();
            }
        });

        // Add components to the overview panel
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(new JScrollPane(droneTable), BorderLayout.CENTER);
        centerPanel.add(showDronesButton, BorderLayout.SOUTH);

        overview.add(centerPanel, BorderLayout.CENTER);
    }

    private void initializeDroneTable() {
        String[] columns = {"ID", "Manufacturer", "Typename", "Serialnumber", "Created", "Status", "Last Seen"};
        droneTableModel = new DefaultTableModel(columns, 0);
        droneTable = new JTable(droneTableModel);
        droneTable.setEnabled(false);
        JTableHeader tableHeader = droneTable.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        droneTable.setAutoResizeMode((int) JTable.CENTER_ALIGNMENT);
        droneTable.getColumnModel().getColumn(0).setPreferredWidth(30);   // ID
        droneTable.getColumnModel().getColumn(1).setPreferredWidth(120);  // Manufacturer
        droneTable.getColumnModel().getColumn(2).setPreferredWidth(150);  // Typename
        droneTable.getColumnModel().getColumn(3).setPreferredWidth(130);  // Serialnumber
        droneTable.getColumnModel().getColumn(4).setPreferredWidth(200);  // Created
        droneTable.getColumnModel().getColumn(5).setPreferredWidth(60);   // Status
        droneTable.getColumnModel().getColumn(6).setPreferredWidth(200);  // Last Update*/
    }

    private void updateDroneTable() {
            droneTableModel.setRowCount(0);
            for (Drones currentDrone : dataStorage.getDronesList()) {
                int id = currentDrone.getId();
                String manufacturer = currentDrone.getDronetype().getManufacturer();
                String typename = currentDrone.getDronetype().getTypename();
                String serialNumber = currentDrone.getSerialnumber();
                String created = currentDrone.getCreated();
                String status = dataStorage.getDynamicsForDrone(id).getLast().getStatus();
                String lastUpdate = dataStorage.getDynamicsForDrone(id).getLast().getLast_seen();

                droneTableModel.addRow(new Object[]{id, manufacturer, typename, serialNumber, created, status, lastUpdate});
            }

    }

}
