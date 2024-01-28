package App.UI;

import App.Services.ApiService;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Overview extends JPanel {

    private JTable droneTable;
    private DefaultTableModel droneTableModel;

    public JPanel overview = new JPanel();

    public JPanel getJPanel() {
        overview.validate();
        return overview;
    }

    public Overview() {
        // Set the layout manager for this JPanel
        setLayout(new BorderLayout());

        // Initialize drone table and button
        initializeDroneTable();

        // Button to show drone information
        JButton showDronesButton = new JButton("Show Drones");
        showDronesButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
        showDronesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Call the method to update drone information
                updateDroneInformation();
            }
        });

        // Add components to the overview panel
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(new JScrollPane(droneTable), BorderLayout.CENTER);
        centerPanel.add(showDronesButton, BorderLayout.SOUTH);

        overview.add(centerPanel, BorderLayout.CENTER);
    }

    private void initializeDroneTable() {
        // Initialize the drone table model with columns
        String[] columns = {"ID", "Manufacturer", "Typename", "Serialnumber", "Created", "Status", "Last Update"};
        droneTableModel = new DefaultTableModel(columns, 0);
        droneTable = new JTable(droneTableModel);
        droneTable.setEnabled(false);
        JTableHeader tableHeader = droneTable.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        droneTable.getColumnModel().getColumn(0).setPreferredWidth(20);   // ID
        droneTable.getColumnModel().getColumn(1).setPreferredWidth(80);  // Manufacturer
        droneTable.getColumnModel().getColumn(2).setPreferredWidth(70);  // Typename
        droneTable.getColumnModel().getColumn(3).setPreferredWidth(80);  // Serialnumber
        droneTable.getColumnModel().getColumn(4).setPreferredWidth(50);  // Created
        droneTable.getColumnModel().getColumn(5).setPreferredWidth(40);   // Status
        droneTable.getColumnModel().getColumn(6).setPreferredWidth(80);  // Last Update
    }

    private void updateDroneInformation() {


    }

    private void updateDroneTable(String drones, String droneTypes, String droneDynamics) {

        }


}
