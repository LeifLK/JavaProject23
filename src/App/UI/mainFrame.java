package App.UI;

import App.Model.Drones;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

public class mainFrame extends JFrame implements ActionListener {

    private static final Logger LOGGER = LogManager.getLogger(mainFrame.class);
    private final JButton overviewButton;
    private final JButton dashboardButton;
    private final JButton catalogButton;
    private final JButton historyButton;
    JFrame program;
    final JPanel rightPanel;
    final JPanel buttonPanelLeft;
    final CardLayout cardLayout;
    final Overview overview;
    final dashboard dashboard;
    final Catalog catalog;
    final History history;

    public mainFrame() {

        //JButton
        overviewButton = new JButton();
        overviewButton.setBounds(10, 10, 50, 50);
        overviewButton.setText("Overview");
        overviewButton.setForeground(Color.BLACK);
        overviewButton.setBackground(Color.CYAN);
        overviewButton.setFocusable(false);
        overviewButton.addActionListener(this);

        dashboardButton = new JButton();
        dashboardButton.setBounds(10, 10, 70, 30);
        dashboardButton.setText("Dashboard");
        dashboardButton.setForeground(Color.BLACK);
        dashboardButton.setBackground(Color.CYAN);
        dashboardButton.setFocusable(false);
        dashboardButton.addActionListener(this);

        catalogButton = new JButton();
        catalogButton.setText("Catalog");
        catalogButton.setForeground(Color.BLACK);
        catalogButton.setBackground(Color.CYAN);
        catalogButton.setFocusable(false);
        catalogButton.addActionListener(this);

        historyButton = new JButton();
        historyButton.setBounds(10, 10, 50, 50);
        historyButton.setText("History");
        historyButton.setForeground(Color.BLACK);
        historyButton.setBackground(Color.CYAN);
        historyButton.setFocusable(false);
        historyButton.addActionListener(this);

        JButton refreshButton = new JButton();
        refreshButton.setBounds(10, 10, 50, 50);
        refreshButton.setText("Refresh UI");
        refreshButton.setForeground(Color.BLACK);
        refreshButton.setBackground(Color.CYAN);
        refreshButton.setFocusable(false);
        refreshButton.addActionListener(e -> refreshPanels());

        // JPanel
        rightPanel = new JPanel();
        rightPanel.setBackground(Color.DARK_GRAY);
        rightPanel.setForeground(Color.DARK_GRAY);
        rightPanel.setBounds(100, 0, 700, 800);

        buttonPanelLeft = new JPanel();
        buttonPanelLeft.setBackground(Color.DARK_GRAY);
        buttonPanelLeft.setBounds(0, 0, 100, 800);
        buttonPanelLeft.setLayout(new GridLayout(5, 1));

        // Set CardLayout for panel1
        cardLayout = new CardLayout();
        rightPanel.setLayout(cardLayout);


        JPanel catalogPanel = new JPanel();
        catalogPanel.add(new JLabel("Catalog Panel Content"));
        catalogPanel.setBackground(Color.DARK_GRAY);
        catalogPanel.setForeground(Color.WHITE);

        overview = new Overview();
        dashboard = new dashboard();
        catalog = new Catalog();
        history = new History();
        history.setMainFrame(this);

        rightPanel.add("Overview", overview.getJPanel());
        rightPanel.add("Catalog", catalog.getJPanel());
        rightPanel.add("Dashboard", dashboard.getJPanel());
        rightPanel.add("History", history);

        // JFrame
        program = new JFrame();
        program.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        program.setSize(800, 800);
        program.setTitle("Drone Manager");
        //rightPanel.add(label);
        buttonPanelLeft.add(overviewButton);
        buttonPanelLeft.add(dashboardButton);
        buttonPanelLeft.add(catalogButton);
        buttonPanelLeft.add(historyButton);
        buttonPanelLeft.add(refreshButton);
        program.add(rightPanel);
        program.add(buttonPanelLeft);
        program.setResizable(false);
        program.setLayout(null);
        program.setVisible(true);


        try {
            //Logo
            ImageIcon logo = new ImageIcon(new URL("https://i.imgur.com/4LylQgE.png")); // set logo of our frame
            program.setIconImage(logo.getImage());
            program.getContentPane().setBackground(Color.BLACK);
        }
        catch (MalformedURLException e)
        {
            LOGGER.warn("ImageIcon URL is malformed");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == overviewButton) {
            cardLayout.show(rightPanel, "Overview");
            overview.refreshData();
        } else if (e.getSource() == dashboardButton) {
            cardLayout.show(rightPanel, "Dashboard");
        } else if (e.getSource() == catalogButton) {
            cardLayout.show(rightPanel, "Catalog");
        } else if (e.getSource() == historyButton) {
            cardLayout.show(rightPanel, "History");
        }
    }
    public void refreshPanels()
    {
        overview.refreshData();
        catalog.refreshData();
        dashboard.refreshData();
        history.refreshData();
        rightPanel.validate();
    }
    public void showDashboard(Drones drone) {
        cardLayout.show(rightPanel, "Dashboard");
        dashboard.reloadPanel(drone);
    }
}



