import CriticalPath.KotlinCP;
import CriticalPath.ScalaCP;
import kotlin.Triple;
import scala.Tuple3;
import scala.collection.immutable.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ScalaCriticalPath {
    public JPanel ScalaCPPanel;
    private JButton backToMainMenuButton;
    private JButton submitButton;
    private JLabel NodesAmountL;
    private JLabel CPL;
    private JLabel DurationL;
    private JComboBox ProjectJBox;
    private JList treeJList;
    private ProjectHandler handler;
    private TaskHandler taskHandler;
    private ScalaCP cphandler;
    private KotlinCP kcpHandler;

    public ScalaCriticalPath() {
        handler = new ProjectHandler();
        taskHandler = new TaskHandler();
        kcpHandler = new KotlinCP();
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

                            Tuple3<String, Object, List<Object>> TreeInfo = cphandler.main(AssignedPTasks, AssignedNPTasks);

                            String tree = TreeInfo._1(); // Tree
                            treeJList.setListData(tree.split(",")); // Converts string to new array for JList

                            int OCPSize = Integer.parseInt(TreeInfo._2().toString()); // Size of critical path with the beginning node (parse object to int)
                            int CPSize = OCPSize - 1; // Size of critical path without the beginning node
                            NodesAmountL.setText("Number of Nodes in Critical Path: " + CPSize);

                            // The list of nodes in the critical path
                            String NodesOfPath = TreeInfo._3().toString();
                            NodesOfPath = NodesOfPath.replace(")", "");
                            NodesOfPath = NodesOfPath.replace(" ", "");
                            NodesOfPath = NodesOfPath.replace("List(0,", "");
                            // CPL.setText("Tasks on the Critical Path: " + NodesOfPath);

                            Triple<String[], String, String> kotlinCPInfo = kcpHandler.main(AssignedPTasks, AssignedNPTasks);
                            NodesAmountL.setText("Number of Nodes in Critical Path: " + kotlinCPInfo.component2());

                            String[] NodesOfPathL = NodesOfPath.split(","); // Turns string of nodes to a sting array

                            int DurationOfCP = 0;
                            int finalDur = 0;

                            for (int i = 0; i < NodesOfPathL.length; i++) {
                                // System.out.println("In loop: " + DurationOfCP);
                                DurationOfCP = taskHandler.TasksDurationForID(NodesOfPathL[i]);
                                finalDur = finalDur + DurationOfCP;
                                // System.out.println("Value that was passed: " + NodesOfPathL[i]);
                                // System.out.println("Value that was received: " + DurationOfCP);
                            } // Array to then get duration of critical tasks

                            // System.out.println("Out of loop: " + finalDur);
                            DurationL.setText("Duration of Critical Path of Project: " + kotlinCPInfo.component3());
                            // DurationL.setText("Duration of Critical Path of Project: " + finalDur);

                            // Resizes and centers current window by re-packing it
                            JComponent comp = (JComponent) e.getSource();
                            Window win = SwingUtilities.getWindowAncestor(comp);
                            win.pack();
                            win.setLocationRelativeTo(null);
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
