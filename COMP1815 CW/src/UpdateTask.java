import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class UpdateTask {
    public JPanel UpdateTPanel;
    private JButton backToMainMenuButton;
    private JTextField TaskIDF;
    private JTextField ProgressF;
    private JButton updateButton;
    private JTextArea textArea1;
    private TaskHandler handler;

    public UpdateTask() {
        textArea1.setText("If Progress is 100 (the task is completed), \nplease note down the number of days taken to \nfinish this task - so that the critical path can be updated.");

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
                handler = new TaskHandler();
                if (ProjectCreator.validationCheck(TaskIDF.getText(), true) &&
                        ProjectCreator.validationCheck(ProgressF.getText(), true)
                ) {
                    String id = TaskIDF.getText();
                    String progress = ProgressF.getText();
                    Boolean foundID = handler.updateTasksProgress(id, progress);
                    if (foundID) {
                        if (progress.equals("100")) { // Notifies user to update Critical Path
                            JOptionPane.showMessageDialog(UpdateTPanel, "Task Progress of 100 means it's completed. \nPlease update the Task Duration to [THE NO. OF DAYS TAKEN FOR THIS TASK] --- so that the Critical Path can be updated.");

                            // Go to Update Task Duration for Critical Path
                            JFrame TaskUpD = new JFrame("Update Task Duration");
                            TaskUpD.setContentPane(new TaskDurationUpdater().UpdateTaskDurationPanel);
                            TaskUpD.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            TaskUpD.pack();
                            TaskUpD.setVisible(true);
                            TaskUpD.setLocationRelativeTo(null);
                            // Closes current window
                            JComponent comp = (JComponent) e.getSource();
                            Window win = SwingUtilities.getWindowAncestor(comp);
                            win.dispose();
                        } else {
                            JOptionPane.showMessageDialog(UpdateTPanel, "Task Progress Updated.");

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
                        }
                    } else {
                        JOptionPane.showMessageDialog(UpdateTPanel, "Error! Task ID Not Found!");
                    }
                } else {
                    JOptionPane.showMessageDialog(UpdateTPanel, "Error! Avoid using special characters or invalid inputs (use numbers only)");
                }
            }
        });
    }
}
