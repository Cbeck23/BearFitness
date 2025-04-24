/*

=============NOTE==================

This is not a functional class. I tried to make it functional but it was too annoying to work around all the
existing structures and pages so I just stuck with the old UserUI class.
This is only here for reference as to what a final home page will look like (hopefully) in the end.

====================================

*/
package org.example.bearfitness.ui;

import org.example.bearfitness.data.DBService;
import org.example.bearfitness.user.User;

import javax.swing.*;
import java.awt.*;

public class BasicUserHomePage extends JPanel {
    private JProgressBar calProgressBar;
    private JButton logNewCaloriesButton;
    private JProgressBar slpProgressBar;
    private JButton logNewSleepButton;
    private JProgressBar exProgressBar;
    private JButton updateWeightButton;
    private JButton viewPlansButton;
    private JButton viewClassesButton;
    private JPanel WorkoutHistoryTable;
    private JButton settingsButton;
    private JButton viewHistoryButton;
    private JButton updateWtGoalButton;
    private JButton updateCalGoalButton;
    private JButton updateSlpGoalButton;
    private JPanel ChartPanel;
    private JLabel ExProgressLabel;
    private JLabel exProgressAmt;
    private JLabel currWeight;
    private JLabel goalWeight;
    private JLabel calProgressLabel;
    private JLabel calProgressAmt;
    private JLabel slpProgressLabel;
    private JLabel slpProgressAmt;
    private JPanel homePage;
    private JSplitPane splitScreen;
    private JPanel rightSide;
    private JLabel userTitle;

    private DBService dbService;
    private ScreenManager screenManager;
    private User user;
    //private JTextArea plansDisplay;
    private WorkoutHistoryUI workoutHistoryUI;
    private PieChartPanel pieChartPanel;

    public BasicUserHomePage(DBService dbService, ScreenManager screenManager, User user) {
//        this.dbService = dbService;
//        this.screenManager = screenManager;
//        this.user = user;
//
//        JFrame frame = new JFrame("UserHomepage");
//
//        frame.setContentPane(this.homePage);
//        frame.pack();
//
//        setLayout(new BorderLayout());
//
//        settingsButton.addActionListener(e-> screenManager.showScreen(ScreenManager.Screen.SETTINGS));
//
//
//        userTitle.setText("Welcome, " + user.getUsername() + "!");
//
//
//
//        // SplitPane for left/right resizable layout
//
//
//        // Split the content below the title
//        //JPanel rightContent = new JPanel(new GridLayout(2, 1));  // Two rows, one for pie, one for future use
//        ChartPanel.removeAll(); // Clear existing contents (just in case)
//        pieChartPanel = new PieChartPanel(dbService, user); // Create your chart panel
//        ChartPanel.setLayout(new BorderLayout()); // Or whatever layout suits your content
//        ChartPanel.add(pieChartPanel, BorderLayout.CENTER); // Add it to the center
//        ChartPanel.revalidate(); // Recalculate layout
//        ChartPanel.repaint();
//
//        //PLACEHOLDER
//        //breakdown of time spent this month, total exercises logged, goal progress, etc.
//        goalWeight.setText(String.valueOf(user.getGoals().getGoalWeight()));
//        updateWeightButton.addActionListener(e -> {
//            Integer newGoalWt = getValidWeight(frame);
//            user.setGoalWeight(newGoalWt);
//            goalWeight.setText(newGoalWt.toString());
//        });
//        // Add the content split to the center of the rightPanel
//
//
//
//        //Left: Workout History
//        workoutHistoryUI = new WorkoutHistoryUI(dbService, screenManager, user, this);
//
//        WorkoutHistoryTable.add(workoutHistoryUI);

    }

    private static Integer getValidWeight(JFrame parent) {
        while (true) {
//            int dateResult = JOptionPane.showConfirmDialog(
//                    parent,
//
//                    "Select Workout Date",
//                    JOptionPane.OK_CANCEL_OPTION
//            );

            try {
                String input = JOptionPane.showInputDialog(parent, "Enter New Weight:");
                if (input == null) return null;
                int newWeight = Integer.parseInt(input);
                if (newWeight > 0) return newWeight;
                JOptionPane.showMessageDialog(parent, "Weight must be positive", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(parent, "Please enter a valid number", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    public JPanel getPanel() {
        return homePage;
    }

    public void setScreenManager(ScreenManager screenManager) {
        this.screenManager = screenManager;
    }

    public void refresh() {
        workoutHistoryUI.refresh();
        pieChartPanel.refreshChart();
    }

}
