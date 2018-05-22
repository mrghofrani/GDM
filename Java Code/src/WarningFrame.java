import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class WarningFrame {
    private JFrame warningFrame;

    private JPanel textPanel;
    private JLabel textLabel;

    private JPanel warningPanel;
    private JLabel warningMark;

    private JPanel bottomPanel;

    private JPanel buttons;
    private JButton cancelButton;
    private JButton OKButton;

    private JPanel checkBox;
    private JCheckBox deleteCheckbox;
    private ActionHandler actionHanlder = new ActionHandler();
    private static boolean[] returnValue = new boolean[2];


    public WarningFrame(){
        reset();
        warningFrame = new JFrame("Cancel");
        warningFrame.setLayout(new BorderLayout(5,5));



        // Text Area
        textPanel = new JPanel(new BorderLayout(5,5));
        textLabel = new JLabel("Chosen tasks will be deleted from the queue. Are you sure?");
        textPanel.add(textLabel,BorderLayout.CENTER);


        // Warning Picture
        warningPanel = new JPanel(new BorderLayout());
        warningMark = new JLabel(new ImageIcon("cross.png"));
        warningPanel.add(warningMark,BorderLayout.CENTER);


        // bottom Part
        bottomPanel = new JPanel(new GridLayout(1,2,5,5));

        buttons = new JPanel(new GridLayout(1,2,5,5));
        OKButton = new JButton("OK");
        OKButton.addActionListener(actionHanlder);
        cancelButton = new JButton("Cancel");
        buttons.add(OKButton);
        buttons.add(cancelButton);

        checkBox = new JPanel();
        deleteCheckbox = new JCheckBox("And also I want to delete the file, itself");
        checkBox.add(deleteCheckbox);
        bottomPanel.add(checkBox);
        bottomPanel.add(buttons);

        warningFrame.add(textPanel,BorderLayout.CENTER);
        warningFrame.add(warningPanel,BorderLayout.WEST);
        warningFrame.add(bottomPanel,BorderLayout.SOUTH);
        warningFrame.pack();
        warningFrame.setVisible(true);
    }

    private void reset(){

        textLabel = null;
        textPanel = null;


        warningMark = null;
        warningPanel = null;

        cancelButton = null;
        OKButton = null;
        buttons = null;


        deleteCheckbox = null;
        checkBox = null;

        bottomPanel = null;

        warningFrame = null;
    }

    private class ActionHandler implements ItemListener,ActionListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            if(e.getSource().equals(deleteCheckbox)) {
                deleteCheckbox.setSelected(true);
                returnValue[1] = false;
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource().equals(OKButton)){
                returnValue[0] = true;
            }
            else if(e.getSource().equals(cancelButton)){
                returnValue[0] = false;
            }
            warningFrame.setVisible(false);
        }
    }

    public boolean[] getReturnValue() {
        return returnValue;
    }
}
