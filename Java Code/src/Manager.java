import javax.swing.*;

public class Manager {

    private static Manager instance;
    private static MainFrame  main;
    private static NewDownloadFrame newDownload;
    private static SettingFrame setting;
    private static String downloadPath = "C:\\Users\\KimiaSe7en\\Desktop";
    private static String numberOfDownloads = "infinitive";

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
        getAction("main.show");
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
        return numberOfDownloads;
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
}
