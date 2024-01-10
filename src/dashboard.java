import javax.swing.*;
import java.awt.*;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class dashboard extends JPanel {

    JPanel dashboard = new JPanel();
    JLabel label2 = new JLabel("Dashboard");

    public JPanel getJPanel(){
        return dashboard;
    }

    public dashboard(){

        label2.setBounds(10,10,100,50);
        label2.setHorizontalAlignment(JLabel.CENTER);
        label2.setVerticalAlignment(JLabel.TOP);
        label2.setFont(new Font(null,Font.PLAIN,25));

        JPanel panel1 = new JPanel();
        panel1.setBounds(0,0,100,100);
        panel1.setBackground(Color.CYAN);

        JPanel panel2 = new JPanel();
        panel2.setBounds(0,0,100,100);
        panel2.setBackground(Color.GREEN);

        JPanel panel3 = new JPanel();
        panel3.setBounds(0,0,100,100);
        panel3.setBackground(Color.BLACK);

        JPanel panel4 = new JPanel();
        panel4.setBounds(0,0,100,100);
        panel4.setBackground(Color.RED);

        JPanel panel5 = new JPanel();
        panel5.setBounds(0,0,100,100);
        panel5.setBackground(Color.ORANGE);

        //dashboard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //dashboard.setResizable(false);
        dashboard.setSize(700,800);
        dashboard.add(label2);
        dashboard.add(panel1);
        dashboard.add(panel2);
        dashboard.add(panel3);
        dashboard.add(panel4);
        dashboard.add(panel5);
        dashboard.setLayout(new GridLayout(2,3,10,10));
        dashboard.setBackground(Color.CYAN);
        dashboard.setForeground(Color.BLUE);
        //dashboard.setIconImage(logo.getImage());

        dashboard.setVisible(true);


    }
}
