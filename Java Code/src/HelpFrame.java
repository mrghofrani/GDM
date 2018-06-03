import javax.swing.*;
import java.awt.*;

public class HelpFrame {
    private JFrame helpFrame;

    private JPanel topPanel;
    private JLabel logo;

    private JPanel bottomPanel;
    private JLabel nameLabel1;
    private JLabel nameLabel2;

    private JLabel dateLabel1;
    private JLabel dateLabel2;

    /**
     * help Frame's constructor
     * which is work like other constructor of
     * class's
     */
    public HelpFrame(){
        helpFrame = new JFrame("About");
        helpFrame.setLayout(new BorderLayout(5,5));

        topPanel = new JPanel();
        logo = new JLabel(new ImageIcon("GDM-logo.jpg"));
        topPanel.add(logo);

        bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(2,2,5,5));

        nameLabel1 = new JLabel("Developer: ");
        nameLabel2 = new JLabel("Mohammadreza Ghofrani");
        dateLabel1 = new JLabel("Project Starting Date: ");
        dateLabel2 = new JLabel("12th Ordibehesht of 1397 year");

        bottomPanel.add(nameLabel1);
        bottomPanel.add(nameLabel2);
        bottomPanel.add(dateLabel1);
        bottomPanel.add(dateLabel2);

        helpFrame.add(topPanel,BorderLayout.CENTER);
        helpFrame.add(bottomPanel,BorderLayout.SOUTH);
    }

    /**
     * shows the help Frame
     */
    public void show(){
        helpFrame.pack();
        helpFrame.setVisible(true);
    }
}
