import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;


public class ProjectViewer {
    public JPanel ProjectVPanel;
    private JButton backToMainMenuButton;
    private JList projectJList;
    private JButton editSelectedProjectButton;
    private JButton deleteSelectedProjectButton;
    private JTextField newDataF;
    private JComboBox dataToEditJBox;
    private ProjectHandler handler;
    private ProjectHandler handlerTwo;
    private List<Project> project;
    private int selectedIndex;

    public ProjectViewer() {
        handler = new ProjectHandler();
        project = handler.loadProjects();
        if (project != null) {
            projectJList.setListData(project.toArray(new Project[0])); // converts list to new array for JList
        } else if (project == null) {
            System.out.println("ERROR: Project is null");
        } else {
            System.out.println("ERROR: Project is of unknown status");
        }
        // Set dataToEditJBox combo box to options excluding the Team ID
        String[] projectDataToEdit = "Commissioner,Project Manager,Assigned Tasks,Assigned Teams,Start Date".split(",");
        dataToEditJBox.setModel(new DefaultComboBoxModel(projectDataToEdit));

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
        projectJList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) { // Prevents event from firing twice upon mouseclick
                    selectedIndex = projectJList.getSelectedIndex();
                }
            }
        });
        editSelectedProjectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handlerTwo = new ProjectHandler(); // Create second instance to prevent clashes in mutable lists that cause saving issues
                if (projectJList.getSelectedValue() != null &&
                        ((
                                (ProjectCreator.validationDateCheck(newDataF.getText())) &&
                                (dataToEditJBox.getSelectedItem().toString().equals("Start Date"))
                        ) ||
                        (
                                (ProjectCreator.validationCheck(newDataF.getText(), false)) &&
                                (!newDataF.getText().isEmpty())
                        )) // Checks if newDataF contents are a valid Date and the combo box has "Start Date" selected, or if it is a valid string input
                ) {
                    handlerTwo.updateProject(selectedIndex, dataToEditJBox.getSelectedIndex() + 1, newDataF.getText());
                    JOptionPane.showMessageDialog(ProjectVPanel, "Project Data Updated.");

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
                } else if (!ProjectCreator.validationDateCheck(newDataF.getText()) && dataToEditJBox.getSelectedItem().toString().equals("Start Date")) {
                    JOptionPane.showMessageDialog(ProjectVPanel, "Error! Invalid date format, please use the format YYYY-MM-DD");
                } else if (projectJList.getSelectedValue() == null) {
                    JOptionPane.showMessageDialog(ProjectVPanel, "Error! There is either no project available or selected from the list."); // Format is correct but did not select project
                } else if (ProjectCreator.validationCheck(newDataF.getText(), false) || !newDataF.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(ProjectVPanel, "Error! Avoid using special characters or invalid inputs (e.g. empty text field)");
                } else {
                    JOptionPane.showMessageDialog(ProjectVPanel, "Error! There is either no project available or selected from the list.");
                }
            }
        });
        deleteSelectedProjectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //
            }
        });
    }
}
