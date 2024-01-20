package App.UI;

import App.Services.DataStorage;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class myframe extends JFrame implements ActionListener {

    JButton button0;
    private final JButton button1;
    private final JButton button2;
    final JButton button3;

    JFrame program;
    final JLabel label;
    final JPanel panel1;
    final JPanel panel2;
    final CardLayout cardLayout;

    public myframe() {
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
        button0 = new JButton();
        button0.setBounds(10, 10, 50, 50);
        button0.setText("Overview");
        button0.setForeground(Color.BLACK);
        button0.setBackground(Color.CYAN);
        button0.setFocusable(false);
        button0.addActionListener(e -> System.out.println("Button0 used"));

        button1 = new JButton();
        button1.setBounds(10, 10, 70, 30);
        button1.setText("Dashboard");
        button1.setForeground(Color.BLACK);
        button1.setBackground(Color.CYAN);
        button1.setFocusable(false);
        button1.addActionListener(this);

        button2 = new JButton();
        button2.setText("Catalog");
        button2.setForeground(Color.BLACK);
        button2.setBackground(Color.CYAN);
        button2.setFocusable(false);
        button2.addActionListener(this);

        button3 = new JButton();
        button3.setBounds(10, 10, 50, 50);
        button3.setText("History");
        button3.setForeground(Color.BLACK);
        button3.setBackground(Color.CYAN);
        button3.setFocusable(false);
        button3.addActionListener(e -> System.out.println("Button3 used"));


        // JPanel
        panel1 = new JPanel();
        panel1.setBackground(Color.DARK_GRAY);
        panel1.setForeground(Color.DARK_GRAY);
        panel1.setBounds(100, 0, 700, 800);

        panel2 = new JPanel();
        panel2.setBackground(Color.DARK_GRAY);
        panel2.setBounds(0, 0, 100, 800);
        panel2.setLayout(new GridLayout(5, 1));

        // Set CardLayout for panel1
        cardLayout = new CardLayout();
        panel1.setLayout(cardLayout);

        // Create panels for CardLayout
        //JPanel dashboardPanel = createPanel("Dashboard Panel Content");

        JPanel catalogPanel = new JPanel();
        catalogPanel.add(new JLabel("Catalog Panel Content"));
        catalogPanel.setBackground(Color.DARK_GRAY);
        catalogPanel.setForeground(Color.WHITE);

        dashboard dashboard = new dashboard();
        catalog catalog = new catalog();

        // Add panels to panel1 with unique names
        //panel1.add(dashboardPanel, "DashboardPanel");
        //panel1.add("catalog", catalogpanel);

        panel1.add("Catalog", catalog.getJPanel());
        panel1.add("Dashboard", dashboard.getJPanel());
        catalog.setframe(this);



        //JComboBox
        /*
        String[] time = {"currentTime", "5 mins ago", "10 mins ago"};
        JComboBox comboBox = new JComboBox(time);
        */
        //JSlider

        JSlider timeSlider = new JSlider(-10, 10, 0);
        timeSlider.setPreferredSize(new Dimension(100, 100));
        timeSlider.setPaintTicks(true);
        timeSlider.setMajorTickSpacing(5);
        timeSlider.setPaintLabels(true);
        //timeSlider.addChangeListener(e-> currentTime.setText(timeSlider.getvalue()));


        // JFrame

        program = new JFrame(); // create a frame
        program.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // what happens when we press the X Button
        program.setSize(800, 800); // Size of frame
        program.setTitle("Drone Manager"); // Title of frame
        panel1.add(label);
        //panel1.add(comboBox);
        panel2.add(button0);
        panel2.add(button1);
        panel2.add(button2);
        panel2.add(button3);
        program.add(timeSlider);
        program.add(panel1);
        program.add(panel2);
        program.setResizable(false); // resizability of frame
        program.setLayout(null);
        program.setVisible(true); // visibility of frame


        //Logo
        ImageIcon logo = new ImageIcon("C:\\Users\\andre\\Downloads\\logo.png"); // set logo of our frame
        program.setIconImage(logo.getImage());
        program.getContentPane().setBackground(Color.BLACK);

    }


    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == button1) {
            cardLayout.show(panel1, "Dashboard");
        } else if (e.getSource() == button2) {
            cardLayout.show(panel1, "Catalog");
        }
    }
    public void reloadCatalog(){
        cardLayout.show(panel1,"Dashboard");
        cardLayout.show(panel1, "Catalog");

    }



    }



