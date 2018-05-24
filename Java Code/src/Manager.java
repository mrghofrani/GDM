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

    private Manager() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        main = new MainFrame();
        newDownload = new NewDownloadFrame();

        setting = new SettingFrame();
        helpFrame = new HelpFrame();
        getAction("main.show");
        if(SystemTray.isSupported()) {
            systemTray = SystemTray.getSystemTray();
            SystemTrayHandler();
        }
        else
            JOptionPane.showMessageDialog(null,"Opps ... Your system doesn't support system tray.","System tray error",JOptionPane.WARNING_MESSAGE);
        initialState();
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


    public static void showRightPanel(FileProperties fileProperties){
        main.showRightPanel(fileProperties);
    }
    public static void hideRightPanel(){
        main.hideRightPanel();
    }

    public static String getDownloadPath(){
        return downloadPath;
    }

    public static void setDownloadPath(String path){
        downloadPath = path;
    }


    public static String getNumberOfDownloads(){
        return setting.getNumberOfDownload();
    }

    public static void setNumberOfDownloads(String number){
        numberOfDownloads = number;
    }


    public static Manager getInstance(){
        if(instance == null){
            instance = new Manager();
        }
        return instance;
    }

    public static void addNewDownload(FileProperties fileProperties){
        main.setNewDownload(fileProperties);
    }

    public static void addNewDownloadQueue(FileProperties fileProperties){
        main.setNewDownloadQueue(fileProperties);
    }

    public static void safelyExit(){
//        ObjectOutput input
        systemTray.remove(trayIcon);
        main.backup();
        System.exit(0);
    }
    public static void showAbout(){
        helpFrame.show();
    }
    public static ArrayList<String> getInvalidURLs(){
        return setting.getInvalidURLs();
    }

    private void initialState(){
        main.initialize();
    }

}
