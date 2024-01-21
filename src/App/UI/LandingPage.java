package App.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import static App.UI.catalog.dataStorage;

public class LandingPage {
    private JFrame frame;
    private JLabel backgroundLabel;
    private JLabel titleLabel;
    private JLabel descriptionLabel;
    private JButton connectButton;
    private JButton startAppButton; // New button for starting the application

    private JLabel connectionLabel;
    private JLabel connectionStatusCircle;

    private API apiService;

    public LandingPage() {
        apiService = new API(); //  instance of ApiService
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Load background image and set it as the content pane
        ImageIcon backgroundIcon = new ImageIcon("C:/Users/ahmed/Downloads/worldmap3.png");
        backgroundLabel = new JLabel(backgroundIcon);
        frame.setContentPane(backgroundLabel);

        // Set layout manager to GridBagLayout
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // padding

        // Connection label and status circle
        connectionLabel = new JLabel("Connection");
        connectionLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        connectionLabel.setForeground(Color.RED); // Initial color is red
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        frame.add(connectionLabel, gbc);

        connectionStatusCircle = new JLabel("●");
        connectionStatusCircle.setFont(new Font("Tahoma", Font.BOLD, 20));
        connectionStatusCircle.setForeground(Color.RED); // Initial color is red
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        frame.add(connectionStatusCircle, gbc);

        // Title label
        titleLabel = new JLabel("Drone Simulator");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 50));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2; // Span two columns
        gbc.anchor = GridBagConstraints.CENTER; // Center the title label
        frame.add(titleLabel, gbc);

        // Description label
        descriptionLabel = new JLabel("Welcome to the Drone Simulator application.");
        descriptionLabel.setFont(new Font("Tahoma", Font.PLAIN, 20)); // Adjust font size
        descriptionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        descriptionLabel.setForeground(Color.WHITE);
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 2; // Span two columns
        frame.add(descriptionLabel, gbc);

        // Connect button
        connectButton = new JButton("Test API");
        connectButton.setFont(new Font("Tahoma", Font.PLAIN, 15)); // Adjust font size
        connectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Call the DroneApiService to simulate a successful connection
                simulateSuccessfulConnection();}});

        // Start Application button
        startAppButton = new JButton("Start Application");
        startAppButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
        startAppButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Close the current JFrame (LandingPage)

                // Then start the application by instantiating and displaying the myframe class
                myframe myFrame = new myframe(dataStorage);
                myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                myFrame.setVisible(true);
            }
        });

        // Positioning //
        gbc.gridy = 3;
        gbc.gridwidth = 2; // Span two columns
        gbc.anchor = GridBagConstraints.CENTER; // Center the connect button
        frame.add(connectButton, gbc);
        gbc.gridy = 6;
        frame.add(startAppButton,gbc);

        // To center the frame
        frame.setLocationRelativeTo(null);

        // Set JFrame size in order to match the image dimensions
        frame.setSize(backgroundIcon.getIconWidth(), backgroundIcon.getIconHeight());
    }

    private void simulateSuccessfulConnection() {
        try {
            apiService.ApiRequest("drones/?format=json&limit=1000");
            connectionLabel.setForeground(Color.GREEN); // Update connection status label to green in case of successful connection
            connectionStatusCircle.setForeground(Color.GREEN);
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error connecting to API: " + ex.getMessage());
        }
    }

    public void show() {
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            LandingPage window = new LandingPage();
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
