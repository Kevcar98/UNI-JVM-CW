import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


public class TeamCreator {
    public JPanel TeamCPanel;
    private JButton backToMainMenuButton;
    private JTextField TeamIDF;
    private JTextField TeamLeaderF;
    private JTextField TeamLocationF;
    private JTextField TeamMembersF;
    private JButton createTeamButton;
    private TeamHandler handler;
    private List<Teams> team;

    public TeamCreator() {
        handler = new TeamHandler();

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
        createTeamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ProjectCreator.validationCheck(TeamIDF.getText(), true) &&
                        ProjectCreator.validationCheck(TeamLeaderF.getText(), false) &&
                        ProjectCreator.validationCheck(TeamLocationF.getText(), false) &&
                        ProjectCreator.validationCheck(TeamMembersF.getText(), false)
                ) {
                    if (handler.uniqueIDCheck(TeamIDF.getText())) {
                        team = handler.createTeam(
                                TeamIDF.getText(),
                                TeamLeaderF.getText(),
                                TeamLocationF.getText(),
                                TeamMembersF.getText(),
                                "0"
                        );
                        handler.save(team);
                        JOptionPane.showMessageDialog(TeamCPanel, "Team saved.");

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
                        JOptionPane.showMessageDialog(TeamCPanel, "Error! Team ID is not unique!");
                    }
                } else {
                    JOptionPane.showMessageDialog(TeamCPanel, "Error! Avoid using special characters or invalid inputs (e.g. letters in a text field expecting only numbers)");
                }
            }
        });
    }
}
