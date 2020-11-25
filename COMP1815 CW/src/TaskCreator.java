import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


public class TaskCreator {
    public JPanel TaskCPanel;
    private JButton backToMainMenuButton;
    private JTextField ProjectManagerF;
    private JTextField CommissionerF;
    private JTextField TaskIDF;
    private JButton createTaskButton;
    private JTextField TaskDurationF;
    private JComboBox assignProjectsJBox;
    private JComboBox assignTeamsJBox;
    private JComboBox prerequisiteTasksJBox;
    private JCheckBox noPrerequisiteTasksCBox;
    private JButton addPrerequisiteButton;
    private JButton resetPrerequisitesButton;
    private JLabel SelectedPrerequisiteF;
    private String prerequisiteTaskID = "";
    private TaskHandler handler;
    private List<Tasks> task;
    private ProjectHandler proHandler;

    public TaskCreator() {
        handler = new TaskHandler();
        proHandler = new ProjectHandler();
        assignProjectsJBox.setModel(new DefaultComboBoxModel(handler.listProjectsForTask())); // Sets Projects combo box to list of ProjectIDs
        if (assignProjectsJBox.getSelectedItem() != null) {
            // Sets Teams combo box to the Team IDs associated with the selected Project ID
            String[] projectTeams = handler.teamsAssignedToProject(assignProjectsJBox.getSelectedItem().toString());
            assignTeamsJBox.setModel(new DefaultComboBoxModel(handler.listTeamsForTask(projectTeams)));
            // Sets Prerequisite Tasks combo box to the Task IDs associated with the selected Project ID (task dependency must be on existing task)
            prerequisiteTasksJBox.setModel(new DefaultComboBoxModel(handler.listTasksForTasks(assignProjectsJBox.getSelectedItem().toString())));
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
        createTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ProjectCreator.validationCheck(TaskIDF.getText(), true) &&
                        !TaskIDF.getText().equals("0") &&
                        assignProjectsJBox.getSelectedItem() != null &&
                        ProjectCreator.validationCheck(CommissionerF.getText(), false) &&
                        ProjectCreator.validationCheck(ProjectManagerF.getText(), false) &&
                        ProjectCreator.validationCheck(TaskDurationF.getText(), true) &&
                        assignTeamsJBox.getSelectedItem() != null &&
                        // If the "No Prerequisites" checkbox is checked but the user has added to the prerequisiteTaskID String, warn them
                        (
                                (noPrerequisiteTasksCBox.isSelected() && prerequisiteTaskID.isEmpty()) ||
                                (!noPrerequisiteTasksCBox.isSelected())
                        ) &&
                        // Checks if the "No Prerequisites" checkbox is checked, and if not, checks if the prerequisiteTaskID String is empty
                        (
                                (!noPrerequisiteTasksCBox.isSelected() && !prerequisiteTaskID.isEmpty()) ||
                                (noPrerequisiteTasksCBox.isSelected())
                        ) &&
                        // Checks if the "No Prerequisites" checkbox is checked, and if not, it checks if the Prerequisite Tasks combo box is not null
                        (
                                (!noPrerequisiteTasksCBox.isSelected() && prerequisiteTasksJBox.getSelectedItem() != null) ||
                                (noPrerequisiteTasksCBox.isSelected())
                        )
                ) {
                    if (handler.uniqueIDCheck(TaskIDF.getText())) {
                        task = handler.createTask(
                                TaskIDF.getText(),
                                assignProjectsJBox.getSelectedItem().toString(),
                                CommissionerF.getText(),
                                ProjectManagerF.getText(),
                                TaskDurationF.getText(),
                                assignTeamsJBox.getSelectedItem().toString(),
                                "0"
                        );
                        handler.save(task);
                        // Gets value of TaskIDF and adds it to the String assignedTaskID which will hold any prerequisite task as well
                        String assignedTaskID = TaskIDF.getText();
                        if (assignedTaskID.isEmpty()) {
                            assignedTaskID = "1";
                        }
                        if (!noPrerequisiteTasksCBox.isSelected()) {
                            assignedTaskID = prerequisiteTaskID + "->" + assignedTaskID;
                        } // If the "No Prerequisites" checkbox is unchecked, use value of prerequisiteTaskID String
                        proHandler.updateProjectTaskData(
                                assignProjectsJBox.getSelectedItem().toString(),
                                assignedTaskID
                        ); // Upon creating a task, updates the relevant Project's AssignedTaskID to show this new task
                        JOptionPane.showMessageDialog(TaskCPanel, "Task saved.");

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
                        JOptionPane.showMessageDialog(TaskCPanel, "Error! Task ID is not unique!");
                    }
                } else if (assignProjectsJBox.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(TaskCPanel, "Error! There are no projects selected. Please select at least one project (or create one if there are none available).");
                } else if (assignTeamsJBox.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(TaskCPanel, "Error! There are no teams selected. Please select at least one team (or create one if there are none available).");
                } else if (!noPrerequisiteTasksCBox.isSelected() && prerequisiteTaskID.isEmpty()) {
                    JOptionPane.showMessageDialog(TaskCPanel, "Error! There are no prerequisite tasks assigned. Please select at least one prerequisite task (or create one if there are none available), or check the \"No Prerequisites\" checkbox.");
                } else if (noPrerequisiteTasksCBox.isSelected() && !prerequisiteTaskID.isEmpty()) {
                    JOptionPane.showMessageDialog(TaskCPanel, "Error! You have checked the \"No Prerequisites\" checkbox but have added prerequisites with the button. Please either uncheck the checkbox or click the \"Reset Prerequisites\" button.");
                } else if (TaskIDF.getText().equals("0")) {
                    JOptionPane.showMessageDialog(TaskCPanel, "Error! Task ID cannot be 0. Please input a different Task ID.");
                } else {
                    JOptionPane.showMessageDialog(TaskCPanel, "Error! Avoid using special characters or invalid inputs (e.g. letters in a text field expecting only numbers)");
                }
            }
        });
        assignProjectsJBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (assignProjectsJBox.getSelectedItem() != null) {
                    // Sets Teams combo box to the Team IDs associated with the selected Project ID
                    String[] projectTeams = handler.teamsAssignedToProject(assignProjectsJBox.getSelectedItem().toString());
                    assignTeamsJBox.setModel(new DefaultComboBoxModel(handler.listTeamsForTask(projectTeams)));
                    // Sets Prerequisite Tasks combo box to the Task IDs associated with the selected Project ID (task dependency must be on existing task)
                    prerequisiteTasksJBox.setModel(new DefaultComboBoxModel(handler.listTasksForTasks(assignProjectsJBox.getSelectedItem().toString())));
                    // Resets selected prerequisite tasks to prevent tasks from other projects to be added
                    prerequisiteTaskID = "";
                    SelectedPrerequisiteF.setText("Selected prerequisite tasks appear here:");
                }
            }
        });
        addPrerequisiteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (prerequisiteTasksJBox.getSelectedItem() != null) { // Checks combo box if valid option selected
                    if (prerequisiteTaskID.isEmpty()) {
                        prerequisiteTaskID = prerequisiteTaskID.concat(prerequisiteTasksJBox.getSelectedItem().toString());
                    } else {
                        prerequisiteTaskID = prerequisiteTaskID.concat("+" + prerequisiteTasksJBox.getSelectedItem().toString()); // If not first entry, separate with +
                    }
                    prerequisiteTasksJBox.removeItem(prerequisiteTasksJBox.getSelectedItem()); // Remove option once selected
                    SelectedPrerequisiteF.setText("Selected prerequisite tasks: " + prerequisiteTaskID); // Display selection

                    // Resizes and centers current window by re-packing it
                    JComponent comp = (JComponent) e.getSource();
                    Window win = SwingUtilities.getWindowAncestor(comp);
                    win.pack();
                    win.setLocationRelativeTo(null);
                } else {
                    JOptionPane.showMessageDialog(TaskCPanel, "Error! There are no available tasks to be assigned. Either they have all been selected or you should first create a task.");
                }
            }
        });
        resetPrerequisitesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prerequisiteTaskID = "";
                if (assignProjectsJBox.getSelectedItem() != null) {
                    // Sets Prerequisite Tasks combo box to the Task IDs associated with the selected Project ID (task dependency must be on existing task)
                    prerequisiteTasksJBox.setModel(new DefaultComboBoxModel(handler.listTasksForTasks(assignProjectsJBox.getSelectedItem().toString())));
                }
                SelectedPrerequisiteF.setText("Selected prerequisite tasks appear here:");

                // Resizes and centers current window by re-packing it
                JComponent comp = (JComponent) e.getSource();
                Window win = SwingUtilities.getWindowAncestor(comp);
                win.pack();
                win.setLocationRelativeTo(null);
            }
        });
    }
}
