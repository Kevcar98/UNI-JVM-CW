import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


public class TaskViewer {
    public JPanel TaskVPanel;
    private JButton backToMainMenuButton;
    private JList taskJList;
    private TaskHandler handler;
    private List<Tasks> task;

    public TaskViewer() {
        handler = new TaskHandler();
        task = handler.loadTasks();
        if (task != null) {
            taskJList.setListData(task.toArray(new Tasks[0])); // converts list to new array for JList
        } else if (task == null) {
            System.out.println("ERROR: Task is null");
        } else {
            System.out.println("ERROR: Task is of unknown status");
        }

        backToMainMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame HomePF = new JFrame("Home Page");
                HomePF.setContentPane(new HomePage().HomePanel);
                HomePF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                HomePF.pack();
                HomePF.setVisible(true);
                HomePF.setLocationRelativeTo(null);
                // Closes current window
                JComponent comp = (JComponent) e.getSource();
                Window win = SwingUtilities.getWindowAncestor(comp);
                win.dispose();
            }
        });
        taskJList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

            }
        });
    }
}
