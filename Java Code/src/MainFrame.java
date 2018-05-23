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
        aboutItem.addActionListener(actionListener);

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
        queuePanel.setVisible(true);

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

    private class MainFrameActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(setting) || e.getSource().equals(settingItem) ) {
                Manager.getAction("setting.show");
            }
            else if (e.getSource().equals(newDownload) || e.getSource().equals(newDownloadItem)){
                Manager.getAction("newDownload.show");
            }
            else if (e.getSource().equals(exitItem)){
                Manager.safelyExit();
            }
            else if(e.getSource().equals(aboutItem)){
                Manager.showAbout();
            }
            else if(e.getSource().equals(processing)){
                BorderLayout layout = (BorderLayout)mainPanel.getLayout();
                mainPanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
                mainPanel.add(processingScrollPane, BorderLayout.CENTER);
                SwingUtilities.updateComponentTreeUI(mainPanel);
                updateProcessingPanel();
            }
            else if(e.getSource().equals(completed)){
                queuePanel.setVisible(false);
                processingPanel.setVisible(false);
            }
            else if(e.getSource().equals(queue)){
                BorderLayout layout = (BorderLayout)mainPanel.getLayout();
                mainPanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
                mainPanel.add(queueScrollPane, BorderLayout.CENTER);
                SwingUtilities.updateComponentTreeUI(mainPanel);
                updateQueuePanel();
            }
            else if(e.getSource().equals(cancel)){ // TODO: I should implement the cancel button working for each panel that is open
                Iterator iterator = processingNewDownloads.iterator();
                Object[] options = {"Yes, please", "And also delete the file", "No, keep the file"};
                ImageIcon tmp = new ImageIcon("cross.png");
                int n = JOptionPane.showOptionDialog(background, "Would you like to delete this download?", "Delete", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, tmp, options,options[2]);
                switch(n){
                    case 0:
                        if (processingNewDownloads.isEmpty())
                            JOptionPane.showMessageDialog(background, "Nothing to delete!", "Delete", JOptionPane.WARNING_MESSAGE);
                        else {
                            boolean found = false;

                            while (iterator.hasNext()) {
                                NewDownloadPanel item = (NewDownloadPanel) iterator.next();
                                if (item.isSelected()) {
                                    found = true;
                                    iterator.remove();
                                    break;
                                }
                            }
                            if (found) {
                                while (iterator.hasNext()) {
                                    NewDownloadPanel item = (NewDownloadPanel) iterator.next();
                                    if (item.isSelected()) {
                                        item.deleteFileProperties();
                                        iterator.remove();
                                    }
                                }
                                updateProcessingDownloads();
                            }
                            else {
                                JOptionPane.showMessageDialog(background, "Nothing was selected,\n please select something then press cancel button", "Delete", JOptionPane.WARNING_MESSAGE);
                            }
                        }
                        break;
                    case 1:
                        // TODO: I should implement the file delete
                        break;
                    default:
                        // Do nothing
                        break;
                }
                updateProcessingDownloads();
                mainPanel.revalidate();
                background.revalidate();
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
        boolean keepGoing = false;
        if(Manager.getNumberOfDownloads().equals("infinitive") ){
//            queueOrder.put(fileProperties.getCreated(),fileProperties);
            keepGoing = true;
        }
        else if ( numberOfAddedToProcessing < Integer.parseInt(Manager.getNumberOfDownloads())){
//            setNewDownload(fileProperties);
//            queueOrder.put(fileProperties.getCreated(),fileProperties);
            keepGoing = true;
            numberOfAddedToProcessing++;
        }
//        queueOrder.put(fileProperties.getCreated(),fileProperties);
        if(keepGoing){
            NewDownloadPanel tmp = new NewDownloadPanel(fileProperties,(int)processingPanel.getSize().getWidth());
            queuePanel.remove(nothing);
            queuePanel.add(tmp.getPanel());
            queueNewDownload.add(tmp);
        }
        else {
            JOptionPane.showMessageDialog(background,"Maximum number of queue reached!","Queue",JOptionPane.WARNING_MESSAGE);
        }
        SwingUtilities.updateComponentTreeUI(mainPanel);
        SwingUtilities.updateComponentTreeUI(queuePanel);
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
        for (NewDownloadPanel item: queueNewDownload) {
            item.setSize(queuePanel.getWidth());
        }
        SwingUtilities.updateComponentTreeUI(processingPanel);
        SwingUtilities.updateComponentTreeUI(queuePanel);
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

    public void updateProcessingDownloads(){
        Iterator iterator = processingNewDownloads.iterator();
        while(iterator.hasNext()){
            NewDownloadPanel tmp =(NewDownloadPanel)iterator.next();
            if(tmp.getFileProperties() == null) {
                iterator.remove();
            }
        }
        updateProcessingPanel();
    }

    private void updateQueuePanel(){
        queuePanel.removeAll();
        queuePanel.setLayout(new BoxLayout(queuePanel,BoxLayout.Y_AXIS));
        if(queueNewDownload.isEmpty()){
            queuePanel.add(nothing);
        }
        else {
            for (NewDownloadPanel item : queueNewDownload) {
                queuePanel.add(item.getPanel());
            }
        }
        SwingUtilities.updateComponentTreeUI(queuePanel);
    }
}
