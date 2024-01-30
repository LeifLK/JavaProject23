package App.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

//The LandingPage class represents the initial landing page of the Drone Simulator application.
public class LandingPage {

    private JFrame frame;
    private JLabel backgroundLabel;
    private JLabel titleLabel;
    private JLabel descriptionLabel;
    private JButton startAppButton;

    public API apiService;

    public LandingPage() throws MalformedURLException {
        initialize();
    }

    private void initialize() throws MalformedURLException {

        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageIcon backgroundIcon = new ImageIcon(new URL("https://i.imgur.com/QGkGoMH.png"));
        backgroundLabel = new JLabel(backgroundIcon);
        frame.setContentPane(backgroundLabel);

        // Set layout manager to GridBagLayout
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // padding

        titleLabel = new JLabel("Drone Simulator");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 50));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2; // Span two columns
        gbc.anchor = GridBagConstraints.CENTER; // Center the title label
        frame.add(titleLabel, gbc);

        descriptionLabel = new JLabel("Welcome to the Drone Simulator application.");
        descriptionLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
        descriptionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        descriptionLabel.setForeground(Color.WHITE);
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 2; // Span two columns
        frame.add(descriptionLabel, gbc);

        startAppButton = new JButton("Start Application");
        startAppButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
        startAppButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Close the current JFrame (LandingPage)

                // Then start the application by instantiating and displaying the "myframe" class
                myframe myFrame = new myframe();
                myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                myFrame.setVisible(true);
            }
        });

        // Positioning
        gbc.gridy = 6;
        frame.add(startAppButton, gbc);

        frame.setLocationRelativeTo(null); // To center the frame

        frame.setSize(backgroundIcon.getIconWidth(), backgroundIcon.getIconHeight()); // To match image dimensions
    }

    public void show() {
        frame.setVisible(true);
    }
}
