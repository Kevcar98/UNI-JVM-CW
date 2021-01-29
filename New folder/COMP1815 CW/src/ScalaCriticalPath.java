import scala.Tuple4;
import scala.collection.immutable.List;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;


public class ScalaCriticalPath {
    public JPanel ScalaCPPanel;
    private JButton backToMainMenuButton;
    private JButton submitButton;
    private JLabel NodesAmountL;
    private JLabel DurationL;
    private JComboBox ProjectJBox;
    private JList treeJList;
    private JLabel CPL;
    private JLabel TaskL;
    private ProjectHandler handler;
    private TaskHandler taskHandler;
    private ScalaCP cphandler;

    public ScalaCriticalPath() {
        handler = new ProjectHandler();
        taskHandler = new TaskHandler();
        cphandler = new ScalaCP();
        ProjectJBox.setModel(new DefaultComboBoxModel(taskHandler.listProjectsForTask())); // Sets Projects combo box to list of ProjectIDs

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
                        TaskL.setText("Assigned Tasks IDs: " + AssignedTasksID); // "31 & 32 & 123->33 & 1+2->5"
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

                            Tuple4<String, Object, List<Object>, Object> TreeInfo = cphandler.main(AssignedPTasks, AssignedNPTasks);

                            String tree = TreeInfo._1(); // Tree
                            treeJList.setListData(tree.split(",")); // Converts string to new array for JList

                            int OCPSize = Integer.parseInt(TreeInfo._2().toString()); // Size of critical path with the beginning node (parse object to int)
                            int CPSize = OCPSize - 1; // Size of critical path without the beginning node
                            NodesAmountL.setText("Number of Nodes in Critical Path: " + CPSize);

                            // The list of nodes in the critical path
                            String NodesOfPath = TreeInfo._3().toString();
                            NodesOfPath = NodesOfPath.replace(")", "");
                            NodesOfPath = NodesOfPath.replace(" ", "");
                            NodesOfPath = NodesOfPath.replace("List(", "0,");
                            CPL.setText("Tasks on the Critical Path: " + NodesOfPath);

                            DurationL.setText("Duration of Critical Path of Project: " + TreeInfo._4().toString());

                            // Gets start date of project and returns finish date by adding the duration from critical path
                            LocalDate date = LocalDate.parse(handler.getProjectItem(ProjectID, 5));
                            String finishDate = date.plusDays(Long.parseLong(TreeInfo._4().toString())).toString();

                            handler.multiUpdateProject(ProjectID, 6, finishDate, 7, TreeInfo._4().toString());

                            // Resizes and centers current window by re-packing it
                            JComponent comp = (JComponent) e.getSource();
                            Window win = SwingUtilities.getWindowAncestor(comp);
                            win.pack();
                            win.setLocationRelativeTo(null);

                            JOptionPane.showMessageDialog(ScalaCPPanel, "Updated Duration and Finish Date of selected project. Projected Finish Date is: " + finishDate);
                        } else {
                            // Prevents passing an empty string as a parameter, if none of the tasks have prerequisites - critical path cannot be calculated
                            // At least one task will not have a prerequisite (nPreq is never empty), as an initial task must exist for there to be prerequisite tasks
                            JOptionPane.showMessageDialog(ScalaCPPanel, "Error! The tasks assigned to this project has no prerequisite tasks, so a critical path cannot be determined.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(ScalaCPPanel, "Error! There are no tasks assigned to this project. Please assign at least one task to this project first.");
                    }
                } else {
                    JOptionPane.showMessageDialog(ScalaCPPanel, "Error! There are no projects selected. Please select at least one project (or create one if there are none available).");
                }
            }
        });
    }
}
