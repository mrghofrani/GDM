
import javax.net.ssl.HttpsURLConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewDownloadPanel implements Serializable {
    private FileProperties fileProperties;
    private File file;
    private JPanel newDownloadPanel;
    private ActionHandler actionHandler = new ActionHandler();
    private boolean selected = false;
    private boolean isPaused = false;
    private boolean completed = false;
    private Worker worker;

    private final ArrayList<String> UNITS = new ArrayList<>(Arrays.asList("Bytes" , "KB","MB", "GB"));
    private double downloadedValue = 0.00;
    private final int BUFFER_SIZE = 1024;

    // top part of the newDownload Panel
    private JPanel textArea;
    private JLabel nameField;
    private final int BUTTON_SIZE_ON_TOP_PANEL = 20;


    // central part  of the NewDownloadPanel
    private JPanel centralPanel;

    private JPanel topSideOfCentral;
    private JProgressBar progressBar;

    //bottom part of the NewDownloadPanel
    private JPanel bottomSideOfCentral;

    private JPanel rightBottomSideOfCentral;
    private JButton resumeButton;
    private ImageIcon resumeIcon;
    private JButton cancelButton;
    private JButton directoryButton;

    private JPanel leftSideOftOfCentral;
    private JLabel volumeField;
    private JLabel speedField;
    private JLabel statusField;

    // right Panel
    private JPanel rightPanel;
    private JButton informationButton;
    private ImageIcon right;
    private ImageIcon left;


    // Left side
    private JPanel leftPanel;
    private JLabel downloadImage;


    public NewDownloadPanel(FileProperties properties, int width) {
        fileProperties = properties;
        file = new File(fileProperties.getAddress(),fileProperties.getFileName());
        newDownloadPanel = new JPanel();
        newDownloadPanel.setLayout(new BorderLayout(5,5));

        // TextArea related Panel
        textArea = new JPanel(new BorderLayout(5,5));
        nameField = new JLabel(properties.getFileUrl());
        nameField.setAlignmentY(Component.LEFT_ALIGNMENT);
        textArea.add(nameField);


        // central part of the NewDownloadPanel
        centralPanel = new JPanel();
        centralPanel.setLayout(new BoxLayout(centralPanel,BoxLayout.Y_AXIS));

        // top side of central panel
        topSideOfCentral = new JPanel();
        progressBar = new JProgressBar(JProgressBar.HORIZONTAL,0,100);
        progressBar.setStringPainted(true);
        progressBar.setAlignmentY(Component.LEFT_ALIGNMENT);
        centralPanel.add(progressBar);

        // bottom Part of the NewDownloadPanel
        bottomSideOfCentral = new JPanel(new BorderLayout(5,5));
        rightBottomSideOfCentral = new JPanel();
        rightBottomSideOfCentral.setLayout(new BoxLayout(rightBottomSideOfCentral,BoxLayout.X_AXIS));
        resumeIcon = new ImageIcon("resume.png");
        resumeButton = new JButton(resumeIcon);
        resumeButton.setMaximumSize(new Dimension(BUTTON_SIZE_ON_TOP_PANEL,BUTTON_SIZE_ON_TOP_PANEL));
        resumeButton.addActionListener(actionHandler);
        cancelButton = new JButton(new ImageIcon("cancel.png"));
        cancelButton.setMaximumSize(new Dimension(BUTTON_SIZE_ON_TOP_PANEL,BUTTON_SIZE_ON_TOP_PANEL));
        cancelButton.addActionListener(actionHandler);
        directoryButton = new JButton(new ImageIcon("folder.png"));
        directoryButton.setMaximumSize(new Dimension(BUTTON_SIZE_ON_TOP_PANEL,BUTTON_SIZE_ON_TOP_PANEL));
        directoryButton.addActionListener(actionHandler);
        rightBottomSideOfCentral.add(resumeButton);
        rightBottomSideOfCentral.add(Box.createRigidArea(new Dimension(5,0)));
        rightBottomSideOfCentral.add(cancelButton);
        rightBottomSideOfCentral.add(Box.createRigidArea(new Dimension(5,0)));
        rightBottomSideOfCentral.add(directoryButton);

        leftSideOftOfCentral = new JPanel();
        leftSideOftOfCentral.setLayout(new BoxLayout(leftSideOftOfCentral,BoxLayout.X_AXIS));
        volumeField = new JLabel("Size Part");
        speedField = new JLabel("Speed");
        statusField = new JLabel("Starting");
        statusField.setForeground(Color.RED);
        leftSideOftOfCentral.add(volumeField);
        leftSideOftOfCentral.add(new JLabel("         "));
        leftSideOftOfCentral.add(speedField);
        leftSideOftOfCentral.add(new JLabel("         "));
        leftSideOftOfCentral.add(statusField);

        bottomSideOfCentral.add(rightBottomSideOfCentral,BorderLayout.WEST);
        bottomSideOfCentral.add(leftSideOftOfCentral,BorderLayout.CENTER);


        centralPanel.add(topSideOfCentral);
        centralPanel.add(bottomSideOfCentral);

        // Right Panel
        rightPanel = new JPanel();
        informationButton = new JButton();
        informationButton.setPreferredSize(new Dimension(BUTTON_SIZE_ON_TOP_PANEL,BUTTON_SIZE_ON_TOP_PANEL));
        right = new ImageIcon("right-arrow.png");
        left = new ImageIcon("left-arrow.png");
        informationButton.setIcon(left);
        informationButton.setVisible(true);
        informationButton.addActionListener(actionHandler);
        rightPanel.add(informationButton);

        //Left Panel
        leftPanel = new JPanel();
        downloadImage = new JLabel();
        downloadImage.setIcon(new ImageIcon("database-downloaded.png"));
        downloadImage.setPreferredSize(new Dimension(downloadImage.getIcon().getIconWidth(),downloadImage.getIcon().getIconHeight()));
        leftPanel.add(downloadImage);


        newDownloadPanel.add(textArea,BorderLayout.NORTH);
        newDownloadPanel.add(rightPanel,BorderLayout.EAST);
        newDownloadPanel.add(centralPanel,BorderLayout.CENTER);
        newDownloadPanel.add(leftPanel,BorderLayout.WEST);
        newDownloadPanel.setVisible(true);
        newDownloadPanel.setMaximumSize(new Dimension(width,(int)newDownloadPanel.getPreferredSize().getHeight()));
        newDownloadPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY,2));
        newDownloadPanel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }
            @Override
            public void mousePressed(MouseEvent e) {
                if(!selected) {
                    selected = true;
                    newDownloadPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE,2));
                }
                else {
                    selected = false;
                    newDownloadPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY,2));
                }
                if( e.getClickCount() >= 2){
                    resumeButton.doClick();
                }
                if(SwingUtilities.isRightMouseButton(e))
                    informationButton.doClick();
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
        });

    }

    /**
     * This method is used to check if
     * two objects are equal or not by our
     * assignment two object of this type are the same
     * if have the same fileProperty
     * @param obj the object supposed to be checked
     * @return true if two objects are the same false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof NewDownloadPanel))
            return false;

        NewDownloadPanel newDownloadPanel = (NewDownloadPanel) obj;

        return this.fileProperties.getFileName().equals(newDownloadPanel.fileProperties.getFileName()) &&
                this.fileProperties.getSize().equals(newDownloadPanel.fileProperties.getSize()) &&
                this.fileProperties.getFileUrl().equals(newDownloadPanel.fileProperties.getFileUrl()) &&
                this.fileProperties.getStatus().equals(newDownloadPanel.fileProperties.getStatus()) &&
                this.fileProperties.getCreated().equals(newDownloadPanel.fileProperties.getCreated()) &&
                this.fileProperties.getAddress().equals(newDownloadPanel.fileProperties.getAddress());
    }

    /**
     * This method creates a SwingWorker class
     * and changes the state of the buttons to have a better look
     */
    public void startDownload(){
        resumeButton.setIcon(new ImageIcon("pause.png"));
        worker = new Worker();
        worker.execute();
        isPaused = false;
    }

    /**
     * Using this method we determine if the
     * panel is selected or not
     * @return true if panel was selected
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * This method pushes the fileProperty of NewDownloadPanel
     * @return FileProperty field of the object
     */
    public FileProperties getFileProperties() {
        return fileProperties;
    }

    /**
     * Using this method we have a better look and feel:)
     * @param width the efficient width of panel
     */
    public void setSize(int width){
        newDownloadPanel.setMaximumSize(new Dimension(width,(int)newDownloadPanel.getPreferredSize().getHeight()));
    }

    /**
     * @return panel field
     */
    public JPanel getPanel(){
        return newDownloadPanel;
    }

    /**
     * @return true if the process of download was completed successfully
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * using this method process of download
     * is stopped
     */
    public void pauseDownload(){
        isPaused = true;
        resumeButton.setIcon(resumeIcon);
    }

    /**
     * This method is used to delete the file {@param completed}
     * is used to check whether we need to delete the file either or not
     */
    public void delete(boolean completed){
        if(worker != null)
            worker.cancel(true);
        isPaused = true;
        fileProperties = null;
        if(completed)
            if(file.exists())
                file.delete();
    }

    /**
     * This method initializes the
     * NewDownloadPanel
     */
    public void initialize() {
        progressBar.setValue(100);
        statusField.setText("");
    }

    /**
     * This the Listener Class of the class which implements
     * only the ActionListener class
     */
    private class ActionHandler implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource().equals(resumeButton)){
                if(file.exists() && completed){
                    if(Desktop.isDesktopSupported()){
                        Desktop desktop = Desktop.getDesktop();
                        try {
                            desktop.open(file);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                else if(completed && !file.exists())
                    JOptionPane.showMessageDialog(newDownloadPanel,"File not found","open",JOptionPane.ERROR_MESSAGE);
                else{
                    if(resumeButton.getIcon().equals(resumeIcon)){
                        startDownload();
                    }
                    else {
                        pauseDownload();
                    }
                }
            }
            else if(e.getSource().equals(cancelButton)) {
                ImageIcon tmp = new ImageIcon("cross.png");
                Object[] options = {"Yes, please", "And also delete the file", "No, keep the file"};
                int n = JOptionPane.showOptionDialog(newDownloadPanel, "Would you like to delete this download?", "Delete", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, tmp, options,options[2]);
                switch(n){
                    case 1:
                        if(file.exists()) {
                            file.delete();
                        }
                        else
                            JOptionPane.showMessageDialog(newDownloadPanel,"File not found deleting from panel","delete",JOptionPane.INFORMATION_MESSAGE);
                    case 0:
                        String tmpString;
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                        LocalDateTime now = LocalDateTime.now();
                        try(BufferedWriter writer =  new BufferedWriter(new FileWriter(new File("files/removed.gdm"),true))){
                                tmpString = "\n";
                                tmpString += "****** Removed On Date " + dtf.format(now) + "******" + "\n";
                                tmpString += "File Properties" + "\n";
                                tmpString += "File URL : " + fileProperties.getFileUrl() + "\n";
                                tmpString += "File Name : " + fileProperties.getFileName()+ "\n";
                                tmpString += "File Created Time :" +fileProperties.getCreated() + "\n";
                                tmpString += "File Size : "    + fileProperties.getSize() + "\n";
                                tmpString += "File Download Address : " +fileProperties.getAddress() + "\n";
                                tmpString += "File Status At While Removing : " + fileProperties.getStatus() + "\n";
                                writer.write(tmpString);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        isPaused = true;
                        worker.cancel(true);
                        fileProperties = null;
                        Manager.getAction("main.update");
                        break;

                    default:
                        // Do nothing
                        break;
                }
            }
            else if(e.getSource().equals(directoryButton)){
                if(Desktop.isDesktopSupported()){
                    Desktop desktop = Desktop.getDesktop();
                    try {
                        desktop.open(new File(fileProperties.getAddress()));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            else if(e.getSource().equals(informationButton)){
                if(informationButton.getIcon() == left){
                    informationButton.setIcon(right);
                    Manager.showRightPanel(fileProperties);
                }
                else {
                    System.out.println("hide");
                    informationButton.setIcon(left);
                    Manager.hideRightPanel();
                }
            }
        }
    }

    /**
     * Corresponding SwingWorker of the class
     */
    private class Worker extends SwingWorker<String, Double>{
        Double fileSize;
        @Override
        protected String doInBackground() throws Exception {
            BufferedInputStream input;
            RandomAccessFile output;
            HttpURLConnection connection;
            Long downloadedSize = file.length();
            URL url = new URL(fileProperties.getFileUrl());
            connection = (HttpURLConnection) url.openConnection();
            if (file.exists()) {
                connection.setAllowUserInteraction(true);
                connection.setRequestProperty("Range", "bytes=" + file.length() + "-");
            }
            connection.setConnectTimeout(14000);
            connection.setReadTimeout(20000);
            connection.connect();

            if (connection.getResponseCode() / 100 != 2) {
                JOptionPane.showMessageDialog(newDownloadPanel, "Error downloading file from " + fileProperties.getFileUrl(), "Error", JOptionPane.ERROR_MESSAGE);
                statusField.setText("Error Downloading file");
            }
            else {
                String connectionField = connection.getHeaderField("content-range");
                if (connectionField != null) {
                    String[] connectionRanges = connectionField.substring("bytes=".length()).split("-");
                    downloadedSize = Long.valueOf(connectionRanges[0]);
                }

                if (connectionField == null && file.exists())
                    file.delete();
                fileSize = (new Long(connection.getContentLength())).doubleValue() + downloadedSize;
                fileProperties.setSize(Long.toString(connection.getContentLength() + downloadedSize));
                System.out.println(fileProperties.getSize());
                input = new BufferedInputStream(connection.getInputStream());
                output = new RandomAccessFile(file, "rw");
                output.seek(downloadedSize);
                resumeButton.setIcon(new ImageIcon("pause.png"));
                byte data[] = new byte[BUFFER_SIZE];
                int count = 0;

                while ((count = input.read(data, 0, BUFFER_SIZE)) != -1) {
                    if(isPaused)
                        return "Paused";
                    Long startingTime = System.nanoTime();
                    downloadedSize += count;
                    output.write(data, 0, count);
                    publish(downloadedSize.doubleValue());
                    Long finishingTime = System.nanoTime();
                    publish( finishingTime.doubleValue() - startingTime.doubleValue());
                }
                output.close();
                input.close();
            }
            return "Finished";
        }

        @Override
        protected void process(List<Double> chunks) {
            statusField.setText("");
            int level;
            // Size part
            Double downloadedSize = chunks.get(chunks.size() - 2);
            Double fileSize = this.fileSize;
            progressBar.setValue((int)((downloadedSize*100)/fileSize));
            String firstPart;
            String secondPart;
            level = 0;
            while(downloadedSize> BUFFER_SIZE && level < UNITS.size()){
                level++;
                downloadedSize /= BUFFER_SIZE;
            }
            firstPart = String.format( "%.2f %s", downloadedSize,UNITS.get(level));
            level = 0;
            while(fileSize > BUFFER_SIZE && level < UNITS.size()){
                level++;
                fileSize /= BUFFER_SIZE;
            }
            secondPart = String.format( "%.2f %s", fileSize,UNITS.get(level));

            volumeField.setText("Downloaded " + firstPart + " of " + secondPart);

            // Speed part
            Double duration = chunks.get(chunks.size()-1);
            duration /= 1000000000;
            Double speed = BUFFER_SIZE/duration;
//            System.out.println(" speed " + speed);
//            System.out.println("Time "+ duration);
            level = 0;
            while(speed > BUFFER_SIZE && level < UNITS.size()){
                speed /= BUFFER_SIZE;
                level++;
            }

            speedField.setText(String.format("Download Speed: %.2f %s ", speed , (UNITS.get(level-1) + "/s")));
        }

        @Override
        protected void done() {
            try {
                switch (get()) {
                    case "Paused":
                        statusField.setText("Paused");
                        fileProperties.setStatus("Pause");
                        completed = false;
                        break;
                    case "Finished":
                        resumeButton.setIcon(new ImageIcon("resume.png"));
                        completed = true;
                        fileProperties.setStatus("Completed");
                        System.out.println(completed);
                        System.out.println("Finished");
                        Manager.updateCompleted();
                        break;
                        default:
                            throw new Exception("Item not found");
                }
            }
            catch(Exception e){}
        }

    }
}