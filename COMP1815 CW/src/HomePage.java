import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class HomePage {
    public JPanel HomePanel;
    private JButton viewTeamsButton;
    private JButton createTeamButton;
    private JButton createProjectButton;
    private JButton viewProjectsButton;
    private JButton createTaskButton;
    private JButton viewTasksButton;
    private JButton updateTaskProgressButton;
    private JButton updateTaskDurationButton;
    private JButton ScalaCriticalPath;

    public static void main(String[] args) {
        JFrame HomePF = new JFrame("Home Page");
        HomePF.setContentPane(new HomePage().HomePanel);
        HomePF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        HomePF.pack();
        HomePF.setVisible(true);
        HomePF.setLocationRelativeTo(null);
    }

    public HomePage() {
        createTeamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame CreateTmF = new JFrame("Team Creator");
                CreateTmF.setContentPane(new TeamCreator().TeamCPanel);
                CreateTmF.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                CreateTmF.pack();
                CreateTmF.setVisible(true);
                CreateTmF.setLocationRelativeTo(null);
                // Closes current window
                JComponent comp = (JComponent) e.getSource();
                Window win = SwingUtilities.getWindowAncestor(comp);
                win.dispose();
            }
        });
        createProjectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame CreatePrF = new JFrame("Project Creator");
                CreatePrF.setContentPane(new ProjectCreator().ProjectCPanel);
                CreatePrF.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                CreatePrF.pack();
                CreatePrF.setVisible(true);
                CreatePrF.setLocationRelativeTo(null);
                // Closes current window
                JComponent comp = (JComponent) e.getSource();
                Window win = SwingUtilities.getWindowAncestor(comp);
                win.dispose();
            }
        });
        viewTeamsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame ViewTmF = new JFrame("Team Viewer");
                ViewTmF.setContentPane(new TeamViewer().TeamVPanel);
                ViewTmF.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                ViewTmF.pack();
                ViewTmF.setVisible(true);
                ViewTmF.setLocationRelativeTo(null);
                // Closes current window
                JComponent comp = (JComponent) e.getSource();
                Window win = SwingUtilities.getWindowAncestor(comp);
                win.dispose();
            }
        });
        viewProjectsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame ViewPrF = new JFrame("Project Viewer");
                ViewPrF.setContentPane(new ProjectViewer().ProjectVPanel);
                ViewPrF.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                ViewPrF.pack();
                ViewPrF.setVisible(true);
                ViewPrF.setLocationRelativeTo(null);
                // Closes current window
                JComponent comp = (JComponent) e.getSource();
                Window win = SwingUtilities.getWindowAncestor(comp);
                win.dispose();
            }
        });
        createTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame CreateTsF = new JFrame("Task Creator");
                CreateTsF.setContentPane(new TaskCreator().TaskCPanel);
                CreateTsF.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                CreateTsF.pack();
                CreateTsF.setVisible(true);
                CreateTsF.setLocationRelativeTo(null);
                // Closes current window
                JComponent comp = (JComponent) e.getSource();
                Window win = SwingUtilities.getWindowAncestor(comp);
                win.dispose();
            }
        });
        viewTasksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame ViewTsF = new JFrame("Tasks Viewer");
                ViewTsF.setContentPane(new TaskViewer().TaskVPanel);
                ViewTsF.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                ViewTsF.pack();
                ViewTsF.setVisible(true);
                ViewTsF.setLocationRelativeTo(null);
                // Closes current window
                JComponent comp = (JComponent) e.getSource();
                Window win = SwingUtilities.getWindowAncestor(comp);
                win.dispose();
            }
        });
        updateTaskProgressButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame TaskUp = new JFrame("Update Tasks");
                TaskUp.setContentPane(new UpdateTask().UpdateTPanel);
                TaskUp.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                TaskUp.pack();
                TaskUp.setVisible(true);
                TaskUp.setLocationRelativeTo(null);
                // Closes current window
                JComponent comp = (JComponent) e.getSource();
                Window win = SwingUtilities.getWindowAncestor(comp);
                win.dispose();
            }
        });
		updateTaskDurationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame TaskUpD = new JFrame("Update Task Duration");
                TaskUpD.setContentPane(new TaskDurationUpdater().UpdateTaskDurationPanel);
                TaskUpD.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                TaskUpD.pack();
                TaskUpD.setVisible(true);
                TaskUpD.setLocationRelativeTo(null);
                // Closes current window
				JComponent comp = (JComponent) e.getSource();
                Window win = SwingUtilities.getWindowAncestor(comp);
                win.dispose();
            }
        });
        ScalaCriticalPath.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame SCPath = new JFrame("Scala Critical Path");
                SCPath.setContentPane(new ScalaCriticalPath().ScalaCPPanel);
                SCPath.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                SCPath.pack();
                SCPath.setVisible(true);
                SCPath.setLocationRelativeTo(null);
                // Closes current window
                JComponent comp = (JComponent) e.getSource();
                Window win = SwingUtilities.getWindowAncestor(comp);
                win.dispose();
            }
        });
    }
}
