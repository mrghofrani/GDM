import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MainFrame {
//    private static MainFrame mainFrame;
    private JFrame background;
    private JPanel mainPanel;
    private MainFrameActionListener actionListener;
    private CheckListener checkListener = new CheckListener();
    private MouseImplement mouseListener = new MouseImplement();
    private ArrayList<NewDownloadPanel> processingNewDownloads = new ArrayList<>();
    private ArrayList<NewDownloadPanel> queueNewDownloads = new ArrayList<>();
    private ArrayList<NewDownloadPanel> completedDownloads = new ArrayList<>();
    private ArrayList<NewDownloadPanel> holder;
    private int numberOfAddedToProcessing = 0;
    private FileInputStream fileInputStream;
    private FileOutputStream fileOutputStream;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private LinkedList<String> sortFactors = new LinkedList<>();
    private final String PROCESSING_PATH = "files/list.gdm";
    private final String REMOVED_PATH = "files/removed.gdm";
    private final String QUEUE_PATH = "files/queue.gdm";


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
//    private JPanel topPanel;
    private final int BUTTON_SIZE_ON_TOP_PANEL = 40;
    private JButton newDownload;
    private JButton pause;
    private JButton resume;
    private JButton cancel;
    private JButton remove;
    private JButton sort;
    private JToolBar toolBar;
    private JCheckBox byDate;
    private JCheckBox byStatus;
    private JCheckBox byName;
    private JCheckBox bySize;
    private JButton ascendingButton;
    private JButton descendingButton;
//    private ButtonGroup sortBy;
    private JPopupMenu sortPopUp;
    private JTextField searchText;

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

    private JPanel completePanel;
    private JScrollPane completeScrollPane;

    public MainFrame() {
        background = new JFrame("GDM");
        background.setIconImage(Toolkit.getDefaultToolkit().getImage("GDM-logo.jpg"));
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(5,5));
        actionListener = new MainFrameActionListener();

        leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel,BoxLayout.Y_AXIS));
        processing = new JButton("Processing");
        processing.setIcon(new ImageIcon("time-left.png"));
        processing.addActionListener(actionListener);
        int buttonWidth = processing.getPreferredSize().width + 100;
        completed = new JButton("completed");
        completed.setIcon(new ImageIcon("correct-mark.png"));
        completed.setMaximumSize(new Dimension(buttonWidth,completed.getPreferredSize().height));
        completed.addActionListener(actionListener);
        queue = new JButton("Queue");
        queue.setMaximumSize(new Dimension(buttonWidth,queue.getPreferredSize().height));
        queue.setIcon(new ImageIcon("rectangles.png"));
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
        remove.addActionListener(actionListener);
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
        byDate = new JCheckBox("By Date");
        byDate.addItemListener(checkListener);
        bySize = new JCheckBox("By Size");
        bySize.addItemListener(checkListener);
        byName = new JCheckBox("By Name");
        byName.addItemListener(checkListener);
        byStatus = new JCheckBox("By Status");
        byStatus.addItemListener(checkListener);
        ascendingButton = new JButton("Ascending");
        ascendingButton.addActionListener(actionListener);
        descendingButton = new JButton("Descending");
        descendingButton.addActionListener(actionListener);
        sortPopUp.add( byDate);
        sortPopUp.add( bySize);
        sortPopUp.add( byName);
        sortPopUp.add( byStatus);
        sortPopUp.add(ascendingButton);
        sortPopUp.add(descendingButton);
        descendingButton.addActionListener(actionListener);
        ascendingButton.addActionListener(actionListener);


        searchText = new JTextField("Search Here ...");
        searchText.setAlignmentY(JComponent.RIGHT_ALIGNMENT);
        searchText.setPreferredSize(new Dimension((int)searchText.getPreferredSize().getWidth(),(int)searchText.getPreferredSize().getHeight()));
        searchText.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                searchText.getDocument().removeDocumentListener(actionListener);
                if(mainPanel.isAncestorOf(processingPanel))
                    holder = new ArrayList<>(processingNewDownloads);
                else if(mainPanel.isAncestorOf(queuePanel))
                    holder = queueNewDownloads;
                searchText.setText("");
                searchText.getDocument().addDocumentListener(actionListener);
            }

            @Override
            public void focusLost(FocusEvent e) {
                searchText.getDocument().removeDocumentListener(actionListener);// To not throw a event
                if(mainPanel.isAncestorOf(processingPanel)) {
                    processingNewDownloads = new ArrayList<>(holder);
                    updateProcessingDownloads();
                }
                else if(mainPanel.isAncestorOf(queuePanel)) {
                    queueNewDownloads = new ArrayList<>(holder);
                    updateQueuePanel();
                }
                searchText.setText("Search Here");
                searchText.getDocument().addDocumentListener(actionListener);
            }
        });

        toolBar = new JToolBar();
        toolBar.add(title);
        toolBar.add(newDownload);
        toolBar.add(resume);
        toolBar.add(pause);
        toolBar.add(cancel);
        toolBar.add(remove);
        toolBar.add(setting);
        toolBar.add(sort);
        toolBar.add(searchText);

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
        processingPanel.setLayout(new BoxLayout(processingPanel,BoxLayout.Y_AXIS));
        nothing = new JLabel(" <html> Nothing is here :) <br/> Hit the download button. </html>",SwingUtilities.CENTER);
        nothing.setHorizontalTextPosition(JLabel.CENTER);
        nothing.setVerticalTextPosition(JLabel.BOTTOM);
        nothing.setForeground(Color.GRAY);
        ImageIcon imageIcon = new ImageIcon("sunbed.png"); // load the image to a imageIcon
        Image image = imageIcon.getImage(); // transform it
        Image newImage = image.getScaledInstance(120, 120,  Image.SCALE_DEFAULT); // scale it the smooth way
        imageIcon = new ImageIcon(newImage);  // transform it back
        nothing.setIcon(imageIcon);
        nothing.setAlignmentX(Component.CENTER_ALIGNMENT);
        processingScrollPane = new JScrollPane(processingPanel);
        processingPanel.add(nothing);

        // Complete Panel
        completePanel = new JPanel();
        completePanel .setLayout(new BoxLayout(completePanel,BoxLayout.Y_AXIS));
        completeScrollPane = new JScrollPane(completePanel);
        completePanel.setVisible(true);

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
        background.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                comfortableResize();
            }
        });
    }

    public void show(){
        background.pack();
        background.setVisible(true);
    }

    public void setNewDownload(FileProperties fileProperties) {
        if(processingPanel.isAncestorOf(nothing)){
            processingPanel.remove(nothing);
            SwingUtilities.updateComponentTreeUI(processingPanel);
        }
        NewDownloadPanel tmp = new NewDownloadPanel(fileProperties, (int)processingPanel.getSize().getWidth());
        processingPanel.add(tmp.getPanel());
        processingNewDownloads.add(tmp);
        tmp.startDownload();
        background.revalidate();
        mainPanel.revalidate();
    }

    /**
     * This method builds a String from removed files then
     * writes it into a file
     * @param removedFiles file that are to be written into a text file
     */
    private void backupRemovedFiles(ArrayList<FileProperties> removedFiles){
        File file = new File(REMOVED_PATH);
        ArrayList<String> orderedText = new ArrayList<>();
        String tmpString;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        try(BufferedWriter writer =  new BufferedWriter(new FileWriter(file,true))){
            for (FileProperties item: removedFiles) {
                tmpString = "\n";
                tmpString += "****** Removed On Date " + dtf.format(now) + "******" + "\n";
                tmpString += "File Properties" + "\n";
                tmpString += "File Name : " + item.getFileUrl() + "\n";
                tmpString += "File Created Time :" +item.getCreated() + "\n";
                tmpString += "File Size : "    + item.getSize() + "\n";
                tmpString += "File Download Address : " +item.getAddress() + "\n";
                tmpString += "File Status At While Removing : " + item.getStatus() + "\n";
                orderedText.add(tmpString);
            }
            for (String item: orderedText)
                writer.write(item);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setNewDownloadQueue(FileProperties fileProperties){
        boolean keepGoing = false;
        if(Manager.getNumberOfDownloads().equals("infinitive") ){
            keepGoing = true;
        }
        else if ( numberOfAddedToProcessing < Integer.parseInt(Manager.getNumberOfDownloads())){
            keepGoing = true;
            numberOfAddedToProcessing++;
        }
        NewDownloadPanel tmp = new NewDownloadPanel(fileProperties,(int)processingPanel.getSize().getWidth());
        queuePanel.remove(nothing);
        queuePanel.add(tmp.getPanel());
        queueNewDownloads.add(tmp);
        if(keepGoing) {
            processingNewDownloads.add(tmp);
            tmp.startDownload();
        }
        updateQueuePanel();
        updateProcessingDownloads();
        SwingUtilities.updateComponentTreeUI(mainPanel);
        SwingUtilities.updateComponentTreeUI(queuePanel);
    }


    public void showRightPanel (FileProperties fileProperties){
        fileName.setText(fileProperties.getFileUrl());
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
        if(mainPanel.isAncestorOf(processingPanel)) {
            for (NewDownloadPanel item : processingNewDownloads)
                item.setSize(processingPanel.getWidth());
            processingPanel.revalidate();
        }
        if(mainPanel.isAncestorOf(queuePanel)) {
            for (NewDownloadPanel item : queueNewDownloads)
                item.setSize(queuePanel.getWidth());
            SwingUtilities.updateComponentTreeUI(queuePanel);
        }
        if(mainPanel.isAncestorOf(completePanel)){
            for (NewDownloadPanel item: completedDownloads)
                item.setSize(completePanel.getWidth());
            SwingUtilities.updateComponentTreeUI(completePanel);
        }
    }

    /**
     * This method at first clears the processingPanel from any element
     * then adds the new Downloads to the Panel if the processingNewDownload
     * doesn't contain any element adds nothing Label to the Panel
     */
    private void updateProcessingPanel() {
        processingPanel.removeAll();
        processingPanel.setLayout(new BoxLayout(processingPanel, BoxLayout.Y_AXIS));
        SwingUtilities.updateComponentTreeUI(processingPanel);
            if (processingNewDownloads.isEmpty()) {
                processingPanel.add(nothing);
            } else {
                for (NewDownloadPanel item : processingNewDownloads) {
                    processingPanel.add(item.getPanel());
                }
            }
            comfortableResize();
    }

    private void updateQueueDownloads(){
        Iterator iterator = queueNewDownloads.iterator();
        while(iterator.hasNext()){
            NewDownloadPanel tmp =(NewDownloadPanel)iterator.next();
            if(tmp.getFileProperties() == null) {
                iterator.remove();
            }
        }
        updateQueuePanel();
    }
    /**
     * This method at first clears the processingNewDownloads
     * from null values then invokes the updateProcessingPanel
     */
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

    /**
     * This method repaints the QueuePanel
     * at first it clears the queue Panel then
     * add the queueNewDownloads to the panel
     */
    private void updateQueuePanel(){
        queuePanel.removeAll();
        queuePanel.setLayout(new BoxLayout(queuePanel,BoxLayout.Y_AXIS));
        if(queueNewDownloads.isEmpty()){
            queuePanel.add(nothing);
        }
        else {
            for (NewDownloadPanel item : queueNewDownloads) {
                queuePanel.add(item.getPanel());
            }
        }
        SwingUtilities.updateComponentTreeUI(queuePanel);
    }

    /**
     * This method always invoked when a download is
     * completed and needs to move into completed Part
     */
    public void updateCompleted(){
        Iterator<NewDownloadPanel> iteratorProcessing = processingNewDownloads.iterator();
        Iterator<NewDownloadPanel> iteratorQueue;
        NewDownloadPanel tmp;
        while(iteratorProcessing.hasNext()){
            tmp = iteratorProcessing.next();
            if(tmp.isCompleted()){
                completedDownloads.add(tmp);
                iteratorQueue = queueNewDownloads.iterator();
                while(iteratorQueue.hasNext()) {
                    if (iteratorQueue.next() == tmp)
                        iteratorQueue.remove();
                }
                iteratorProcessing.remove();
            }
        }
        updateCompletedDownloads();
        updateQueueDownloads();
        updateProcessingDownloads();
    }

    /**
     * This method checks whether a download
     * has been deleted or not. if it was deleted
     * deletes it from completedDownload list
     */
    private void updateCompletedDownloads(){
        Iterator iterator = completedDownloads.iterator();
        while(iterator.hasNext()){
            NewDownloadPanel tmp =(NewDownloadPanel)iterator.next();
            if(tmp.getFileProperties() == null)
                iterator.remove();
        }
        updateCompletedPanel();
    }

    /**
     * This method update the completed Panel. According to completedDownload list
     * if the list was empty adds the rest logo.
     */
    private void updateCompletedPanel(){
        completePanel.removeAll();
        completePanel.setLayout(new BoxLayout(completePanel,BoxLayout.Y_AXIS));
        if(completedDownloads.isEmpty()){
            completePanel.add(nothing);
        }
        else {
            for (NewDownloadPanel item : completedDownloads)
                completePanel.add(item.getPanel());
        }
        SwingUtilities.updateComponentTreeUI(completePanel);
    }

    /**
     * when the programme is going to terminate this
     * method is invoked by the Manager to get a backup
     * from data that is showing to the user. The backed up
     * data is ArrayList of FileProperties.
     */
    public void backup(){
        // Backing up processing downloads
        try{
            FileOutputStream fileOutputStream = new FileOutputStream(PROCESSING_PATH,false);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.flush();
            ArrayList<FileProperties> files = new ArrayList<>();
            for (NewDownloadPanel item: processingNewDownloads)
                files.add(item.getFileProperties());
            objectOutputStream.writeObject(files);
            objectOutputStream.flush();
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Backing up queue downloads
        try{
            FileOutputStream fileOutputStream = new FileOutputStream(QUEUE_PATH,false);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            ArrayList<FileProperties> files = new ArrayList<>();
            for (NewDownloadPanel item: queueNewDownloads)
                files.add(item.getFileProperties());
            objectOutputStream.writeObject(files);
            objectOutputStream.flush();
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * this method at first evaluates the whether the
     * desired file is exists, then reads the object and
     * build the FileProperties ArrayList and by using that
     * creates the download panels. at the end it invokes the
     * comfortableResize and updateProcessingPanel method to have a good GUI.
     */
    public void initialize(){
        File file = new File(PROCESSING_PATH);
        if(file.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(PROCESSING_PATH);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                ArrayList<FileProperties> downloadPanels = (ArrayList<FileProperties>) objectInputStream.readObject();
                NewDownloadPanel tmp;
                for (FileProperties item : downloadPanels) {
                    tmp = new NewDownloadPanel(item, processingPanel.getWidth());
                    processingNewDownloads.add(tmp);
                    tmp.startDownload();
                }
                comfortableResize();
                updateProcessingPanel();
                fileInputStream.close();
                objectInputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        file = new File(QUEUE_PATH);
        if(file.exists()){
            try{
                FileInputStream fileInputStream = new FileInputStream(QUEUE_PATH);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                ArrayList<FileProperties> downloadPanels = (ArrayList) objectInputStream.readObject();
                for (FileProperties item : downloadPanels) {
                    queueNewDownloads.add(new NewDownloadPanel(item, processingPanel.getWidth()));
                }
                comfortableResize();
                updateProcessingPanel();
                fileInputStream.close();
                objectInputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void sort(){
        if(!sortFactors.isEmpty()) {
            ArrayList<NewDownloadPanel> downloadFiles = new ArrayList<>();
            if (mainPanel.isAncestorOf(processingPanel)) {
                for (NewDownloadPanel item : processingNewDownloads)
                    downloadFiles.add(item);
            } else if (mainPanel.isAncestorOf(queuePanel)) {
                for (NewDownloadPanel item : queueNewDownloads)
                    downloadFiles.add(item);
            }
            TreeMap<String, NewDownloadPanel> sortHashMap = new TreeMap<>();
            for (NewDownloadPanel file : downloadFiles)
                sortHashMap.put(file.getFileProperties().get(sortFactors.getFirst()), file);
            if (sortFactors.size() != 1) {
                for (int i = 1; i<sortFactors.size() ; i++) {
                    for (int j = 0; j < downloadFiles.size(); j++) {
                        if(downloadFiles.get(j).getFileProperties().get(sortFactors.get(i-1)).equals(downloadFiles.get(j+1).getFileProperties().get(sortFactors.get(i-1)))){
                            if(downloadFiles.get(j).getFileProperties().get(sortFactors.get(i-1)).compareTo(downloadFiles.get(j+1).getFileProperties().get(sortFactors.get(i-1)))>0) // Ascending order
                                Collections.swap(downloadFiles,j,j+1);
                        }
                    }
                }
            }
            if (mainPanel.isAncestorOf(processingPanel)) {
                processingNewDownloads = new ArrayList<>(downloadFiles);
                updateProcessingDownloads();
            } else if (mainPanel.isAncestorOf(queuePanel)) {
                queueNewDownloads = new ArrayList<>(downloadFiles);
                updateQueuePanel();
            }
        }
    }

    private class MainFrameActionListener implements ActionListener,DocumentListener {
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
            else if(e.getSource().equals(ascendingButton)){
                if(mainPanel.isAncestorOf(processingPanel)){
                    System.out.println("processing Panel " + " ascending Item");;
                    Collections.reverse(processingNewDownloads);
                    updateProcessingPanel();
                }
                else if(mainPanel.isAncestorOf(queuePanel)){
                    System.out.println("queuePanel " + " ascending Item");
                    Collections.reverse(queueNewDownloads);
                    updateQueuePanel();
                }
            }
            else if(e.getSource().equals(descendingButton)) {
                if(mainPanel.isAncestorOf(processingPanel)){
                    System.out.println("processing Panel " + " descending Item");;
                    Collections.reverse(processingNewDownloads);
                    updateProcessingPanel();
                }
                else if(mainPanel.isAncestorOf(queuePanel)){
                    System.out.println("queuePanel " + " descending Item");
                    Collections.reverse(queueNewDownloads);
                    updateQueuePanel();
                }
            }
            else if(e.getSource().equals(processing)){
                BorderLayout layout = (BorderLayout)mainPanel.getLayout();
                mainPanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
                mainPanel.add(processingScrollPane, BorderLayout.CENTER);
                updateProcessingDownloads();
                SwingUtilities.updateComponentTreeUI(mainPanel);
            }
            else if(e.getSource().equals(completed)){
                BorderLayout layout = (BorderLayout)mainPanel.getLayout();
                mainPanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
                mainPanel.add(completeScrollPane,BorderLayout.CENTER);
                updateCompletedDownloads();
                SwingUtilities.updateComponentTreeUI(mainPanel);
            }
            else if(e.getSource().equals(queue)){
                BorderLayout layout = (BorderLayout)mainPanel.getLayout();
                mainPanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
                mainPanel.add(queueScrollPane, BorderLayout.CENTER);
                updateQueueDownloads();
                SwingUtilities.updateComponentTreeUI(mainPanel);
            }
            else if(e.getSource().equals(cancel)){
                boolean  completely = false;
                Iterator iterator;
                Object[] options = {"Yes, please", "And also delete the file", "No, keep the file"};
                ImageIcon tmp = new ImageIcon("cross.png");
                int n = JOptionPane.showOptionDialog(background, "Would you like to delete this download?", "Delete", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, tmp, options,options[2]);
                switch(n){
                    case 1:
                        completely = true;
                    case 0:
                        JPanel tmpPanel;
                        ArrayList<NewDownloadPanel> tmpNewDownloads = new ArrayList<>();
                        ArrayList<FileProperties> removed = new ArrayList<>();
                        if(mainPanel.isAncestorOf(processingPanel)){
                            tmpPanel = processingPanel;
                            tmpNewDownloads = processingNewDownloads;
                        }
                        else if(mainPanel.isAncestorOf(queuePanel)){
                            tmpPanel = queuePanel;
                            tmpNewDownloads = queueNewDownloads;
                        }

                        if (tmpNewDownloads.isEmpty())
                            JOptionPane.showMessageDialog(background, "Nothing to delete!", "Delete", JOptionPane.WARNING_MESSAGE);
                        else {
                            boolean found = false;
                            iterator = tmpNewDownloads.iterator();
                            while (iterator.hasNext()) {
                                NewDownloadPanel item = (NewDownloadPanel) iterator.next();
                                if (item.isSelected()) {
                                    found = true;
                                    if(completely)
                                        item.getFile().delete();
                                    removed.add(item.getFileProperties());
                                    (item).deleteFileProperties();
                                    iterator.remove();
                                    break;
                                }
                            }
                            if (found) {
                                while (iterator.hasNext()) {
                                    NewDownloadPanel item = (NewDownloadPanel) iterator.next();
                                    if (item.isSelected()) {
                                        removed.add(item.getFileProperties());
                                        if(completely)
                                            item.getFile().delete();
                                        item.deleteFileProperties();
                                        iterator.remove();
                                    }
                                }
                                backupRemovedFiles(removed);
                                if(mainPanel.isAncestorOf(queuePanel))
                                    queueNewDownloads = tmpNewDownloads;
                                if(mainPanel.isAncestorOf(processingPanel))
                                    processingNewDownloads = tmpNewDownloads;
                                updateProcessingDownloads();
                                updateQueueDownloads();
                            }
                            else {
                                JOptionPane.showMessageDialog(background, "Nothing was selected,\n please select something then press cancel button", "Delete", JOptionPane.WARNING_MESSAGE);
                            }
                        }

                        break;
                    default:
                        // Do nothing
                        break;
                }
                updateProcessingDownloads();
                mainPanel.revalidate();
                background.revalidate();
            }
            else if(e.getSource().equals(searchText)){
                searchText.setText("");
                System.out.println("set");
            }
            else if(e.getSource().equals(remove)){
                boolean completely = false;
                Object[] options = {"Yes, please", "And also delete the file", "No, keep the file"};
                ImageIcon tmp = new ImageIcon("cross.png");
                int n = JOptionPane.showOptionDialog(background, "Would you like to delete Whole download including queue and processing and completed?", "Delete", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, tmp, options,options[2]);
                switch (n){
                    case 1:
                        completely = true;
                    case 0:
                        for (NewDownloadPanel item:processingNewDownloads) {
                            if(completely)
                                item.getFile().delete();
                            item.deleteFileProperties();
                        }
                        processingNewDownloads.clear();
                        for (NewDownloadPanel item: queueNewDownloads) {
                            if(completely)
                                item.getFile().delete();
                            item.deleteFileProperties();
                        }
                        queueNewDownloads.clear();
                        break;

                    default:
                        // Do nothing
                        break;
                }
                updateQueuePanel();
                updateProcessingPanel();
            }
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            String key = searchText.getText();
            if (mainPanel.isAncestorOf(processingPanel)) {
                processingNewDownloads.clear();
                for (NewDownloadPanel item : holder) {
                    if (item.getFileProperties().getFileUrl().contains(key)) {
                        processingNewDownloads.add(item);
                    }
                }
                updateProcessingPanel();
            } else if (mainPanel.isAncestorOf(queuePanel)) {
                queueNewDownloads.clear();
                for (NewDownloadPanel item : holder) {
                    if (item.getFileProperties().getFileUrl().contains(key)) {
                        queueNewDownloads.add(item);
                    }
                }
                updateQueuePanel();
            }
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            String key = searchText.getText();
            if(key.isEmpty()){
                System.out.println("nothing");
                if(mainPanel.isAncestorOf(processingPanel)) {
                    processingNewDownloads = new ArrayList<>(holder);
                    updateProcessingPanel();
                }
                else if(mainPanel.isAncestorOf(queuePanel)) {
                    queueNewDownloads = new ArrayList<>(holder);
                    updateQueuePanel();
                }
            }
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
        }
    }

    private class MouseImplement implements MouseListener{

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            if(e.getSource().equals(sort))
                sortPopUp.show(e.getComponent(),e.getX(),e.getY());
            ascendingButton.doClick();
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
    private class CheckListener implements  ItemListener{

        @Override
        public void itemStateChanged(ItemEvent e) {
            // For byDate
            if(byDate.isSelected())
                sortFactors.add("date");
            else
                sortFactors.remove("date");

            // For bySize
            if(bySize.isSelected())
                sortFactors.add("size");
            else
                sortFactors.remove("date");

            // For byName
            if(byName.isSelected())
                sortFactors.add("name");
            else
                sortFactors.remove("name");

            // For byStatus
            if(byStatus.isSelected())
                sortFactors.add("status");
            else
                sortFactors.remove("status");
            sort();
        }
    }
}
