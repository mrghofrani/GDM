import org.omg.CORBA.Object;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.peer.TrayIconPeer;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class SettingFrame implements Serializable {
    // Main basis
    private JFrame settingFrame;
    private final String SETTING_PATH = "files/setting.gdm";
    private final String DEFULT_DOWNLOAD_PATH = "C:\\Users\\KimiaSe7en\\Desktop";

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
    private String locationString = DEFULT_DOWNLOAD_PATH;
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

    // language Panel
    private JPanel languagePanel;
    private JLabel languageLabel;
    private JComboBox languageComboBox;
    private String chosenLanguage = "Persian";
    private final HashMap<String,HashMap<String,String>> language = new HashMap<>();


    // reset button
    private JPanel setDefaultPanel;
    private JButton setDefaultButton;

    private ActionHandler actionHandler = new ActionHandler();

    public SettingFrame(){
        settingFrame = new JFrame();
        settingFrame.setLayout(new BoxLayout(settingFrame.getContentPane(),BoxLayout.PAGE_AXIS));

        //download Panel
        downloadLimitPanel = new JPanel();
        downloadLimitPanel.setLayout(new BoxLayout(downloadLimitPanel,BoxLayout.LINE_AXIS));
        downloadLimitButtonGroup = new ButtonGroup();
        infinitiveDownloads = new JRadioButton();
        infinitiveDownloads.setMnemonic(KeyEvent.VK_I);
        infinitiveDownloads.setSelected(true);
        finiteDownloads = new JRadioButton();
        finiteDownloads.setMnemonic(KeyEvent.VK_N);
        finiteDownloads.setSelected(false);
        numberOfDownloadSpinner = new JSpinner(new SpinnerNumberModel(1,1,10,1));
        numberOfDownloadSpinner.setBackground(Color.WHITE);
        numberOfDownloadSpinner.setEditor(new JSpinner.DefaultEditor(numberOfDownloadSpinner));
        downloadLimitButtonGroup.add(infinitiveDownloads);
        downloadLimitButtonGroup.add(finiteDownloads);
        downloadLimitPanel.add(new JLabel("             "));
        downloadLimitPanel.add(infinitiveDownloads);
        downloadLimitPanel.add(new JLabel("       "));
        downloadLimitPanel.add(finiteDownloads);
        downloadLimitPanel.add(numberOfDownloadSpinner);
        downloadLimitPanel.add(new JLabel("              "));
        settingFrame.add(downloadLimitPanel);


        // Look And Feel Panel Code
        themePanel = new JPanel(new FlowLayout(5));
        theme = new JLabel();
        themeComboBox = new JComboBox();
        themeComboBox.setBackground(Color.WHITE);
        themePanel.add(new JLabel("             "));
        themePanel.add(theme);
        themePanel.add(themeComboBox);
        themePanel.add(new JLabel("             "));
        settingFrame.add(themePanel);

        // filter Panel and its related elements
        filterPanel = new JPanel(new BorderLayout(5,5));

        westSideOfFilterPanel = new JPanel(new BorderLayout());
        filterLabel = new JLabel();
        westSideOfFilterPanel.add(filterLabel);
        centralSideOfFilterPanel = new JPanel();
        filterListModel = new DefaultListModel();
        filterList = new JList(filterListModel);
        filterList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        filterList.setLayoutOrientation(JList.VERTICAL);
        filterList.setVisibleRowCount(5);
        filterList.setOpaque(true);
        filterList.setBackground(Color.WHITE);
        filterScrollPane = new JScrollPane(filterList);
        centralSideOfFilterPanel.add(filterScrollPane,BorderLayout.CENTER);

        eastSideOfFilterPanel = new JPanel();
        eastSideOfFilterPanel.setLayout(new GridLayout(4,1,5,5));
        addListButton = new JButton();
        deleteListButton = new JButton();
        deleteListButton.setEnabled(false);
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
        locationLabel = new JLabel();
        locationText = new JTextField();
        locationText.setEditable(false);
        locationButton = new JButton("...");
        locationButton.addActionListener(actionHandler);
        locationChooser = new JFileChooser();
//        locationChooser.setCurrentDirectory(new File(Manager.getDownloadPath())); TODO : I should add implement this part
//        locationChooser.setCurrentDirectory(new File(locationString));
        locationChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        locationChooser.setAcceptAllFileFilterUsed(false);
//        locationText.setText(Manager.getDownloadPath()); TODO : I should add implement this part
//        locationText.setText(locationString); TODO : I should add implement this part
        locationText.setBackground(Color.WHITE);
        locationPanel.add(locationLabel);
        locationPanel.add(locationText);
        locationPanel.add(locationButton);
        settingFrame.add(locationPanel);


        // language Panel
        languagePanel = new JPanel();
        languagePanel.setLayout(new BoxLayout(languagePanel,BoxLayout.LINE_AXIS));
        languageLabel = new JLabel();
        languageComboBox = new JComboBox();
        languageComboBox.addItem("English");
        languageComboBox.addItem("Persian");
        languageComboBox.setSelectedItem(chosenLanguage);
        languagePanel.add(new JLabel("             "));
        languagePanel.add(languageLabel);
        languagePanel.add(languageComboBox);
        languagePanel.add(new JLabel("              "));
        settingFrame.add(languagePanel);

        //default button
        setDefaultPanel = new JPanel();
        setDefaultButton = new JButton();
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
                if(arrayList.get(index).equals("infinitive")) {
                    infinitiveDownloads.setSelected(true);
                    numberOfDownloadSpinner.setEnabled(false);
                    infinitive = true;
                }
                else{
                    infinitive = false;
                    finiteDownloads.setSelected(true);
                    numberOfDownloadSpinner.setEnabled(true);
                    numberOfDownloadSpinner.setValue(Integer.parseInt(arrayList.get(index)));
                }

                // Preparing theme
                index++;
                ArrayList<String> themes = new ArrayList<>();
                boolean themeFound = false;
                for (UIManager.LookAndFeelInfo item: UIManager.getInstalledLookAndFeels()) {
                    if (item.getClassName().equals(arrayList.get(index)))
                        themeFound = true;
                    themeComboBox.addItem(item.getClassName());
                }
                if(themeFound)
                    themeComboBox.setSelectedItem(arrayList.get(index));
                else
                    themeComboBox.setSelectedItem(UIManager.getSystemLookAndFeelClassName());
                Manager.updateUI((String)themeComboBox.getSelectedItem());

                // Preparing illegal websites
                index++;
                if(!arrayList.get(index).isEmpty()) {
                    String[] illegalWebSites = ((String) arrayList.get(index)).split(" ");
                    for (String illegalWebSite : illegalWebSites)
                        filterListModel.addElement(illegalWebSite);
                }

                // Preparing location of the download
                index++;
                locationString = arrayList.get(index);
                locationText.setText(locationString);
                locationChooser.setCurrentDirectory(new File(locationString));

                // Preparing for language
                index++;
                chosenLanguage = arrayList.get(index);
                languageComboBox.setSelectedItem(chosenLanguage);
                initializingComponentsLanguage();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        else {
            infinitive = true;
            numberOfDownloadSpinner.setEnabled(false);
            infinitiveDownloads.setSelected(true);
            ArrayList<String> themes = new ArrayList<>();
            for (UIManager.LookAndFeelInfo item: UIManager.getInstalledLookAndFeels()){
                themes.add(item.getClassName());
                themeComboBox.addItem(item.getClassName());
            }

            themeComboBox.addActionListener(actionHandler);
            SwingUtilities.updateComponentTreeUI(themeComboBox);

            themeComboBox.setSelectedItem(UIManager.getSystemLookAndFeelClassName());

            locationText.setText(DEFULT_DOWNLOAD_PATH);
            chosenLanguage = "English";
            initializingComponentsLanguage();
        }
        initializingComponentsLanguage();
        initializingComponentsActionListener();
    }

    /**
     * this class initialize the component's language
     */
    private void initializingComponentsLanguage(){
        languagePackageInitialize();
        settingFrame.setTitle(language.get("settingFrame").get(chosenLanguage));
        infinitiveDownloads.setText(language.get("infinitiveDownloads").get(chosenLanguage));
        finiteDownloads.setText(language.get("finiteDownloads").get(chosenLanguage));
        theme.setText(language.get("theme").get(chosenLanguage));
        filterLabel.setText(language.get("filterLabel").get(chosenLanguage));
        addListButton.setText(language.get("addListButton").get(chosenLanguage));
        deleteListButton.setText(language.get("deleteListButton").get(chosenLanguage));
        locationLabel.setText(language.get("locationLabel").get(chosenLanguage));
        locationChooser.setDialogTitle(language.get("locationChooser").get(chosenLanguage));
        languageLabel.setText(language.get("languageLabel").get(chosenLanguage));
        setDefaultButton.setText(language.get("setDefaultButton").get(chosenLanguage));
    }

    /**
     * This method add actionListener to
     * related components
     */
    private void initializingComponentsActionListener(){
        infinitiveDownloads.addActionListener(actionHandler);
        finiteDownloads.addActionListener(actionHandler);
        numberOfDownloadSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                SwingUtilities.updateComponentTreeUI(numberOfDownloadSpinner);
            }
        });
        themeComboBox.addActionListener(actionHandler);
        addListButton.addActionListener(actionHandler);
        deleteListButton.addActionListener(actionHandler);
        languageComboBox.addActionListener(actionHandler);
    }

    private void languagePackageInitialize(){
        HashMap<String,String> tip = new HashMap<>();
        tip.put("English","Setting");
        tip.put("Persian","تنظیمات");
        language.put("settingFrame",tip);
        tip = new HashMap<>();
        tip.put("English","Infinitive number of download");
        tip.put("Persian","تعداد دانلود بی نهایت");
        language.put("infinitiveDownloads",tip);
        tip = new HashMap<>();
        tip.put("English","Number of downloads");
        tip.put("Persian","تعداد دانلود ها");
        language.put("finiteDownloads",tip);
        tip = new HashMap<>();
        tip.put("English","Look and feel");
        tip.put("Persian","ظاهر");
        language.put("theme",tip);
        tip = new HashMap<>();
        tip.put("English","Filtered Sites");
        tip.put("Persian","سایت های فیلتر شده");
        language.put("filterLabel",tip);
        tip = new HashMap<>();
        tip.put("English","Add");
        tip.put("Persian","اضافه کن");
        language.put("addListButton",tip);
        tip = new HashMap<>();
        tip.put("English","Delete");
        tip.put("Persian","حذف");
        language.put("deleteListButton",tip);

        tip = new HashMap<>();
        tip.put("English","Location of downloads");
        tip.put("Persian","محل ذخیره دانلود ها");
        language.put("locationLabel",tip);

        tip = new HashMap<>();
        tip.put("English","Choose where you want to save your downloads");
        tip.put("Persian","کجا می خوای فایلتو ذخیره کنی؟");
        language.put("locationChooser",tip);

        tip = new HashMap<>();
        tip.put("English","Language");
        tip.put("Persian","زبان");
        language.put("languageLabel",tip);

        tip = new HashMap<>();
        tip.put("English","reset to defaults");
        tip.put("Persian","بازگشت به تنظیمات اولیه");
        language.put("setDefaultButton",tip);

        tip = new HashMap<>();
        tip.put("English","Enter your desired website or file \n to be filtered:" );
        tip.put("Persian","صفحه مورد نظر خود را وارد کنید");
        language.put("tmp.get(0)",tip);

        tip = new HashMap<>();
        tip.put("English","Add to filter");
        tip.put("Persian","به فیلتر اضافه کن");
        language.put("tmp.title",tip);

        tip = new HashMap<>();
        tip.put("English","Your input is already in the list");
        tip.put("Persian","صفحه ای که وارد کرده اید را قبلا وارد کرده بودید");
        language.put("tmp.error",tip);

        tip = new HashMap<>();
        tip.put("English","");

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
            ArrayList<String> hello = backupData();
            objectOutputStream.writeObject(hello);
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

        returnValue.add(locationString);
        for (String value: returnValue) {
            System.out.println(value);
        }

        returnValue.add(chosenLanguage);
        return returnValue;
    }
    
    public ArrayList<String> getInvalidURLs(){
        ArrayList<String> returnStrings = new ArrayList<>();
        for (int i = 0; i < filterListModel.size(); i++)
            returnStrings.add((String)filterListModel.getElementAt(i));
        return returnStrings;
    }

    public String getDownloadPath(){
        return locationString;
    }


    private class ActionHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource().equals(themeComboBox)) {
                String tmp = (String) themeComboBox.getSelectedItem();
                Manager.updateUI(tmp);
            }
            else if(e.getSource().equals(locationButton)){
                locationChooser.setVisible(true);
                locationChooser.showOpenDialog(settingFrame);
                locationString = locationChooser.getSelectedFile().getAbsolutePath();
//                Manager.setDownloadPath(locationString);
                locationText.setText(locationString);
                locationText.revalidate();
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
                    boolean persian = true;
                    tmp = (String) JOptionPane.showInputDialog(settingFrame,
                            language.get("tmp.get(0)").get(chosenLanguage),
                            language.get("tmp.title").get(chosenLanguage), JOptionPane.PLAIN_MESSAGE,
                            null,
                            null,
                            null);
                    if (filterListModel.contains(tmp)) {
                        JOptionPane.showMessageDialog(settingFrame,language.get(chosenLanguage).get("tmp.error"));
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
            else if(e.getSource().equals(languageComboBox)){
                chosenLanguage = (String)languageComboBox.getSelectedItem();
                initializingComponentsLanguage();
            }
        }
    }
}
