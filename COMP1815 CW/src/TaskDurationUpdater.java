import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class TaskDurationUpdater {
    public JPanel UpdateTaskDurationPanel;
    private JTextField TaskIDF;
    private JTextField DurationF;
    private JButton backToMainMenuButton;
    private JButton updateButton;
    private TaskHandler handler;

    public TaskDurationUpdater() {
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
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ProjectCreator.validationCheck(TaskIDF.getText(), true) &&
                        ProjectCreator.validationCheck(DurationF.getText(), true)
                ) {
                    handler = new TaskHandler();
                    String id = TaskIDF.getText();
                    String duration = DurationF.getText();
                    Boolean foundID = handler.updateTasksDuration(id, duration);
                    if (foundID) {
                        JOptionPane.showMessageDialog(UpdateTaskDurationPanel, "Task Duration Updated.");

                        // Back to Main Menu
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
                    } else {
                        JOptionPane.showMessageDialog(UpdateTaskDurationPanel, "Error! Task ID Not Found!");
                    }
                } else {
                    JOptionPane.showMessageDialog(UpdateTaskDurationPanel, "Error! Avoid using special characters or invalid inputs (use numbers only)");
                }
            }
        });
    }
}
