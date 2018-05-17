import javax.swing.*;
import java.awt.*;

public class NewDownloadPanel {
    private JPanel newDownloadPanel;

    // top part of the newDownload Panel
    private JPanel textArea;
    private JLabel nameField;

    // central part  of the NewDownloadPanel
    private JPanel centralPart;
    private JLabel downloadImage;
    private JProgressBar progressBar;
    private JButton showDirectoryButton;
    private JTextField percent;

    //bottom part of the NewDownloadPanel
    private JPanel bottomPanel;
    private JButton resumeButton;
    private JButton cancelButton;
    private JButton directoryButton;
    private JLabel volumeField;

    public NewDownloadPanel(String name, String volume) {
        newDownloadPanel = new JPanel();
        newDownloadPanel.setLayout(new BoxLayout(newDownloadPanel,BoxLayout.Y_AXIS));

        // TextArea related Panel
        textArea = new JPanel(new FlowLayout());
        nameField = new JLabel(name);
        textArea.add(nameField);


        // central part of the NewDownloadPanel
        centralPart = new JPanel(new FlowLayout());
        downloadImage = new JLabel(new ImageIcon("database-downloaded.png"));
        progressBar = new JProgressBar();
        showDirectoryButton = new JButton();
        showDirectoryButton.setIcon(new ImageIcon("right-arrow.png"));
        showDirectoryButton.setVisible(false);
        percent = new JTextField(Double.toString(progressBar.getPercentComplete()));
        percent.setEditable(false);
        centralPart.add(downloadImage);
        centralPart.add(progressBar);
        centralPart.add(showDirectoryButton);
        centralPart.add(percent);


        // bottom Part of the NewDownloadPanel
        bottomPanel = new JPanel();
        resumeButton = new JButton(new ImageIcon("play-button.png"));
        cancelButton = new JButton(new ImageIcon("cancel.png"));
//        pauseButton = new JButton(new ImageIcon("pause.png"));
        directoryButton = new JButton(new ImageIcon("right-arrow.png"));
        volumeField = new JLabel("0");
        bottomPanel.add(resumeButton);
        bottomPanel.add(cancelButton);
        bottomPanel.add(directoryButton);
        bottomPanel.add(volumeField);


        newDownloadPanel.add(textArea);
        newDownloadPanel.add(centralPart);
        newDownloadPanel.add(bottomPanel);
        newDownloadPanel.setVisible(true);

        newDownloadPanel.setSize(new Dimension((int)(textArea.getPreferredSize().getWidth()+downloadImage.getPreferredSize().getWidth() + resumeButton.getPreferredSize().getWidth()),(int)newDownloadPanel.getPreferredSize().getHeight()));
    }
    public JPanel getPanel(){
        return newDownloadPanel;
    }
}