import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

public class LandingPage {

    private JFrame frame;
    private JLabel backgroundLabel;
    private JLabel titleLabel;
    private JLabel descriptionLabel;
    private JButton startAppButton;

    public LandingPage() throws MalformedURLException {
        initialize();
    }

    private void initialize() throws MalformedURLException {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setupBackground();
        setupTitleAndDescription();
        setupStartAppButton();

        frame.setLocationRelativeTo(null);
        frame.setSize(backgroundIcon.getIconWidth(), backgroundIcon.getIconHeight());
    }

    private void setupBackground() throws MalformedURLException {
        ImageIcon backgroundIcon = new ImageIcon(new URL("https://i.imgur.com/QGkGoMH.png"));
        backgroundLabel = new JLabel(backgroundIcon);
        frame.setContentPane(backgroundLabel);
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
        mainFrame myFrame = new mainFrame();
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.setVisible(true);
    }

    public void show() {
        frame.setVisible(true);
    }

}
