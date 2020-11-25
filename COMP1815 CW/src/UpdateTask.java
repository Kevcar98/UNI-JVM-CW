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
    private TaskHandler handler;

    public UpdateTask() {
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
