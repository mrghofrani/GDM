import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class MainFrame {
//    private static MainFrame mainFrame;
    private JFrame background;
    private SettingFrame settingFrame;
    private NewDownloadFrame downloadFrame;
    private MainFrameActionListener actionListener;
    private ArrayList<NewDownloadPanel> arrayList = new ArrayList<>();

    //SystemTray and its related components
    SystemTray systemTray = SystemTray.getSystemTray();
    TrayIcon trayIcon;
    PopupMenu trayPopupMenu = new PopupMenu();
    MenuItem newDownloadPop = new MenuItem("new download");
    MenuItem pausePop = new MenuItem("Pause all downloads");
    MenuItem resumePop = new MenuItem("resume all downloads");
    MenuItem settingPop = new MenuItem("settings");
    MenuItem exitPop = new MenuItem("exit");

    //menuBar and its related elements
    private JMenuBar menuBar;
    private JMenu download;
    private JMenuItem newDownloadItem;
    private JMenuItem pauseItem;
    private JMenuItem resumeItem;
    private JMenuItem cancelItem;
    private JMenuItem removeItem;
    private JMenuItem settingItem;
    private JMenuItem exitItem;
    private JMenu helpMenu;
    private JMenuItem aboutItem;

    // leftPanel and its related components
    private JPanel leftPanel;
    private JLabel title;
    private JButton processing;
    private JButton completed;
    private JButton queue;

    // left panel and its related components
    private JPanel topPanel;
    private final int BUTTON_SIZE_ON_TOP_PANEL = 40;
    private JButton newDownload;
    private JButton pause;
    private JButton resume;
    private JButton cancel;
    private JButton remove;
    private JButton sort;
    private JToolBar toolBar;
    private JMenuItem byDate;
    private JMenuItem byStatus;
    private JMenuItem byName;
    private JMenuItem bySize;
    private JPopupMenu sortPopUp;

    private JButton setting;

    private JPanel rightPanel;


    // Main Panel
    private JPanel centralPanel;
    private JScrollPane scrollPane;



    public MainFrame() {
        // initializing Frame
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
        background = new JFrame("GDM");
        background.setLayout(new BorderLayout(5,5));
//        settingFrame = SettingFrame.getInstanceOf();
//        downloadFrame = NewDownloadFrame.getInstance();
        actionListener = new MainFrameActionListener();

        // initializing leftPanel
        leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel,BoxLayout.Y_AXIS));
        processing = new JButton("Processing");
        int buttonWidth = processing.getPreferredSize().width + 100;
        completed = new JButton("completed");
        completed.setMaximumSize(new Dimension(buttonWidth,completed.getPreferredSize().height));
        queue = new JButton("Queue");
        queue.setMaximumSize(new Dimension(buttonWidth,queue.getPreferredSize().height));
        leftPanel.add(processing);
        leftPanel.add(Box.createRigidArea(new Dimension(0,5)));// for having a gap to
        leftPanel.add(completed);
        leftPanel.add(Box.createRigidArea(new Dimension(0,5)));
        leftPanel.add(queue);

        // initializing topPanel
        title = new JLabel("Faster download with GDM");
        newDownload = new JButton();
        newDownload.setToolTipText("Hit me if you want a new download.");
        newDownload.setIcon(new ImageIcon("newDownload.png"));
        newDownload.setPreferredSize(new Dimension(BUTTON_SIZE_ON_TOP_PANEL,BUTTON_SIZE_ON_TOP_PANEL));
        newDownload.addActionListener(actionListener);
        pause = new JButton();
        pause.setToolTipText("Hit me if you want to pause your donwload.");
        resume = new JButton();
        resume.setToolTipText("Hit me if you want to resume your download.");
        resume.setIcon(new ImageIcon("resume.png"));
        resume.setPreferredSize(new Dimension(BUTTON_SIZE_ON_TOP_PANEL,BUTTON_SIZE_ON_TOP_PANEL));
        cancel = new JButton();
        cancel.setToolTipText("Hit me if you want to cancel your donwload.");
        cancel.setIcon(new ImageIcon("cancel.png"));
        cancel.setPreferredSize(new Dimension(BUTTON_SIZE_ON_TOP_PANEL,BUTTON_SIZE_ON_TOP_PANEL));
        remove = new JButton();
        remove.setToolTipText("Hit me if you want to remove your downloaded file.");
        remove.setIcon(new ImageIcon("remove.jpeg"));
        remove.setPreferredSize(new Dimension(BUTTON_SIZE_ON_TOP_PANEL,BUTTON_SIZE_ON_TOP_PANEL));
        setting = new JButton();
        setting.setToolTipText("Hit me if you want to change your setting.");
        setting.setIcon(new ImageIcon("setting.png"));
        setting.setPreferredSize(new Dimension(BUTTON_SIZE_ON_TOP_PANEL,BUTTON_SIZE_ON_TOP_PANEL));
        setting.addActionListener(actionListener);
        sort = new JButton(new ImageIcon("sort.png"));
        sortPopUp = new JPopupMenu();
        sort.setToolTipText("Sort Via ...");
        sort.setPreferredSize(new Dimension(BUTTON_SIZE_ON_TOP_PANEL,BUTTON_SIZE_ON_TOP_PANEL));
        sort.addActionListener(actionListener);
        sortPopUp.add( byDate = new JMenuItem("By Date"));
        sortPopUp.add( bySize = new JMenuItem("By Size"));
        sortPopUp.add( byName = new JMenuItem("By Name"));
        sortPopUp.add( byStatus = new JMenuItem("By Status"));

        toolBar = new JToolBar();
        toolBar.add(title);
        toolBar.add(newDownload);
        toolBar.add(pause);
        toolBar.add(resume);
        toolBar.add(cancel);
        toolBar.add(remove);
        toolBar.add(setting);
        toolBar.add(sort);
