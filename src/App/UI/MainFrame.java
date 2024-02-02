package App.UI;

import App.Model.Drones;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URI;

/**
 * The {@code mainFrame} creates our Main Frame where we have two main Panels.
 * The Left Panel is used to contain the Buttons and the right Panel is used as a card Layout to display the panel according to the button that is used.
 * Each Button has a card in the card Layout adressed to it.
 *
 * @author André
 */

public class MainFrame extends JFrame implements ActionListener {

    private static final Logger LOGGER = LogManager.getLogger(MainFrame.class);
    private final JButton overviewButton;
    private final JButton dashboardButton;
    private final JButton catalogButton;
    private final JButton historyButton;
    //JFrame program;
    final JPanel rightPanel;
    final JPanel buttonPanelLeft;
    final CardLayout cardLayout;
    final Overview overview;
    final Dashboard dashboard;
    final Catalog catalog;
    final History history;

    /**
     * This is our constructor to create our Main Frame.
     * Here we create our buttons as well as our Main Panels and the panels used as cards for the card Layout.
     * At the end we also add our Logo to the Main Frame
     *
     * @author André
     */
    public MainFrame() {

        overviewButton = new JButton();
        overviewButton.setBounds(10, 10, 50, 50);
        overviewButton.setText("Overview");
        overviewButton.setForeground(Color.BLACK);
        overviewButton.setFocusable(false);
        overviewButton.addActionListener(this);

        dashboardButton = new JButton();
        dashboardButton.setBounds(10, 10, 70, 30);
        dashboardButton.setText("Dashboard");
        dashboardButton.setForeground(Color.BLACK);
        dashboardButton.setFocusable(false);
        dashboardButton.addActionListener(this);

        catalogButton = new JButton();
        catalogButton.setText("Catalog");
        catalogButton.setForeground(Color.BLACK);
        catalogButton.setFocusable(false);
        catalogButton.addActionListener(this);

        historyButton = new JButton();
        historyButton.setBounds(10, 10, 50, 50);
        historyButton.setText("History");
        historyButton.setForeground(Color.BLACK);
        historyButton.setFocusable(false);
        historyButton.addActionListener(this);

        JButton refreshButton = new JButton();
        refreshButton.setBounds(10, 10, 50, 50);
        refreshButton.setText("Refresh UI");
        refreshButton.setForeground(Color.BLACK);
        refreshButton.setFocusable(false);
        refreshButton.addActionListener(e -> refreshPanels());

        rightPanel = new JPanel();
        rightPanel.setBackground(Color.DARK_GRAY);
        rightPanel.setForeground(Color.DARK_GRAY);
        rightPanel.setBounds(100, 0, 700, 800);

        buttonPanelLeft = new JPanel();
        buttonPanelLeft.setBackground(Color.DARK_GRAY);
        buttonPanelLeft.setBounds(0, 0, 100, 800);
        buttonPanelLeft.setLayout(new GridLayout(5, 1));

        cardLayout = new CardLayout();
        rightPanel.setLayout(cardLayout);


        JPanel catalogPanel = new JPanel();
        catalogPanel.add(new JLabel("Catalog Panel Content"));
        catalogPanel.setBackground(Color.DARK_GRAY);
        catalogPanel.setForeground(Color.WHITE);

        overview = new Overview();
        dashboard = new Dashboard();
        catalog = new Catalog();
        history = new History();
        history.setMainFrame(this);

        rightPanel.add("Overview", overview.getJPanel());
        rightPanel.add("Catalog", catalog);
        rightPanel.add("Dashboard", dashboard.getJPanel());
        rightPanel.add("History", history);

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(800, 800);
        this.setTitle("Drone Manager");
        buttonPanelLeft.add(overviewButton);
        buttonPanelLeft.add(dashboardButton);
        buttonPanelLeft.add(catalogButton);
        buttonPanelLeft.add(historyButton);
        buttonPanelLeft.add(refreshButton);
        this.add(rightPanel);
        this.add(buttonPanelLeft);
        this.setResizable(false);
        this.setLayout(null);
        this.setVisible(true);


        try {
            ImageIcon logo = new ImageIcon(URI.create("https://i.imgur.com/4LylQgE.png").toURL());
            this.setIconImage(logo.getImage());
            this.getContentPane().setBackground(Color.BLACK);
        } catch (MalformedURLException e) {
            LOGGER.warn("ImageIcon URL is malformed");
        }
    }

    /**
     * Here we have our actionPerfomed Method to display the expected card according to which Button was pressed.
     *
     * @author André
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == overviewButton) {
            cardLayout.show(rightPanel, "Overview");
        } else if (e.getSource() == dashboardButton) {
            cardLayout.show(rightPanel, "Dashboard");
        } else if (e.getSource() == catalogButton) {
            cardLayout.show(rightPanel, "Catalog");
        } else if (e.getSource() == historyButton) {
            cardLayout.show(rightPanel, "History");
        }
    }

    public void refreshPanels() {
        overview.refreshData();
        catalog.refreshData();
        //Had to be removed last minute because of visual bugs
        //dashboard.refreshData();
        history.refreshData();
        rightPanel.validate();
    }

    /**
     * This method reloads our dashboard panel.
     * It takes drone as a parameter and reloads the panel with it
     *
     * @author André
     */
    public void showDashboard(Drones drone) {
        cardLayout.show(rightPanel, "Dashboard");
        dashboard.reloadPanel(drone);
    }
}



