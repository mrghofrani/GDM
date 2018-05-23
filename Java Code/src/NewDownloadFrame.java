import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewDownloadFrame {
    // Main Basis
    private static NewDownloadFrame instance;
    private JFrame downloadFrame;

    // Web Page Part
    private JPanel webPagePanel;
    private JLabel webPageTitle;
    private JTextField webPageAddress;

    // Volume Panel
    private JPanel volumePanel;
    private JLabel volumeTitle;
    private JTextField volumeValue;

    // Location part that user can choose where to save file
    private JPanel locationPanel;
    private String locationString;
    private JLabel locationLabel;
    private JTextField locationText;
    private JButton locationButton;
    private JFileChooser locationChooser;

    //
    private JPanel buttonPanel;
    private JButton downloadButton;
    private JButton addToQueueButton;
    private JButton cancelButton;


    private ActionHandler actionHandler = new ActionHandler();

//    public static NewDownloadFrame getInstance(){
//        if(instance == null){
//            instance = new NewDownloadFrame();
//        }
//        return instance;
//    }

    public NewDownloadFrame(){
        downloadFrame = new JFrame("New Download");
        downloadFrame.setLayout(new BoxLayout(downloadFrame.getContentPane(),BoxLayout.Y_AXIS));

        webPagePanel = new JPanel();
        webPagePanel.setLayout(new BoxLayout(webPagePanel,BoxLayout.X_AXIS));
        webPageTitle = new JLabel("Address:");
        webPageTitle.setAlignmentY(Component.LEFT_ALIGNMENT);
        webPageAddress = new JTextField("https://google.com");
        webPageAddress.setEditable(true);
        webPageAddress.setBackground(Color.WHITE);
        webPagePanel.add(webPageTitle);
        webPagePanel.add(webPageAddress);
        downloadFrame.add(webPagePanel);

        // volume bar
        volumePanel = new JPanel();
        volumePanel.setLayout(new BoxLayout(volumePanel,BoxLayout.X_AXIS));
        volumeTitle = new JLabel("Volume:");
        volumeTitle.setAlignmentY(Component.LEFT_ALIGNMENT);
        volumeValue = new JTextField("100 GB");
        volumeValue.setBackground(Color.WHITE);
        volumeValue.setEditable(false);
        volumePanel.add(volumeTitle);
        volumePanel.add(volumeValue);
        downloadFrame.add(volumePanel);


        locationPanel = new JPanel(new FlowLayout());
        locationLabel = new JLabel("Location Of Downloads: ");
        locationText = new JTextField(Manager.getDownloadPath());
        locationText.setSize(locationText.getWidth(),200);
        locationText.setEditable(false);
        locationButton = new JButton("...");
        locationButton.addActionListener(actionHandler);
        locationChooser = new JFileChooser();
        locationChooser.setCurrentDirectory(new File(Manager.getDownloadPath()));
        locationChooser.setDialogTitle("Choose where you want to save your downloads");
        locationChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        locationChooser.setAcceptAllFileFilterUsed(false);
        locationText.setText(Manager.getDownloadPath());
        locationText.setBackground(Color.WHITE);
        locationText.setPreferredSize(new Dimension(300,25));
        locationPanel.add(locationLabel);
        locationPanel.add(locationText);
        locationPanel.add(locationButton);
        downloadFrame.add(locationPanel);

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.X_AXIS));
        downloadButton = new JButton("Start Download");
        downloadButton.addActionListener(actionHandler);
        addToQueueButton = new JButton("Add to Queue");
        addToQueueButton.addActionListener(actionHandler);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(actionHandler);
        buttonPanel.add(downloadButton);
        buttonPanel.add(addToQueueButton);
        buttonPanel.add(cancelButton);
        downloadFrame.add(buttonPanel);

    }

    public void show(){
        reset();
        downloadFrame.pack();
        downloadFrame.setVisible(true);
    }

    private void reset(){
        locationText.setText(Manager.getDownloadPath());
        locationChooser.setCurrentDirectory(new File(Manager.getDownloadPath()));
    }

    private class ActionHandler implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource().equals(locationButton)) {
                locationChooser.setVisible(true);
                locationChooser.showOpenDialog(null);
                locationString = locationChooser.getSelectedFile().getAbsolutePath();
                locationText.setText(locationString);
                locationText.revalidate();
            }
            else if(e.getSource().equals(cancelButton)){
                downloadFrame.dispose();
            }
            else if(e.getSource().equals(downloadButton)){
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                Manager.addNewDownload(new FileProperties(webPageAddress.getText(),"not completed",volumeValue.getText(),dateFormat.format(date),locationText.getText()));
                downloadFrame.dispose();
            }
            else if(e.getSource().equals(addToQueueButton)){
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                Manager.addNewDownloadQueue(new FileProperties(webPageAddress.getText(),"not completed",volumeValue.getText(),dateFormat.format(date),locationText.getText()));
                downloadFrame.dispose();
                System.out.println("Action");
            }
        }
    }

}
