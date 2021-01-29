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
    private JButton editSelectedTeamButton;
    private JTextField newDataF;
    private JComboBox dataToEditJBox;
    private TaskHandler handler;
    private TaskHandler handlerTwo;
    private List<Tasks> task;
    private int selectedIndex;

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
        // Set dataToEditJBox combo box to options excluding the Team ID
        String[] taskDataToEdit = "Commissioner,Project Manager,Duration,Progress".split(",");
        dataToEditJBox.setModel(new DefaultComboBoxModel(taskDataToEdit));

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
                if (!e.getValueIsAdjusting()) { // Prevents event from firing twice upon mouseclick
                    selectedIndex = taskJList.getSelectedIndex();
                }
            }
        });
        editSelectedTeamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handlerTwo = new TaskHandler(); // Create second instance to prevent clashes in mutable lists that cause saving issues
                if (taskJList.getSelectedValue() != null &&
                        (
                                (ProjectCreator.validationCheck(newDataF.getText(), false)) &&
                                (!newDataF.getText().isEmpty())
                        ) // Checks if newDataF contents is a valid string input
                ) {
                    handlerTwo.updateTask(selectedIndex, dataToEditJBox.getSelectedIndex() + 2, newDataF.getText());
                    JOptionPane.showMessageDialog(TaskVPanel, "Task Data Updated.");

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
                } else if (ProjectCreator.validationCheck(newDataF.getText(), false) || !newDataF.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(TaskVPanel, "Error! Avoid using special characters or invalid inputs (e.g. empty text field)");
                } else {
                    JOptionPane.showMessageDialog(TaskVPanel, "Error! There is either no project available or selected from the list.");
                }
            }
        });
    }
}
