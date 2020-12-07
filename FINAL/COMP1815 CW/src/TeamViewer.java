import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


public class TeamViewer {
    public JPanel TeamVPanel;
    private JButton backToMainMenuButton;
    private JList teamJList;
    private JButton editSelectedTeamButton;
    private JComboBox dataToEditJBox;
    private JTextField newDataF;
    private JButton deleteSelectedTeamButton;
    private TeamHandler handler;
    private TeamHandler handlerTwo;
    private List<Teams> team;
    private int selectedIndex;

    public TeamViewer() {
        handler = new TeamHandler();
        team = handler.loadTeams();
        if (team != null) {
            teamJList.setListData(team.toArray(new Teams[0])); // converts list to new array for JList
        } else if (team == null) {
            System.out.println("ERROR: Team is null");
        } else {
            System.out.println("ERROR: Team is of unknown status");
        }
        // Set dataToEditJBox combo box to options excluding the Team ID
        String[] teamDataToEdit = "Team Leader,Team Members,Team Location".split(",");
        dataToEditJBox.setModel(new DefaultComboBoxModel(teamDataToEdit));

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
        teamJList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) { // Prevents event from firing twice upon mouseclick
                    selectedIndex = teamJList.getSelectedIndex();
                }
            }
        });
        editSelectedTeamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handlerTwo = new TeamHandler(); // Create second instance to prevent clashes in mutable lists that cause saving issues
                if (teamJList.getSelectedValue() != null &&
                        ProjectCreator.validationCheck(newDataF.getText(), false) &&
                        !newDataF.getText().isEmpty()
                ) {
                    handlerTwo.updateTeam(selectedIndex, dataToEditJBox.getSelectedIndex() + 1, newDataF.getText());
                    JOptionPane.showMessageDialog(TeamVPanel, "Team Data Updated.");

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
                    JOptionPane.showMessageDialog(TeamVPanel, "Error! Avoid using special characters or invalid inputs (e.g. empty text field)");
                } else {
                    JOptionPane.showMessageDialog(TeamVPanel, "Error! There is either no team available or selected from the list.");
                }
            }
        });
        deleteSelectedTeamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handlerTwo = new TeamHandler(); // Create second instance to prevent clashes in mutable lists that cause saving issues
                if (teamJList.getSelectedValue() != null) {
                    handlerTwo.deleteTeam(selectedIndex);
                    JOptionPane.showMessageDialog(TeamVPanel, "Team Data Deleted.");

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
                    JOptionPane.showMessageDialog(TeamVPanel, "Error! There is either no team available or selected from the list.");
                }
            }
        });
    }
}
