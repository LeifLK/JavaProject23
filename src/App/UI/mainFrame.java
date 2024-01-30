package App.UI;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

public class mainFrame extends JFrame implements ActionListener {

    private final JButton overviewButton;
    private final JButton dashboardButton;
    private final JButton catalogButton;
    private final JButton historyButton;

    JFrame program;
    final JLabel label;
    final JPanel rightPanel;
    final JPanel buttonPanelLeft;
    final CardLayout cardLayout;
    final dashboard dashboard;

    public mainFrame() throws MalformedURLException {
        ImageIcon image = new ImageIcon("C:\\Users\\andre\\Downloads\\drone1.jpeg");
        Border border = BorderFactory.createLineBorder(Color.WHITE);

        //JLabel
        label = new JLabel(image);  // create label
        label.setText("Welcome to the Drone Manager"); // set Text
        label.setIcon(image); // set Icon with our image
        label.setIconTextGap(25); // Gap between Icon and text
        label.setHorizontalTextPosition(JLabel.LEFT); // Horizontal Position of Text
        label.setVerticalTextPosition(JLabel.CENTER); // Vertical Position of Text
        label.setForeground(Color.WHITE);  // Color of Text
        label.setFont(new Font("MV Boli", Font.PLAIN, 20)); // Form and Size of Text
        label.setVerticalAlignment(JLabel.TOP); // set vertical position of icon and text inside label
        label.setHorizontalAlignment(JLabel.CENTER); // set horizontal position of icon and text inside label
        label.setBorder(border);


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

        Overview overview = new Overview();
        dashboard = new dashboard();
        Catalog catalog = new Catalog();
        History history = new History();

        rightPanel.add("Overview", overview.getJPanel());
        rightPanel.add("Catalog", catalog.getJPanel());
        rightPanel.add("Dashboard", dashboard.getJPanel());
        rightPanel.add("History", history);
        catalog.setFrame(this);
        history.setFrame(this);

        // JFrame
        program = new JFrame();
        program.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        program.setSize(800, 800);
        program.setTitle("Drone Manager");
        rightPanel.add(label);
        buttonPanelLeft.add(overviewButton);
        buttonPanelLeft.add(dashboardButton);
        buttonPanelLeft.add(catalogButton);
        buttonPanelLeft.add(historyButton);
        program.add(rightPanel);
        program.add(buttonPanelLeft);
        program.setResizable(false);
        program.setLayout(null);
        program.setVisible(true);


        //Logo
        ImageIcon logo = new ImageIcon(new URL("https://i.imgur.com/4LylQgE.png")); // set logo of our frame
        program.setIconImage(logo.getImage());
        program.getContentPane().setBackground(Color.BLACK);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == overviewButton) {
            cardLayout.show(rightPanel, "Overview");
        } else if (e.getSource() == dashboardButton) {
            cardLayout.show(rightPanel, "Dashboard");
        } else if (e.getSource() == catalogButton) {
            cardLayout.show(rightPanel, "Catalog");
        } else if (e.getSource() == historyButton) {
            //history.startHistory();
            cardLayout.show(rightPanel, "History");
        }
    }

    //TODO: Remove
    public void reloadCatalog() {
        cardLayout.show(rightPanel, "Dashboard");
        cardLayout.show(rightPanel, "Catalog");
    }

    public void loadDashboardAt(int droneID) {
        cardLayout.show(rightPanel, "Dashboard");
        dashboard.currentDroneId = droneID;
        dashboard.createDashboard();
    }
}



