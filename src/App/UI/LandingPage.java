package App.UI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * The LandingPage class represents the first welcome page displayed when the application is launched.
 * It includes a background image, a title, description, and a button to start the main application.
 * @author Ahmad Hamd Al Ali
 */

public class LandingPage {

    private static final Logger LOGGER = LogManager.getLogger(LandingPage.class);
    private JFrame frame;
    private JLabel backgroundLabel;
    private JLabel titleLabel;
    private JLabel descriptionLabel;
    private JButton startAppButton;
    private ImageIcon backgroundIcon;

    /**
     * Constructs a new LandingPage instance and initializes the user interface components.
     */
    
    public LandingPage() {
        initialize();
    }

    /**
     * Initializes the user interface components, including the frame, background, title, description, and start button.
     */

    private void initialize() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setupBackground();
        setupTitleAndDescription();
        setupStartAppButton();
        frame.setLocationRelativeTo(null);
        try {
            //Logo
            ImageIcon logo = new ImageIcon(new URL("https://i.imgur.com/4LylQgE.png")); // set logo of our frame
            frame.setIconImage(logo.getImage());
        }
        catch (MalformedURLException e)
        {
            LOGGER.warn("ImageIcon URL is malformed");
        }
        if (backgroundIcon != null)
        {
            frame.setSize(backgroundIcon.getIconWidth(), backgroundIcon.getIconHeight());
        }else {
            frame.setSize(new Dimension(200, 200));
        }
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    /**
     * Enables the start application button and updates its text to "Start Application"
     * when data is successfully loaded.
     */

    public void dataLoaded()
    {
        startAppButton.setEnabled(true);
        startAppButton.setText("Start Application");
    }

    /**
     * Sets up the background image for the landing page.
     */

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

    /**
     * Sets up the title and description labels on the landing page.
     */

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
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        frame.add(descriptionLabel, gbc);
    }

    /**
     * Sets up the start application button on the landing page.
     */

    private void setupStartAppButton() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 6;
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        startAppButton = new JButton("Loading Data");
        startAppButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
        startAppButton.addActionListener(e -> startApplication());
        startAppButton.setEnabled(false);
        frame.add(startAppButton, gbc);
    }

    /**
     * Disposes of the landing page frame and starts the main application.
     */

    private void startApplication() {
        frame.dispose();
        mainFrame mainFrame = new mainFrame();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
    }

    /**
     * Displays the landing page frame.
     */

    public void show() {
        frame.setVisible(true);
    }
}
