import javax.swing.*;
import java.awt.*;

public class NewDownloadPanel {
    private FileProperties fileProperties;
    private JPanel newDownloadPanel;

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
    private JButton showDirectoryButton;

    // Left side
    private JPanel leftPanel;
    private JLabel downloadImage;


    public NewDownloadPanel(FileProperties properties, Dimension dimension) {
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
//        bottomSideOfCentral.setLayout(new BoxLayout(bottomSideOfCentral,BoxLayout.X_AXIS));

        rightBottomSideOfCentral = new JPanel();
        rightBottomSideOfCentral.setLayout(new BoxLayout(rightBottomSideOfCentral,BoxLayout.X_AXIS));
        resumeButton = new JButton(new ImageIcon("resume.png"));
        resumeButton.setMaximumSize(new Dimension(BUTTON_SIZE_ON_TOP_PANEL,BUTTON_SIZE_ON_TOP_PANEL));
//        resumeButton.setAlignmentY(Component.LEFT_ALIGNMENT);
        cancelButton = new JButton(new ImageIcon("cancel.png"));
        cancelButton.setMaximumSize(new Dimension(BUTTON_SIZE_ON_TOP_PANEL,BUTTON_SIZE_ON_TOP_PANEL));
        directoryButton = new JButton(new ImageIcon("folder.png"));
        directoryButton.setMaximumSize(new Dimension(BUTTON_SIZE_ON_TOP_PANEL,BUTTON_SIZE_ON_TOP_PANEL));
        rightBottomSideOfCentral.add(resumeButton);
        rightBottomSideOfCentral.add(Box.createRigidArea(new Dimension(5,0)));
        rightBottomSideOfCentral.add(cancelButton);
        rightBottomSideOfCentral.add(Box.createRigidArea(new Dimension(5,0)));
        rightBottomSideOfCentral.add(directoryButton);
//        rightBottomSideOfCentral.add(Box.createRigidArea(new Dimension(0,5)));

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
        showDirectoryButton = new JButton();
        showDirectoryButton.setPreferredSize(new Dimension(BUTTON_SIZE_ON_TOP_PANEL,BUTTON_SIZE_ON_TOP_PANEL));
        showDirectoryButton.setIcon(new ImageIcon("right-arrow.png"));
        showDirectoryButton.setVisible(true);
        rightPanel.add(showDirectoryButton);

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
//        newDownloadPanel.add(bottomSideOfCentral,BorderLayout.SOUTH);
        newDownloadPanel.setVisible(true);
        newDownloadPanel.setMaximumSize(dimension);
        newDownloadPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK,2));
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
}