//        toolBar.add()
//        topPanel = new JPanel();
//        topPanel.setLayout(new FlowLayout());
//        topPanel.add(title);
//        topPanel.add(newDownload);
//        topPanel.add(pause);
//        topPanel.add(resume);
//        topPanel.add(cancel);
//        topPanel.add(remove);
//        topPanel.add(setting);

        // Designing menuBar
        menuBar = new JMenuBar();
        download = new JMenu("Download");

        newDownloadItem = new JMenuItem("New Download",KeyEvent.VK_D);
        newDownloadItem.setAccelerator(KeyStroke.getKeyStroke("control N"));
        newDownloadItem.addActionListener(actionListener);

        resumeItem = new JMenuItem("Resume",KeyEvent.VK_R);
        resumeItem.setAccelerator(KeyStroke.getKeyStroke("control R"));

        pauseItem = new JMenuItem("Pause",KeyEvent.VK_P);
        pauseItem.setAccelerator(KeyStroke.getKeyStroke("control P"));

        cancelItem = new JMenuItem("Cancel",KeyEvent.VK_C);
        cancelItem.setAccelerator(KeyStroke.getKeyStroke("control C"));

        removeItem = new JMenuItem("Remove",KeyEvent.VK_V);
        removeItem.setAccelerator(KeyStroke.getKeyStroke("control V"));

        settingItem = new JMenuItem("Setting",KeyEvent.VK_S);
        settingItem.setAccelerator(KeyStroke.getKeyStroke("control S"));
        settingItem.addActionListener(actionListener);

        exitItem = new JMenuItem("Exit",KeyEvent.VK_E);
        exitItem.setAccelerator(KeyStroke.getKeyStroke("control E"));
        exitItem.addActionListener(actionListener);

        download.add(resumeItem);
        download.add(pauseItem);
        download.add(cancelItem);
        download.add(removeItem);
        download.add(settingItem);
        download.add(exitItem);

        helpMenu = new JMenu("Help");

        aboutItem = new JMenuItem("About");
        aboutItem.setAccelerator(KeyStroke.getKeyStroke("control A"));

        helpMenu.add(aboutItem);
        menuBar.add(download);
        menuBar.add(helpMenu);


        // Central Panel
        centralPanel = new JPanel();
        centralPanel.setLayout(new BoxLayout(centralPanel,BoxLayout.Y_AXIS));
        scrollPane = new JScrollPane(centralPanel);
        background.add(scrollPane);


        // setting to frame
        background.setJMenuBar(menuBar);
        background.add(toolBar,BorderLayout.NORTH);
        background.add(leftPanel,BorderLayout.WEST);



    }

    public void show(){
        SystemTrayHandler();
        background.pack();
        background.setVisible(true);
    }

    private void SystemTrayHandler(){
        if(!SystemTray.isSupported()){
            JOptionPane.showMessageDialog(null,"Opps ... Your system doesn't support system tray.","System tray error",JOptionPane.WARNING_MESSAGE);
            return;
        }
        Image image = Toolkit.getDefaultToolkit().getImage("Java Code/favicon (1).ico");
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
        trayIcon = new TrayIcon(image,"GDM The Fastest Download Manager",trayPopupMenu);
        trayIcon.setImageAutoSize(true);
        try{
            systemTray.add(trayIcon);
        }catch(AWTException awtException){
            JOptionPane.showMessageDialog(background,"Opps.. Something went wrong","Error",JOptionPane.ERROR_MESSAGE);
        }
        System.out.println("end of main");
    }


    private class MainFrameActionListener implements ActionListener,MouseListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(setting) ) {
//                settingFrame.show();
                Manager.getAction("setting.show");
            }
            else if (e.getSource().equals(settingItem)){
//                settingFrame.show();
                Manager.getAction("setting.show");
            }
            else if (e.getSource().equals(newDownload)){
                Manager.getAction("newDownload.show");
            }
            else if(e.getSource().equals(newDownloadItem)){
//                downloadFrame.show();
                Manager.getAction("newDownload.show");
            }
            else if (e.getSource().equals(exitItem)){
                System.exit(0);
            }
            else if(e.getSource().equals(trayIcon)){
                background.setVisible(true);
            }
            else if(e.getSource().equals(newDownloadPop)){
//                NewDownloadFrame.getInstance().show();
                Manager.getAction("newDownload.show");
            }
            else if(e.getSource().equals(settingPop)){
//                SettingFrame.getInstanceOf().show();
                Manager.getAction("setting.show");
            }
            else if(e.getSource().equals(exitPop)) {
                System.exit(0);
            }
            else if(e.getSource().equals(trayIcon) || e.getSource().equals(systemTray) || e.getSource().equals(trayPopupMenu)){
                background.setVisible(true);
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
//            if (e.equals(setting)) {
//                settingFrame.show();
//                System.out.println("Pressed");
//            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if(e.getSource().equals(systemTray) && e.getClickCount() > 1){
                background.setVisible(true);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

    }

    public void setNewDownload(NewDownloadPanel newDownload) {
        centralPanel.add(newDownload.getPanel());
        background.revalidate();
    }

//    public static MainFrame getInstance(){
//        if(mainFrame == null){
//            try {
//                mainFrame = new MainFrame();
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            } catch (UnsupportedLookAndFeelException e) {
//                e.printStackTrace();
//            } catch (InstantiationException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        }
//        return mainFrame;
//    }
}
