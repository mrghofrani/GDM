import com.sun.javafx.binding.StringFormatter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
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
        bottomSideOfCentral = new JPanel(new BorderLayout());
        rightBottomSideOfCentral = new JPanel();
        rightBottomSideOfCentral.setLayout(new BoxLayout(rightBottomSideOfCentral,BoxLayout.X_AXIS));
        resumeButton = new JButton(new ImageIcon("resume.png"));
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
        worker = new Worker();
    }

    public void startDownload(){
        worker.execute();
    }

    public boolean isSelected() {
        return selected;
    }

    public FileProperties getFileProperties() {
        return fileProperties;
    }

    public void setFileProperties(FileProperties fileProperties) {
        this.fileProperties = fileProperties;
    }

    public void setSize(int width){
        newDownloadPanel.setMaximumSize(new Dimension(width,(int)newDownloadPanel.getPreferredSize().getHeight()));
    }

    public JPanel getPanel(){
        return newDownloadPanel;
    }

    public void deleteFileProperties(){
        fileProperties = null;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

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
                else
                    JOptionPane.showMessageDialog(newDownloadPanel,"File not found","open",JOptionPane.ERROR_MESSAGE);
            }
            else if(e.getSource().equals(cancelButton)) {
                ImageIcon tmp = new ImageIcon("cross.png");
                Object[] options = {"Yes, please", "And also delete the file", "No, keep the file"};
                int n = JOptionPane.showOptionDialog(newDownloadPanel, "Would you like to delete this download?", "Delete", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, tmp, options,options[2]);
                switch(n){
                    case 1:
                        if(file.exists())
                            file.delete();
                        else
                            JOptionPane.showMessageDialog(newDownloadPanel,"File not found deleting from panel","delete",JOptionPane.INFORMATION_MESSAGE);
                    case 0:
                        fileProperties = null;
                        worker.cancel(true);
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

    private class Worker extends SwingWorker<Void, String>{

        @Override
        protected Void doInBackground() throws Exception {
            BufferedInputStream bufferedInputStream = null;
            FileOutputStream fileOutputStream = null;
            BufferedOutputStream bufferedOutputStream = null;
            try {
                URL url = new URL(fileProperties.getFileUrl());
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                if(httpURLConnection.getResponseCode() != 200) {
                    JOptionPane.showMessageDialog(newDownloadPanel, "Error downloading file from " + fileProperties.getFileUrl(), "Error", JOptionPane.ERROR_MESSAGE);
                    statusField.setText("Error Downloading file");
                }
                else {
                    fileProperties.setSize(Long.toString(httpURLConnection.getContentLength()));
                    bufferedInputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                    fileOutputStream = new FileOutputStream(file);
                    bufferedOutputStream = new BufferedOutputStream(fileOutputStream,BUFFER_SIZE);
                    byte[] buffer = new byte[BUFFER_SIZE];
                    int read;
                    System.out.println("reached");
                    while ((read = bufferedInputStream.read(buffer, 0, BUFFER_SIZE)) >= 0 && !isPaused) {
                        Long startingTime = System.nanoTime();
                        bufferedOutputStream.write(buffer, 0, read);
                        downloadedValue += read;
                        Long endingTime = System.nanoTime();
                        publish(Double.toString(downloadedValue));
                        Double duration = endingTime.doubleValue() - startingTime.doubleValue();
                        publish(duration.toString());
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            finally {
                if(bufferedInputStream != null)
                    bufferedInputStream.close();
                if(bufferedOutputStream != null)
                    bufferedOutputStream.close();
            }

            return null;
        }

        @Override
        protected void process(List<String> chunks) {
            statusField.setText("");
            int level;
            // Size part
            String downloadedString = chunks.get(chunks.size() - 2);
            Double downloadedDouble = Double.parseDouble(downloadedString);
            Double fileSize = Double.parseDouble(fileProperties.getSize());
            progressBar.setValue((int)((downloadedDouble*100)/fileSize));
            String firstPart;
            String secondPart;
            level = 0;
            while(downloadedDouble> BUFFER_SIZE && level < UNITS.size()){
                level++;
                downloadedDouble /= BUFFER_SIZE;
            }
            firstPart = String.format( "%.2f %s", downloadedDouble,UNITS.get(level));
            level = 0;
            while(fileSize > BUFFER_SIZE && level < UNITS.size()){
                level++;
                fileSize /= BUFFER_SIZE;
            }
            secondPart = String.format( "%.2f %s", fileSize,UNITS.get(level));

            volumeField.setText("Downloaded " + firstPart + " of " + secondPart);

            // Speed part
            String durationString = chunks.get(chunks.size()-1);
            Double durationDouble = Double.parseDouble(durationString);
            durationDouble /= Math.pow(10,9);
            Double speed = BUFFER_SIZE/durationDouble;
            System.out.println(" speed " + speed);
            System.out.println("Time "+ durationDouble);
            level = 0;
            while(speed > BUFFER_SIZE && level < UNITS.size()){
                speed /= BUFFER_SIZE;
                level++;
            }

            speedField.setText(String.format("Download Speed: %.2f %s ", speed , (UNITS.get(level) + "/s")));
        }

        @Override
        protected void done() {
            completed = true;
        }
    }
}