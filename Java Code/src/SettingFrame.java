import org.omg.CORBA.Object;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SettingFrame implements Serializable {
    // Main basis
    private JFrame settingFrame;
    private final String SETTING_PATH = "files/setting.gdm";

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
    private JList filterList;
    private DefaultListModel filterListModel;
    private JScrollPane filterScrollPane;
    private JPanel eastSideOfFilterPanel;
    private JButton addListButton;
    private JButton deleteListButton;


    // reset button
    private JPanel setDefaultPanel;
    private JButton setDefaultButton;

    private ActionHandler actionHandler = new ActionHandler();


    public SettingFrame(){
        settingFrame = new JFrame("Setting");
        settingFrame.setLayout(new BoxLayout(settingFrame.getContentPane(),BoxLayout.PAGE_AXIS));

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

        westSideOfFilterPanel = new JPanel(new BorderLayout());
        filterLabel = new JLabel("Filtered Sites: ");
        westSideOfFilterPanel.add(filterLabel);
        centralSideOfFilterPanel = new JPanel();
        filterListModel = new DefaultListModel();
        filterList = new JList(filterListModel);
        filterList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        filterList.setLayoutOrientation(JList.VERTICAL);
        filterList.setVisibleRowCount(5);
        filterList.setOpaque(true);
        filterList.setBackground(Color.WHITE);
//        filterList.setSize(new Dimension(100,200));
//        filterList.setBorder(BorderFactory.createLineBorder(Color.ORANGE,20));
        filterScrollPane = new JScrollPane(filterList);
        centralSideOfFilterPanel.add(filterScrollPane,BorderLayout.CENTER);

        eastSideOfFilterPanel = new JPanel();
        eastSideOfFilterPanel.setLayout(new GridLayout(4,1,5,5));
        addListButton = new JButton();
        addListButton.setText("Add");
        addListButton.addActionListener(actionHandler);
        deleteListButton = new JButton();
        deleteListButton.setText("Delete");
        deleteListButton.setEnabled(false);
        deleteListButton.addActionListener(actionHandler);
        eastSideOfFilterPanel.add(new JLabel());
        eastSideOfFilterPanel.add(addListButton);
        eastSideOfFilterPanel.add(deleteListButton);
        eastSideOfFilterPanel.add(new JLabel());

        filterPanel.add(westSideOfFilterPanel,BorderLayout.WEST);
        filterPanel.add(centralSideOfFilterPanel,BorderLayout.CENTER);
        filterPanel.add(eastSideOfFilterPanel,BorderLayout.EAST);
        settingFrame.add(filterPanel);


        // Default location Panel
        locationPanel = new JPanel();
        locationPanel.setLayout(new BoxLayout(locationPanel,BoxLayout.LINE_AXIS));
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

    public void initialize(){
        File file = new File(SETTING_PATH);
        if(file.exists()){
           FileInputStream fileInputStream;
           ObjectInputStream objectInputStream;
           try{
               fileInputStream = new FileInputStream(file);
               objectInputStream = new ObjectInputStream(fileInputStream);
               ArrayList<String> arrayList = (ArrayList)objectInputStream.readObject();

               // Preparing number of downloads
               int index = 0;
               if(arrayList.get(index).equals(infinitive))
                   infinitive = true;
               else{
                   infinitive = false;
                   numberOfDownloadSpinner.setValue(arrayList.get(index));
               }


               // Preparing theme
               index++;
               ArrayList<String> themes = new ArrayList<>();
               boolean themeFound = false;
               for (UIManager.LookAndFeelInfo item: UIManager.getInstalledLookAndFeels()) {
                   if(item.equals(arrayList.get(index)))
                       themeFound = true;
                   themes.add(item.getClassName());
               }
               themeComboBox = new JComboBox(themes.toArray());
               if(themeFound)
                   themeComboBox.setSelectedItem(arrayList.get(index));
               else
                   themeComboBox.setSelectedItem(UIManager.getSystemLookAndFeelClassName());

               // Preparing illegal websites
               index++;
               String[] illegalWebSites = ((String)arrayList.get(index)).split(" ");
               for (String illegalWebSite: illegalWebSites)
                    filterListModel.addElement(illegalWebSite);

               // Preparing location of the download
               index++;
               locationText.setText(arrayList.get(index));
           } catch (FileNotFoundException e) {
               e.printStackTrace();
           } catch (IOException e) {
               e.printStackTrace();
           } catch (ClassNotFoundException e) {
               e.printStackTrace();
           }
        }
        else {

        }
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

    /**
     * this method implements the
     * serialization concept and
     * stores the data into the file
     */
    public void backup(){
        File file = new File(SETTING_PATH);
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream  = null;
        try {
            fileOutputStream = new FileOutputStream(file,false);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(backupData());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * this method maintains all data and
     * backs up them into an arrayList so
     * back up method could write it into the file
     * @return ArrayList of Setting fields
     */
    public ArrayList<String> backupData(){
        ArrayList<String> returnValue = new ArrayList<>();

        if(infinitive)returnValue.add("infinitive");
        else returnValue.add(numberOfDownloadSpinner.getValue().toString());

        returnValue.add((String)themeComboBox.getSelectedItem());

        String tmp = "";
        for (String item: getInvalidURLs())
            tmp += item + " " ;
        returnValue.add(tmp);

        returnValue.add(locationText.getText());
        return returnValue;
    }
    
    public ArrayList<String> getInvalidURLs(){
        ArrayList<String> returnStrings = new ArrayList<>();
        for (int i = 0; i < filterListModel.size(); i++)
            returnStrings.add((String)filterListModel.getElementAt(i));
        return returnStrings;
    }

    public String getDownloadPath(){
        return locationText.getText();
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
//                Manager.setDownloadPath(locationString);
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
            else if(e.getSource().equals(addListButton)) {
                int state;
                String tmp;
                do {
                    tmp = (String) JOptionPane.showInputDialog(settingFrame,
                            "Enter your desired website or file \n to be filtered:",
                            "Add to filter", JOptionPane.PLAIN_MESSAGE,
                            null,
                            null,
                            null);
                    if (filterListModel.contains(tmp)) {
                        JOptionPane.showMessageDialog(settingFrame,"Your input is already in the list");
                        state = 4;
                    }
                    else {
                        try {
                            URL tmpUrl = new URL(tmp);
                            state = 0;
                        } catch (MalformedURLException e1) {
                            if (tmp == null) state = 1;
                            else {
                                state = 2;
                                JOptionPane.showMessageDialog(settingFrame, "Invalid url entered", "", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }while (state == 2) ;
                    if (state == 0) {
                        filterListModel.add(filterListModel.getSize(), tmp);
                        filterList.ensureIndexIsVisible(filterListModel.getSize());
                        deleteListButton.setEnabled(true);
                    }
                }
            else if (e.getSource().equals(deleteListButton)){
                int[] indicies = filterList.getSelectedIndices();
                if(indicies.length > 0){
                    for (int i = indicies.length - 1; i >= 0 ; i--)
                        filterListModel.removeElementAt(indicies[i]);

                }
                else{
                    JOptionPane.showMessageDialog(settingFrame,"Select some items then click the button.","Cancel",JOptionPane.WARNING_MESSAGE);
                }
                if(filterListModel.isEmpty()){
                    deleteListButton.setEnabled(false);
                }
            }
        }
    }
}
