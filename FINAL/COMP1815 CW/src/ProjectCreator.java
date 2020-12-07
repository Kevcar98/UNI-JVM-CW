import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ProjectCreator {
    public JPanel ProjectCPanel;
    private JButton backToMainMenuButton;
    private JButton createProjectButton;
    private JTextField ProjectIDF;
    private JTextField CommissionerF;
    private JTextField ProjectManagerF;
    private JLabel ResultF;
    private JComboBox assignTeamsJBox;
    private JButton resetTeamsButton;
    private JButton addTeamButton;
    private JLabel SelectedTeamF;
    private String teamSelection = "";
    private ProjectHandler handler;
    private List<Project> project;

    public static boolean validationCheck(String s, boolean numberOnly) {
        Pattern alpha = Pattern.compile("[^A-Za-z0-9 ]");
        Pattern numbers = Pattern.compile("[^0-9]");
        Matcher alphaMatcher = alpha.matcher(s);
        Matcher numbersMatcher = numbers.matcher(s);
        boolean alphaBoolean = alphaMatcher.find();
        boolean numbersBoolean = numbersMatcher.find();
        if ((alphaBoolean && !numberOnly) || (numbersBoolean && numberOnly)) {
            System.out.println(s + " did not pass validation check");
            return false;
        }
        return true;
    }

    public static boolean validationDateCheck(String inputDate) {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false); // Must be in given format
        try {
            sdf.parse(inputDate);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    public ProjectCreator() {
        handler = new ProjectHandler();
        assignTeamsJBox.setModel(new DefaultComboBoxModel(handler.listTeamsForProject()));

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
        createProjectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validationCheck(ProjectIDF.getText(), true) &&
                        validationCheck(CommissionerF.getText(), false) &&
                        validationCheck(ProjectManagerF.getText(), false) &&
                        !teamSelection.isEmpty()
                ) {
                    if (handler.uniqueIDCheck(ProjectIDF.getText())) {
                        project = handler.createProject(
                                ProjectIDF.getText(),
                                CommissionerF.getText(),
                                ProjectManagerF.getText(),
                                "None Currently Assigned",
                                teamSelection,
                                "",
                                "",
                                ""
                        );
                        ResultF.setText(project.get(project.size() - 1).toString()); // Displays last item in list
                        handler.save(project);

                        // Resizes and centers current window by re-packing it
                        JComponent comp = (JComponent) e.getSource();
                        Window win = SwingUtilities.getWindowAncestor(comp);
                        win.pack();
                        win.setLocationRelativeTo(null);

                        JOptionPane.showMessageDialog(ProjectCPanel, "Project saved.");

                        // Back to Main Menu
                        JFrame HomePF = new JFrame("Home Page");
                        HomePF.setContentPane(new HomePage().HomePanel);
                        HomePF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        HomePF.pack();
                        HomePF.setVisible(true);
                        HomePF.setLocationRelativeTo(null);
                        // Closes current window
                        comp = (JComponent) e.getSource();
                        win = SwingUtilities.getWindowAncestor(comp);
                        win.dispose();
                    } else {
                        JOptionPane.showMessageDialog(ProjectCPanel, "Error! Project ID is not unique!");
                        ResultF.setText("Project details appear here:");
                    }
                } else if (teamSelection.isEmpty()) {
                    JOptionPane.showMessageDialog(ProjectCPanel, "Error! There are no teams assigned. Please select at least one team (or create one if there are none available).");
                    ResultF.setText("Project details appear here:");
                } else {
                    JOptionPane.showMessageDialog(ProjectCPanel, "Error! Avoid using special characters or invalid inputs (e.g. letters in a text field expecting only numbers)");
                    ResultF.setText("Project details appear here:");
                }
            }
        });
        addTeamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (assignTeamsJBox.getSelectedItem() != null) { // Checks combo box if valid option selected
                    if (teamSelection.isEmpty()) {
                        teamSelection = teamSelection.concat(assignTeamsJBox.getSelectedItem().toString());
                    } else {
                        teamSelection = teamSelection.concat(" & " + assignTeamsJBox.getSelectedItem().toString()); // If not first entry, separate with &
                    }
                    assignTeamsJBox.removeItem(assignTeamsJBox.getSelectedItem()); // Remove option once selected
                    SelectedTeamF.setText("Selected teams: " + teamSelection); // Display selection

                    // Resizes and centers current window by re-packing it
                    JComponent comp = (JComponent) e.getSource();
                    Window win = SwingUtilities.getWindowAncestor(comp);
                    win.pack();
                    win.setLocationRelativeTo(null);
                } else {
                    JOptionPane.showMessageDialog(ProjectCPanel, "Error! There are no available teams to be assigned. Either they have all been selected or you should first create a team.");
                }
            }
        });
        resetTeamsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                teamSelection = "";
                assignTeamsJBox.setModel(new DefaultComboBoxModel(handler.listTeamsForProject()));
                SelectedTeamF.setText("Selected teams appear here:");

                // Resizes and centers current window by re-packing it
                JComponent comp = (JComponent) e.getSource();
                Window win = SwingUtilities.getWindowAncestor(comp);
                win.pack();
                win.setLocationRelativeTo(null);
            }
        });
    }
}
