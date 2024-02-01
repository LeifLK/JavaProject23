package App.UI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;

public class LandingPage {

    private static final Logger LOGGER = LogManager.getLogger(LandingPage.class);
    private JFrame frame;
    private JLabel backgroundLabel;
    private JLabel titleLabel;
    private JLabel descriptionLabel;
    private JButton startAppButton;
    private ImageIcon backgroundIcon;

    public LandingPage() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setupBackground();
        setupTitleAndDescription();
        setupStartAppButton();
        frame.setLocationRelativeTo(null);
        if (backgroundIcon != null)
        {
            frame.setSize(backgroundIcon.getIconWidth(), backgroundIcon.getIconHeight());
        }else
            frame.setSize(new Dimension(200, 200));
    }

    private void setupBackground() {
        try {
            backgroundIcon = new ImageIcon(new URL("https://i.imgur.com/QGkGoMH.png"));
            backgroundLabel = new JLabel(backgroundIcon);
            frame.setContentPane(backgroundLabel);
        }
        catch (MalformedURLException e)
        {
            LOGGER.warn("Landing page could not load background Image from  URL");
        }
        frame.setLayout(new GridBagLayout());
    }

    private void setupTitleAndDescription() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        titleLabel = new JLabel("Drone Simulator");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 50));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        frame.add(titleLabel, gbc);

        descriptionLabel = new JLabel("Welcome to the Drone Simulator application.");
        descriptionLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
        descriptionLabel.setForeground(Color.WHITE);
        descriptionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        frame.add(descriptionLabel, gbc);
    }

    private void setupStartAppButton() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 6;

        startAppButton = new JButton("Start Application");
        startAppButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
        startAppButton.addActionListener(e -> startApplication());

        frame.add(startAppButton, gbc);
    }

    private void startApplication() {
        frame.dispose();
        mainFrame mainFrame = new mainFrame();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
    }

    public void show() {
        frame.setVisible(true);
    }
}
