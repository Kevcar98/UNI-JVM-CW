import CriticalPath.KotlinCP;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class KotlinCriticalPath {

    private JList list1;
    private JLabel DurationL;
    private JButton submitButton;
    private JButton backToMainMenuButton;
    private JComboBox ProjectJBox;
    private JLabel CPL;
    private JLabel NodesAmountL;
    private ProjectHandler handler;
    private TaskHandler taskHandler;
    private KotlinCP cphandler;


    public KotlinCriticalPath() {
        handler = new ProjectHandler();
        taskHandler = new TaskHandler();
        cphandler = new KotlinCP();

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        backToMainMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
}
