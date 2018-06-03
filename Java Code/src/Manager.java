import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class Manager {

    private static Manager instance;
    private static MainFrame  main;
    private static NewDownloadFrame newDownload;
    private static SettingFrame setting;
    private static HelpFrame helpFrame;
    private static String downloadPath = "C:\\Users\\KimiaSe7en\\Desktop";
    private static String numberOfDownloads = "infinitive";

    //SystemTray and its related components
    private static SystemTray systemTray;
    private static TrayIcon trayIcon;
    private PopupMenu trayPopupMenu = new PopupMenu();
    private MenuItem newDownloadPop = new MenuItem("new download");
    private MenuItem pausePop = new MenuItem("Pause all downloads");
    private MenuItem resumePop = new MenuItem("resume all downloads");
    private MenuItem settingPop = new MenuItem("settings");
    private MenuItem exitPop = new MenuItem("exit");

    private ActionHandler actionListener = new ActionHandler();

    /**
     * constructor of the class which is private becuase we are
     * usign Singlton
     */
    private Manager() {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } catch (InstantiationException e1) {
            e1.printStackTrace();
        } catch (NullPointerException e) {
            System.out.println("Got it");
        }
        catch (IllegalAccessException e1) {
            e1.printStackTrace();
        } catch (UnsupportedLookAndFeelException e1) {
            e1.printStackTrace();
        }
        for (Window window : Window.getWindows()) {
            SwingUtilities.updateComponentTreeUI(window);
            window.pack();
        }
        setting = new SettingFrame();
        setting.initialize();
        main = new MainFrame();
        newDownload = new NewDownloadFrame();
        helpFrame = new HelpFrame();
        getAction("main.show");
        if(SystemTray.isSupported()) {
            systemTray = SystemTray.getSystemTray();
            SystemTrayHandler();
        }
        else
            JOptionPane.showMessageDialog(null,"Opps ... Your system doesn't support system tray.","System tray error",JOptionPane.WARNING_MESSAGE);
        main.initialize();
    }

    private void SystemTrayHandler(){
        Image image = Toolkit.getDefaultToolkit().getImage("pin.png");
        newDownloadPop.addActionListener(actionListener);
        settingPop.addActionListener(actionListener);
        exitPop.addActionListener(actionListener);
        trayPopupMenu.addActionListener(actionListener);


        // adding elements to trayPopupMenu
        trayPopupMenu.add(newDownloadPop);
        trayPopupMenu.add(pausePop);
        trayPopupMenu.add(resumePop);
        trayPopupMenu.add(settingPop);
        trayPopupMenu.add(exitPop);
        trayIcon = new TrayIcon(image,"GDM",trayPopupMenu);
        trayIcon.setImageAutoSize(true);
        trayIcon.addMouseListener(actionListener);
        try{
            systemTray.add(trayIcon);
        }catch(AWTException awtException){
            JOptionPane.showMessageDialog(null,"Ops.. Something went wrong","Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * A repository of action desired by the classes
     * @param action invoked by the user
     */
    public static void getAction(String action){
        String part[] = action.split("[.]");
        // class finder switch case
        switch( part[0] ){

            case "main":
                switch( part[1] ){
                    case "show":
                        main.show();
                        break;
                    case "update":
                        main.updateProcessingDownloads();
                        break;
                }
            break;


            case "setting":
                // Action finder switch case
                switch( part[1] ){
                    case "show":
                        setting.show();
                        break;
                }
                break;


            case "newDownload":
                switch( part[1] ){
                    case "show":
                        newDownload.show();
                        break;
                }
                break;

        }
    }

    private class ActionHandler implements ActionListener,MouseListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource().equals(newDownloadPop)){
                Manager.getAction("newDownload.show");
            }
            else if(e.getSource().equals(settingPop)){
                Manager.getAction("setting.show");
            }
            else if(e.getSource().equals(exitPop)) {
                safelyExit();
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getSource().equals(trayIcon) && e.getClickCount() > 1){
                main.show();
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if(e.getSource().equals(trayIcon) && e.getClickCount() > 1){
                main.show();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {}

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}
    }

    /**
     * a request for showing the right panel information on the mainFrame
     * @param fileProperties
     */
    public static void showRightPanel(FileProperties fileProperties){
        main.showRightPanel(fileProperties);
    }

    /**
     * a request for hiding the information panel located in mainPanel
     */
    public static void hideRightPanel(){
        main.hideRightPanel();
    }

    /**
     * @return download path of the downlaod file
     */
    public static String getDownloadPath(){
        return setting.getDownloadPath();
    }


    /**
     * @return the number of downloads in each queue
     * in real asks it from Setting Frame
     */
    public static String getNumberOfDownloads(){
        return setting.getNumberOfDownload();
    }


    /**
     * @return an instance of the class which
     * help us have only one object of the class
     */
    public static Manager getInstance(){
        if(instance == null){
            instance = new Manager();
        }
        return instance;
    }

    /**
     * updates the completed panel
     */
    public static void updateCompleted(){
        main.updateCompleted();
    }
    /**
     * adds a new download to Processing
     * @param fileProperties properties of file
     */
    public static void addNewDownload(FileProperties fileProperties){
        main.setNewDownload(fileProperties);
    }

    /**
     * adds a new download to Queue
     * @param fileProperties properties of file
     */
    public static void addNewDownloadQueue(FileProperties fileProperties){
        main.setNewDownloadQueue(fileProperties);
    }

    /**
     * this method exits the
     * program by writing the
     * state of program into hardware
     */
    public static void safelyExit(){
//        ObjectOutput input
        systemTray.remove(trayIcon);
        main.backup();
        setting.backup();
        System.exit(0);
    }

    /**
     * shows the help frames components
     */
    public static void showAbout(){
        helpFrame.show();
    }

    /**
     * @return inValid urls stored in the Setting
     */
    public static ArrayList<String> getInvalidURLs(){
        return setting.getInvalidURLs();
    }

    /**
     * updates and set the look and feel to
     * desired look and feel
     * @param lookAndFeel desired look and feel
     */
    public static void updateUI(String lookAndFeel){
        try {
            UIManager.setLookAndFeel(lookAndFeel);
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
            window.pack();
        }
    }

    /**
     * This method is used to
     * initialize the first state
     * of programme which is running
     */
    private void initialState(){
        main.initialize();
        setting.initialize();
    }

}
