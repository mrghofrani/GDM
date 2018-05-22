import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class MainFrame {
//    private static MainFrame mainFrame;
    private JFrame background;
    private JPanel mainPanel;
    private MainFrameActionListener actionListener;
    private MouseImplement mouseListener = new MouseImplement();
    private ArrayList<NewDownloadPanel> processingNewDownloads = new ArrayList<>();
    private HashMap<String,FileProperties > processingOrder = new HashMap<>();
    private HashMap<String,FileProperties> completedOrder = new HashMap<>();
    private HashMap<String,FileProperties> queueOrder = new HashMap<>();
    private ArrayList<NewDownloadPanel> queueNewDownload = new ArrayList<>();
    private int numberOfAddedToProcessing = 0;

//    //SystemTray and its related components
//    SystemTray systemTray = SystemTray.getSystemTray();
//    TrayIcon trayIcon;
//    PopupMenu trayPopupMenu = new PopupMenu();
//    MenuItem newDownloadPop = new MenuItem("new download");
//    MenuItem pausePop = new MenuItem("Pause all downloads");
//    MenuItem resumePop = new MenuItem("resume all downloads");
//    MenuItem settingPop = new MenuItem("settings");
//    MenuItem exitPop = new MenuItem("exit");

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
    private JRadioButton byDate;
    private JRadioButton byStatus;
    private JRadioButton byName;
    private JRadioButton bySize;
    private ButtonGroup sortBy;
    private JPopupMenu sortPopUp;

    private JButton setting;


    // right Panel's elements
    private JPanel rightPanel;
    private JScrollPane rightScrollPane;

    private JPanel fileNamePanel;
    private JLabel fileNameLabel;
    private JLabel fileName;

    private JPanel statusPanel;
    private JLabel statusLabel;
    private JLabel status;

    private JPanel sizePanel;
    private JLabel sizeLabel;
    private JLabel size;

    private JPanel createdPanel;
    private JLabel createdLabel;
    private JLabel created;

    private JPanel modifiedPanel;
    private JLabel modifiedLabel;
    private JLabel modified;

    private JPanel addressPanel;
    private JLabel addressLabel;
    private JLabel address;


    // Main Panel
    private JPanel processingPanel;
    private JScrollPane processingScrollPane;
    private JLabel nothing;

    private JPanel queuePanel;
    private JScrollPane queueScrollPane;




    public MainFrame() {
        background = new JFrame("GDM");
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(5,5));
        actionListener = new MainFrameActionListener();

        // initializing leftPanel
        leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel,BoxLayout.Y_AXIS));
        processing = new JButton("Processing");
        processing.addActionListener(actionListener);
        int buttonWidth = processing.getPreferredSize().width + 100;
        completed = new JButton("completed");
        completed.setMaximumSize(new Dimension(buttonWidth,completed.getPreferredSize().height));
        completed.addActionListener(actionListener);
        queue = new JButton("Queue");
        queue.setMaximumSize(new Dimension(buttonWidth,queue.getPreferredSize().height));
        queue.addActionListener(actionListener);
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
        pause.setIcon(new ImageIcon("pause.png"));
        pause.setToolTipText("Hit me if you want to pause your download.");
        pause.setPreferredSize(new Dimension(BUTTON_SIZE_ON_TOP_PANEL,BUTTON_SIZE_ON_TOP_PANEL));
        resume = new JButton();
        resume.setToolTipText("Hit me if you want to resume your download.");
        resume.setIcon(new ImageIcon("resume.png"));
        resume.setPreferredSize(new Dimension(BUTTON_SIZE_ON_TOP_PANEL,BUTTON_SIZE_ON_TOP_PANEL));
        cancel = new JButton();
        cancel.setToolTipText("Hit me if you want to cancel your donwload.");
        cancel.setIcon(new ImageIcon("cancel.png"));
        cancel.setPreferredSize(new Dimension(BUTTON_SIZE_ON_TOP_PANEL,BUTTON_SIZE_ON_TOP_PANEL));
        cancel.addActionListener(actionListener);
        remove = new JButton();
        remove.setToolTipText("Hit me if you want to remove your downloaded file.");
        remove.setIcon(new ImageIcon("remove.png"));
        remove.setPreferredSize(new Dimension(BUTTON_SIZE_ON_TOP_PANEL,BUTTON_SIZE_ON_TOP_PANEL));
        setting = new JButton();
        setting.setToolTipText("Hit me if you want to change your setting.");
        setting.setIcon(new ImageIcon("setting.png"));
        setting.setPreferredSize(new Dimension(BUTTON_SIZE_ON_TOP_PANEL,BUTTON_SIZE_ON_TOP_PANEL));
        setting.addActionListener(actionListener);
        sort = new JButton(new ImageIcon("sort.png"));
        sort.addActionListener(actionListener);
        sortPopUp = new JPopupMenu();
        sort.setToolTipText("Sort Via ...");
        sort.setPreferredSize(new Dimension(BUTTON_SIZE_ON_TOP_PANEL,BUTTON_SIZE_ON_TOP_PANEL));
        sort.addMouseListener(mouseListener);
        sortPopUp.add( byDate = new JRadioButton("By Date"));
        sortPopUp.add( bySize = new JRadioButton("By Size"));
        sortPopUp.add( byName = new JRadioButton("By Name"));
        sortPopUp.add( byStatus = new JRadioButton("By Status"));
        sortBy = new ButtonGroup();
        sortBy.add(byDate);
        sortBy.add(bySize);
        sortBy.add(byName);
        sortBy.add(byStatus);

        toolBar = new JToolBar();
        toolBar.add(title);
        toolBar.add(newDownload);
        toolBar.add(resume);
        toolBar.add(pause);
        toolBar.add(cancel);
        toolBar.add(remove);
        toolBar.add(setting);
        toolBar.add(sort);

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

        // Processing Panel
        processingPanel = new JPanel();
        nothing = new JLabel(" Nothing is here :) " + "\n" +  " Hit the download button.");
        nothing.setHorizontalTextPosition(JLabel.CENTER);
        nothing.setVerticalTextPosition(JLabel.BOTTOM);
        nothing.setForeground(Color.GRAY);
        nothing.setIcon(new ImageIcon("sunbed.png"));
        nothing.setAlignmentX(Component.CENTER_ALIGNMENT);
        processingPanel.setLayout(new BoxLayout(processingPanel,BoxLayout.Y_AXIS));
        processingScrollPane = new JScrollPane(processingPanel);
        processingPanel.add(nothing);

        // Queue Panel
        queuePanel = new JPanel();
        queuePanel.setLayout(new BoxLayout(queuePanel,BoxLayout.Y_AXIS));
        queueScrollPane = new JScrollPane(queuePanel);
        queuePanel.setVisible(false);



        // right Panel
        rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel,BoxLayout.Y_AXIS));
        rightScrollPane = new JScrollPane(rightPanel);

        fileNamePanel  = new JPanel(new BorderLayout());
        fileNameLabel = new JLabel("File name : ");
        fileNameLabel.setBackground(Color.GRAY);
        fileName = new JLabel();
        fileNamePanel.add(fileNameLabel,BorderLayout.WEST);
        fileNamePanel.add(fileName,BorderLayout.CENTER);

        statusPanel = new JPanel(new BorderLayout());
        statusLabel = new JLabel("Status : ");
        statusLabel.setBackground(Color.GRAY);
        status = new JLabel();
        statusPanel.add(statusLabel,BorderLayout.WEST);
        statusPanel.add(status,BorderLayout.CENTER);

        sizePanel = new JPanel(new BorderLayout());
        sizeLabel = new JLabel("Size : ");
        sizeLabel.setBackground(Color.GRAY);
        size = new JLabel();
        sizePanel.add(sizeLabel,BorderLayout.WEST);
        sizePanel.add(size,BorderLayout.CENTER);

        createdPanel = new JPanel(new BorderLayout());
        createdLabel = new JLabel("Created : ");
        createdLabel.setBackground(Color.GRAY);
        created = new JLabel();
        createdPanel.add(createdLabel,BorderLayout.WEST);
        createdPanel.add(created,BorderLayout.CENTER);

        modifiedPanel = new JPanel(new BorderLayout());
        modifiedLabel = new JLabel("Modified : ");
        modifiedLabel.setBackground(Color.GRAY);
        modified = new JLabel();
        modifiedPanel.add(modifiedLabel,BorderLayout.WEST);
        modifiedPanel.add(modified,BorderLayout.CENTER);

        addressPanel = new JPanel(new BorderLayout());
        addressLabel = new JLabel("Address : ");
        addressLabel.setBackground(Color.GRAY);
        address = new JLabel();
        addressPanel.add(addressLabel,BorderLayout.WEST);
        addressPanel.add(address,BorderLayout.CENTER);

        rightPanel.add(fileNamePanel);
        rightPanel.add(statusPanel);
        rightPanel.add(sizePanel);
        rightPanel.add(createdPanel);
        rightPanel.add(modifiedPanel);
        rightPanel.add(addressPanel);

        // setting to frame
        background.setJMenuBar(menuBar);
        mainPanel.add(toolBar,BorderLayout.NORTH);
        mainPanel.add(leftPanel,BorderLayout.WEST);
        mainPanel.add(processingScrollPane,BorderLayout.CENTER);
