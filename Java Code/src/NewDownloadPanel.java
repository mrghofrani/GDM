import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

public class NewDownloadPanel {
    private FileProperties fileProperties;
    private JPanel newDownloadPanel;
    private ActionHandler actionHandler = new ActionHandler();
    private boolean selected = false;

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
        newDownloadPanel = new JPanel();
        newDownloadPanel.setLayout(new BorderLayout(5,5));

        // TextArea related Panel
        textArea = new JPanel(new BorderLayout(5,5));
        nameField = new JLabel(properties.getFileName());
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
        leftSideOftOfCentral.add(volumeField);

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


    private class ActionHandler implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource().equals(resumeButton)){
                JOptionPane.showMessageDialog(null,"File Opened","open",JOptionPane.INFORMATION_MESSAGE);
            }
            else if(e.getSource().equals(cancelButton)) {
                ImageIcon tmp = new ImageIcon("cross.png");
                Object[] options = {"Yes, please", "And also delete the file", "No, keep the file"};
                int n = JOptionPane.showOptionDialog(newDownloadPanel, "Would you like to delete this download?", "Delete", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, tmp, options,options[2]);
                switch(n){
                    case 0:
                        fileProperties = null;
                        Manager.getAction("main.update");
                        break;
                    case 1:
                        // TODO: I should implement the file delete part
                        break;
                    case 2:
                        break;
                }
            }
            else if(e.getSource().equals(directoryButton)){
                if(Desktop.isDesktopSupported()){
                    Desktop desktop = Desktop.getDesktop();
                    File file = new File(fileProperties.getAddress());
                    try {
                        desktop.open(file);
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
            else if(e.getSource().equals(newDownloadPanel) || e.getSource().equals(rightPanel) || e.getSource().equals(progressBar)){

            }
        }
    }
}