import CriticalPath.KotlinCP;
import kotlin.Triple;
import scala.Tuple3;
import scala.collection.immutable.List;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.Date;

public class KotlinCriticalPath {

    private JList kotlinProjectJList;
    private JLabel DurationL;
    private JButton submitButton;
    private JButton backToMainMenuButton;
    private JComboBox ProjectJBox;
    private JLabel NodesAmountL;
    public JPanel KotlinCPPanel;
    private ProjectHandler handler;
    private TaskHandler taskHandler;
    private KotlinCP cphandler;

    public KotlinCriticalPath() {
        handler = new ProjectHandler();
        taskHandler = new TaskHandler();
        cphandler = new KotlinCP();
        ProjectJBox.setModel(new DefaultComboBoxModel(taskHandler.listProjectsForTask()));
        kotlinProjectJList.setFont(new Font("monospaced", Font.BOLD, 15)); // Keep spacing of strings passed to JList

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
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ProjectJBox.getSelectedItem() != null) {
                    String ProjectID = ProjectJBox.getSelectedItem().toString();
                    String AssignedTasksID = handler.retrieveAssignedTasksID(ProjectID);
                    if (!AssignedTasksID.equals("") && !AssignedTasksID.equals("None Currently Assigned")) {
                        System.out.println("Assigned Tasks IDs: " + AssignedTasksID); // "31 & 32 & 123->33 & 1+2->5"
                        String[] AssignedTasks = AssignedTasksID.split(" & "); // [31,32,123->33,1+2->5]
                        String preq = "";
                        String nPreq = "";
                        for (int i = 0; i < AssignedTasks.length; i++) {
                            if (AssignedTasks[i].contains("->")) {
                                if (preq.isEmpty()) {
                                    preq = AssignedTasks[i];
                                } else {
                                    preq += "," + AssignedTasks[i];
                                } // If it is prerequisite task (e.g. 1+2->5), then add it to preq String, separated by , (e.g. preq = "123->33,1+2->5")
                            } else {
                                if (nPreq.isEmpty()) {
                                    nPreq = AssignedTasks[i];
                                } else {
                                    nPreq += "," + AssignedTasks[i];
                                } // nPreq = "31,32"
                            }
                        }
                        if (!preq.isEmpty()) {
                            String[] AssignedPTasks = preq.split(","); // [123->33,1+2->5]
                            String[] AssignedNPTasks = nPreq.split(","); // [31,32]

                            Triple<String[], String, String> kotlinCPInfo = cphandler.main(AssignedPTasks, AssignedNPTasks);
                            String[] taskStrings = kotlinCPInfo.component1();
                            kotlinProjectJList.setListData(taskStrings);

                            NodesAmountL.setText("Number of Nodes in Critical Path: " + kotlinCPInfo.component2());
                            DurationL.setText("Duration of Critical Path of Project: " + kotlinCPInfo.component3());

                            // Gets start date of project and returns finish date by adding the duration from critical path
                            LocalDate date = LocalDate.parse(handler.getProjectItem(ProjectID, 5));
                            String finishDate = date.plusDays(Long.parseLong(kotlinCPInfo.component3())).toString();

                            handler.multiUpdateProject(ProjectID, 6, finishDate, 7, kotlinCPInfo.component3());
                            JOptionPane.showMessageDialog(KotlinCPPanel, "Updated Duration and Finish Date of selected project. Projected Finish Date is: " + finishDate);

                            // Resizes and centers current window by re-packing it
                            JComponent comp = (JComponent) e.getSource();
                            Window win = SwingUtilities.getWindowAncestor(comp);
                            win.pack();
                            win.setLocationRelativeTo(null);
                        } else {
                            // Prevents passing an empty string as a parameter, if none of the tasks have prerequisites - critical path cannot be calculated
                            // At least one task will not have a prerequisite (nPreq is never empty), as an initial task must exist for there to be prerequisite tasks
                            JOptionPane.showMessageDialog(KotlinCPPanel, "Error! The tasks assigned to this project has no prerequisite tasks, so a critical path cannot be determined.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(KotlinCPPanel, "Error! There are no tasks assigned to this project. Please assign at least one task to this project first.");
                    }
                } else {
                    JOptionPane.showMessageDialog(KotlinCPPanel, "Error! There are no projects selected. Please select at least one project (or create one if there are none available).");
                }
            }
        });
    }
}
