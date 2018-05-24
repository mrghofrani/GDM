import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class SettingFrame implements Serializable {
    // Main basis
    private JFrame settingFrame;

    // Download limit Panel
    private JPanel downloadLimitPanel;
    private ButtonGroup downloadLimitButtonGroup;
    private JRadioButton infinitiveDownloads;
    private JRadioButton finiteDownloads;
    private boolean infinitive;
    private JSpinner numberOfDownloadSpinner;

    //theme Panel's components
    private JPanel themePanel;
    private JLabel theme;
    private JComboBox themeComboBox;
    private ArrayList<String> lookAndFeels;

    // location Panel's components
    private JPanel locationPanel;
    private JLabel locationLabel;
    private JTextField locationText;
    private JButton locationButton;
    private String locationString;
    private JFileChooser locationChooser;

    // illegal web
    private JPanel filterPanel;
    private JPanel westSideOfFilterPanel;
    private JLabel filterLabel;
    private JPanel centralSideOfFilterPanel;
    private JList<String> filterList;
    private JPanel eastSideOfFilterPanel;
    private JButton addListButton;
    private JButton deleteListButton;


    // reset button
    private JPanel setDefaultPanel;
    private JButton setDefaultButton;

    private ActionHandler actionHandler = new ActionHandler();


    public SettingFrame(){
        settingFrame = new JFrame("Setting");
        settingFrame.setLayout(new BoxLayout(settingFrame.getContentPane(),BoxLayout.Y_AXIS));

        //download Panel
        downloadLimitPanel = new JPanel(new FlowLayout());
        downloadLimitButtonGroup = new ButtonGroup();
        infinitiveDownloads = new JRadioButton("Infinite number of buttons");
        infinitiveDownloads.setMnemonic(KeyEvent.VK_I);
        infinitiveDownloads.setSelected(true);
        infinitiveDownloads.addActionListener(actionHandler);
        finiteDownloads = new JRadioButton("Number of download");
        finiteDownloads.setMnemonic(KeyEvent.VK_N);
        finiteDownloads.setSelected(false);
        finiteDownloads.addActionListener(actionHandler);
        numberOfDownloadSpinner = new JSpinner(new SpinnerNumberModel(1,1,10,1));
        numberOfDownloadSpinner.setBackground(Color.WHITE);
        numberOfDownloadSpinner.setEditor(new JSpinner.DefaultEditor(numberOfDownloadSpinner));
        numberOfDownloadSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                SwingUtilities.updateComponentTreeUI(numberOfDownloadSpinner);
            }
        });
        downloadLimitButtonGroup.add(infinitiveDownloads);
        downloadLimitButtonGroup.add(finiteDownloads);
        downloadLimitPanel.add(infinitiveDownloads);
        downloadLimitPanel.add(finiteDownloads);
        downloadLimitPanel.add(numberOfDownloadSpinner);
        settingFrame.add(downloadLimitPanel);


        // Look And Feel Panel Code
        themePanel = new JPanel(new FlowLayout(5));
        theme = new JLabel("Look And Feeel: ");
        lookAndFeels = new ArrayList<>();
        for (UIManager.LookAndFeelInfo info: UIManager.getInstalledLookAndFeels())
            lookAndFeels.add(info.getClassName());
        themeComboBox = new JComboBox(lookAndFeels.toArray());
        themeComboBox.setBackground(Color.WHITE);
        themeComboBox.addActionListener(actionHandler);
        themePanel.add(theme);
        themePanel.add(themeComboBox);
        settingFrame.add(themePanel);

        // filter Panel and its related elements
        filterPanel = new JPanel(new BorderLayout(5,5));

        westSideOfFilterPanel = new JPanel();
        filterLabel = new JLabel("Filtered Sites: ");
        westSideOfFilterPanel.add(filterLabel);
        centralSideOfFilterPanel = new JPanel();
        filterList = new JList<>();
        filterList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        filterList.setLayoutOrientation(JList.VERTICAL);
        filterList.setVisibleRowCount(-1);
        filterList.setBackground(Color.ORANGE);
        filterList.setBorder(BorderFactory.createLineBorder(Color.ORANGE,20));
        centralSideOfFilterPanel.add(filterList);

        eastSideOfFilterPanel = new JPanel();
        eastSideOfFilterPanel.setLayout(new BoxLayout(eastSideOfFilterPanel,BoxLayout.Y_AXIS));
        addListButton = new JButton();
        addListButton.setText("Add");
        deleteListButton = new JButton();
        deleteListButton.setText("delete");
        eastSideOfFilterPanel.add(addListButton);
        eastSideOfFilterPanel.add(deleteListButton);

        filterPanel.add(westSideOfFilterPanel,BorderLayout.WEST);
        filterPanel.add(centralSideOfFilterPanel,BorderLayout.CENTER);
        filterPanel.add(eastSideOfFilterPanel,BorderLayout.EAST);
        settingFrame.add(filterPanel);


        // Default location Panel
        locationPanel = new JPanel(new FlowLayout());
        locationLabel = new JLabel("Location Of Downloads: ");
        locationString = new String(Manager.getDownloadPath());
        locationText = new JTextField(locationString);
        locationText.setSize(locationText.getWidth(),200);
        locationText.setEditable(false);
        locationButton = new JButton("...");
        locationButton.addActionListener(actionHandler);
        locationChooser = new JFileChooser();
        locationChooser.setCurrentDirectory(new File(Manager.getDownloadPath()));
        locationChooser.setDialogTitle("Choose where you want to save your downloads");
        locationChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        locationChooser.setAcceptAllFileFilterUsed(false);
        locationText.setText(Manager.getDownloadPath());
        locationText.setBackground(Color.WHITE);
        locationText.setPreferredSize(new Dimension(300,25));
        locationPanel.add(locationLabel);
        locationPanel.add(locationText);
        locationPanel.add(locationButton);
        settingFrame.add(locationPanel);

        //default button
        setDefaultPanel = new JPanel();
        setDefaultButton = new JButton("reset to defaults");
        setDefaultPanel.add(setDefaultButton);
        settingFrame.add(setDefaultPanel);

        // some codes for settingFrame to look better
        settingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }

    @Override
    public String toString() {
        String returnString = "";
        returnString += "infinitive ";

        if(infinitive) returnString += "infinitive";
        else returnString += numberOfDownloadSpinner.getValue();
        returnString += "\n";

        returnString += "Theme ";
        returnString += themeComboBox.getSelectedItem();
        returnString += "\n";

        returnString += "Location ";
        returnString += locationString;
        returnString += "\n";

        return returnString;
    }

    public void show(){
        settingFrame.pack();
        settingFrame.setVisible(true);
    }

    public String getNumberOfDownload(){
        if(infinitive)
            return "infinitive";
        else
            return numberOfDownloadSpinner.getValue() + "";
    }

    private class ActionHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource().equals(themeComboBox)) {
                String tmp = (String) themeComboBox.getSelectedItem();
                try {
                    UIManager.setLookAndFeel(tmp);
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                } catch (InstantiationException e1) {
                    e1.printStackTrace();
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                } catch (UnsupportedLookAndFeelException e1) {
                    e1.printStackTrace();
                }
                for (Window window : Window.getWindows()) {
                    SwingUtilities.updateComponentTreeUI(window);
                }
            }
            else if(e.getSource().equals(locationButton)){
                locationChooser.setVisible(true);
                locationChooser.showOpenDialog(null);
                locationString = locationChooser.getSelectedFile().getAbsolutePath();
                Manager.setDownloadPath(locationString);
                locationText.setText(Manager.getDownloadPath());
                locationText.revalidate();
                System.out.println(Manager.getDownloadPath());
            }
            else if(e.getSource().equals(finiteDownloads)){
                numberOfDownloadSpinner.setEnabled(true);
                infinitive = false;
            }
            else if(e.getSource().equals(infinitiveDownloads)){
                numberOfDownloadSpinner.setEnabled(false);
                infinitive = true;
            }
        }
    }
}
