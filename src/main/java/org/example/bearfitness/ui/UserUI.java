package org.example.bearfitness.ui;

import org.example.bearfitness.data.DBService;
import org.example.bearfitness.user.*;

import javax.swing.*;
import java.awt.*;


class UserUI extends JPanel {
    private DBService dbService;
    private ScreenManager screenManager;
    private User user;
    //private JTextArea plansDisplay;
    private WorkoutHistoryUI workoutHistoryUI;
    private PieChartPanel pieChartPanel;
    private JProgressBar exerciseProgressBar;
    private JLabel exProgressText;

    public UserUI(DBService dbService, ScreenManager screenManager, User user) {
        this.dbService = dbService;
        this.screenManager = screenManager;
        this.user = user;

        setLayout(new BorderLayout());

        JButton settingsButton = new JButton("Settings");

        settingsButton.addActionListener(e-> screenManager.showScreen(ScreenManager.Screen.SETTINGS));


        // Create the title label
        JLabel titleLabel = new JLabel("Welcome, " + user.getUsername() + "!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        JPanel centerTitlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerTitlePanel.add(titleLabel);


        // Right-aligned settings button panel
        JPanel rightButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightButtonPanel.add(settingsButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(centerTitlePanel, BorderLayout.CENTER);
        topPanel.add(rightButtonPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);



        // SplitPane for left/right resizable layout
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.25);
        splitPane.setDividerLocation(0.25);

        // Right Panel Container
        JPanel rightPanel = new JPanel(new BorderLayout());


        // Split the content below the title
        JPanel rightContent = new JPanel(new GridLayout(2, 1));  // Two rows, one for pie, one for future use
        pieChartPanel = new PieChartPanel(dbService, user);
        rightContent.add(pieChartPanel);


//        JPanel dataPanel = new JPanel();
//        dataPanel.add(new JLabel("More charts & info"));

        // STATS DISPLAY AND LOGGING
        JPanel dataPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;


        //EXERCISE
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        dataPanel.add(new JLabel("Progress towards Exercise Time Goal"), gbc);

        // Progress bar
        this.exerciseProgressBar = new JProgressBar();
        exerciseProgressBar.setMinimum(0);


        int exThisWeek = dbService.getExerciseLastWeek(user.getId());
        int weeklyExGoal = user.getGoals().getWeeklyExMinutes();

        exerciseProgressBar.setMaximum(weeklyExGoal);
        exerciseProgressBar.setValue(exThisWeek);
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        dataPanel.add(exerciseProgressBar, gbc);
        ;

        // Progress text under bar
        exProgressText = new JLabel(exThisWeek+" / " + weeklyExGoal + " hours");
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        dataPanel.add(exProgressText, gbc);

        updateExerciseUI();


        //-----CALORIES-----
        // Label above progress bar
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        dataPanel.add(new JLabel("Progress towards Calorie Goal"), gbc);

        // Progress bar
        JProgressBar calProgressBar = new JProgressBar();
        calProgressBar.setMinimum(0);
        int goalCals = user.getGoals().getGoalCalories();
        int lastWeekCals = user.getUserStats().getCaloriesLastWeek();
        calProgressBar.setMaximum(goalCals);
        calProgressBar.setValue(lastWeekCals);
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        dataPanel.add(calProgressBar, gbc);

        // "Log New Calories" button
        JButton logNewCaloriesButton = new JButton("Log New Calories");
        gbc.gridx = 1;
        gbc.weightx = 0;

        dataPanel.add(logNewCaloriesButton, gbc);

        // "Update Goal" button
        JButton updateGoalButton = new JButton("Update Goal");
        gbc.gridx = 2;
        dataPanel.add(updateGoalButton, gbc);

        // Progress text under bar
        JLabel calProgressText = new JLabel(lastWeekCals+" / " + goalCals);
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        dataPanel.add(calProgressText, gbc);


        updateGoalButton.addActionListener(e->{
            Integer newGoal = getValidCal(this);
            user.setNewGoalCalories(newGoal);
            dbService.updateUserData(user);

            // Refresh progress bar and label
            int updatedGoal = user.getGoals().getGoalCalories();
            //int updatedCalories = user.getUserStats().getCaloriesLastWeek();

            calProgressBar.setMaximum(updatedGoal);
            calProgressBar.setValue(user.getUserStats().getCaloriesLastWeek());

            calProgressText.setText(user.getUserStats().getCaloriesLastWeek() + " / " + updatedGoal);
            refresh();
        });

        logNewCaloriesButton.addActionListener(e->{
            Integer newCalories = getValidCal(this);
            user.logNewCalories(newCalories);
            dbService.updateUserData(user);
            // Refresh progress bar and label
            //int updatedGoal = user.getGoals().getGoalCalories();
            int updatedCalories = user.getUserStats().getCaloriesLastWeek();

            //calProgressBar.setMaximum(updatedGoal);
            calProgressBar.setValue(updatedCalories);

            calProgressText.setText(updatedCalories + " / " + user.getGoals().getGoalCalories());
            refresh();
        });

        rightContent.add(dataPanel);
        //breakdown of time spent this month, total exercises logged, goal progress, etc.

        // Add the content split to the center of the rightPanel
        rightPanel.add(rightContent, BorderLayout.CENTER);



        //Left: Workout History
        workoutHistoryUI = new WorkoutHistoryUI(dbService, screenManager, user, this);

        splitPane.setRightComponent(rightPanel);
        splitPane.setLeftComponent(workoutHistoryUI);

        add(splitPane, BorderLayout.CENTER);

    }

    private static Integer getValidCal(JPanel parent) {
        while (true) {
//            int dateResult = JOptionPane.showConfirmDialog(
//                    parent,
//
//                    "Select Workout Date",
//                    JOptionPane.OK_CANCEL_OPTION
//            );

            try {
                String input = JOptionPane.showInputDialog(parent, "Enter New Calories:");
                if (input == null) return null;
                int newWeight = Integer.parseInt(input);
                if (newWeight > 0) return newWeight;
                JOptionPane.showMessageDialog(parent, "Calories must be positive", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(parent, "Please enter a valid number", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    public void updateExerciseUI() {
        int done  = dbService.getExerciseLastWeek(user.getId());
        int goal  = user.getGoals().getWeeklyExMinutes();
        exerciseProgressBar.setValue(done);
        exProgressText.setText(done + " / " + goal + " hours");
    }

    public void refresh() {
        workoutHistoryUI.refresh();
        pieChartPanel.refreshChart();
        updateExerciseUI();
    }
}