//        mainPanel.add(queueScrollPane,BorderLayout.CENTER);
        background.add(mainPanel);
        mainPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                if(!processingOrder.isEmpty())
                    comfortableResize();
            }
        });
    }

    public void show(){
        background.pack();
        background.setVisible(true);
    }

//    private void SystemTrayHandler(){
//        if(!SystemTray.isSupported()){
//            JOptionPane.showMessageDialog(null,"Opps ... Your system doesn't support system tray.","System tray error",JOptionPane.WARNING_MESSAGE);
//            return;
//        }
//        Image image = Toolkit.getDefaultToolkit().getImage("Java Code/favicon (1).ico");
//        newDownloadPop.addActionListener(actionListener);
//        settingPop.addActionListener(actionListener);
//        exitPop.addActionListener(actionListener);
//        trayPopupMenu.addActionListener(actionListener);
//
//
//        // adding elements to trayPopupMenu
//        trayPopupMenu.add(newDownloadPop);
//        trayPopupMenu.add(pausePop);
//        trayPopupMenu.add(resumePop);
//        trayPopupMenu.add(settingPop);
//        trayPopupMenu.add(exitPop);
//        trayIcon = new TrayIcon(image,"GDM The Fastest Download Manager",trayPopupMenu);
//        trayIcon.setImageAutoSize(true);
//        try{
//            systemTray.add(trayIcon);
//        }catch(AWTException awtException){
//            JOptionPane.showMessageDialog(background,"Ops.. Something went wrong","Error",JOptionPane.ERROR_MESSAGE);
//        }
//    }


    private class MainFrameActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(setting) ) {
                Manager.getAction("setting.show");
            }
            else if (e.getSource().equals(settingItem)){
                Manager.getAction("setting.show");
            }
            else if (e.getSource().equals(newDownload)){
                Manager.getAction("newDownload.show");
            }
            else if(e.getSource().equals(newDownloadItem)){
                Manager.getAction("newDownload.show");
            }
            else if (e.getSource().equals(exitItem)){
                System.exit(0);
            }

            else if(e.getSource().equals(processing)){
                mainPanel.remove(queueScrollPane);
                mainPanel.add(processingScrollPane,BorderLayout.CENTER);
                background.revalidate();
                mainPanel.revalidate();
//                comfortableResize();
//                comfortableResize();
            }
            else if(e.getSource().equals(completed)){
                queuePanel.setVisible(false);
                processingPanel.setVisible(false);
            }
            else if(e.getSource().equals(queue)){
                mainPanel.remove(processingScrollPane);
                mainPanel.add(queueScrollPane,BorderLayout.CENTER);
                background.revalidate();
                mainPanel.revalidate();
            }
            else if(e.getSource().equals(cancel)){
                ListIterator<NewDownloadPanel> iterator = processingNewDownloads.listIterator();
                while(iterator.hasNext()){
                    NewDownloadPanel tmp = (NewDownloadPanel) iterator.next();
                    if(tmp.isSelected()) {
                        iterator.next().deleteFileProperties();
                        iterator.remove();
                    }
                }
                mainPanel.revalidate();
                background.revalidate();
                comfortableResize();
            }
        }

    }

    private class MouseImplement implements MouseListener{

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            if(e.getSource().equals(sort)){
                sortPopUp.show(e.getComponent(),e.getX(),e.getY());
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

    public void setNewDownload(FileProperties fileProperties) {
        if(processingPanel.isAncestorOf(nothing)){
            processingPanel.remove(nothing);
            SwingUtilities.updateComponentTreeUI(processingPanel);
        }
        processingOrder.put(fileProperties.getCreated(), fileProperties);
        NewDownloadPanel tmp = new NewDownloadPanel(fileProperties, (int)processingPanel.getSize().getWidth());
        processingPanel.add(tmp.getPanel());
        processingNewDownloads.add(tmp);
        background.revalidate();
        mainPanel.revalidate();
    }

    public void setNewDownloadQueue(FileProperties fileProperties){
        queueOrder.put(fileProperties.getCreated(),fileProperties);
        if(Manager.getNumberOfDownloads().equals("infinitive")){
            processingOrder.put(fileProperties.getCreated(),fileProperties);
        }
        else if ( numberOfAddedToProcessing < Integer.parseInt(Manager.getNumberOfDownloads())){
            setNewDownload(fileProperties);
        }
        NewDownloadPanel tmp = new NewDownloadPanel(fileProperties,(int)processing.getSize().getWidth());
        queuePanel.add(tmp.getPanel());
        queueNewDownload.add(tmp);
        background.revalidate();
        mainPanel.revalidate();
    }


    public Dimension getCentralPanelSize(){
        return processingPanel.getSize();
    }


    public void showRightPanel (FileProperties fileProperties){
        fileName.setText(fileProperties.getFileName());
        status.setText(fileProperties.getStatus());
        size.setText(fileProperties.getSize());
        created.setText(fileProperties.getCreated());
        modified.setText(fileProperties.getModified());
        address.setText(fileProperties.getAddress());
        mainPanel.add(rightScrollPane,BorderLayout.EAST);
        background.revalidate();
        mainPanel.revalidate();
    }

    public void hideRightPanel(){
        BorderLayout layout = (BorderLayout) mainPanel.getLayout();
        mainPanel.remove(layout.getLayoutComponent(BorderLayout.EAST));
        background.revalidate();
        mainPanel.revalidate();
        comfortableResize();
    }

    private void comfortableResize(){
        for (NewDownloadPanel item : processingNewDownloads) {
            item.setSize(processingPanel.getWidth());
        }
        SwingUtilities.updateComponentTreeUI(processingPanel);
    }

    private void updateProcessingPanel(){
        processingPanel.removeAll();
        processingPanel.setLayout(new BoxLayout(processingPanel,BoxLayout.Y_AXIS));
        SwingUtilities.updateComponentTreeUI(processingPanel);
        if(processingNewDownloads.isEmpty()){
            processingPanel.add(nothing);
        }
        else {
            for (NewDownloadPanel item : processingNewDownloads) {
                processingPanel.add(item.getPanel());
            }
        }
    }

    public void updateDownloads(){
        Iterator iterator = processingNewDownloads.iterator();
        while(iterator.hasNext()){
            NewDownloadPanel tmp =(NewDownloadPanel)iterator.next();
            if(tmp.getFileProperties() == null) {
                iterator.remove();
            }
        }
        updateProcessingPanel();
    }

}
