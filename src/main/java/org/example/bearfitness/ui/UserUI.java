package org.example.bearfitness.ui;

import org.example.bearfitness.data.DBService;
import org.example.bearfitness.user.*;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;


class UserUI extends JPanel {
    private DBService dbService;
    private ScreenManager screenManager;
    private User user;
    //private JTextArea plansDisplay;
    private WorkoutHistoryUI workoutHistoryUI;
    private AttendClassUI attendClassUI;
    private PieChartPanel pieChartPanel;
    private JProgressBar exerciseProgressBar;
    private JLabel exProgressText;

    public UserUI(DBService dbService, ScreenManager screenManager, User user) {
        this.dbService = dbService;
        this.screenManager = screenManager;
        this.user = user;
        Dimension defaultButtonSize = new Dimension(50, 30);

        setLayout(new BorderLayout());

        JButton settingsButton = new JButton("Settings");
        settingsButton.addActionListener(e-> screenManager.showScreen(ScreenManager.Screen.SETTINGS));

        JButton viewClassesButton = new JButton("View/Search Classes");
        viewClassesButton.addActionListener(e -> screenManager.showScreen(ScreenManager.Screen.VIEW_CLASSES));

        JButton viewMyPlansButton = new JButton("View My Plans");
        viewMyPlansButton.addActionListener(e -> {screenManager.showScreen(ScreenManager.Screen.VIEW_PLANS, user);});


        JButton viewGoalsDisplayButton = new JButton("View Tracked Goals");
        viewGoalsDisplayButton.addActionListener(e -> screenManager.showScreen(ScreenManager.Screen.GOALS));

        // Create the title label
        JLabel titleLabel = new JLabel("Welcome, " + user.getUsername() + "!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        JPanel centerTitlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerTitlePanel.add(titleLabel);


        // Right-aligned settings button panel
        JPanel rightButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightButtonPanel.add(settingsButton);
        JPanel leftButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftButtonPanel.add(viewClassesButton);
        leftButtonPanel.add(viewMyPlansButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(centerTitlePanel, BorderLayout.CENTER);
        topPanel.add(rightButtonPanel, BorderLayout.EAST);
        topPanel.add(leftButtonPanel, BorderLayout.WEST);

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


        //----EXERCISE----
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        JLabel exLabel = new JLabel("Progress towards Weekly Exercise Goal:");
        exLabel.setFont(new Font("Sans Serif", Font.BOLD, 14));
        dataPanel.add(exLabel, gbc);

        // Progress bar
        this.exerciseProgressBar = new JProgressBar();
        exerciseProgressBar.setMinimum(0);


        int exThisWeek = dbService.getExerciseLastWeek(user.getId());
        int weeklyExGoal = user.getGoals().getWeeklyExercises();
        //System.out.println("EXERCISE THIS WEEK: " + exThisWeek);

        exerciseProgressBar.setMaximum(weeklyExGoal);
        exerciseProgressBar.setValue(exThisWeek);
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.weightx = 2;
        dataPanel.add(exerciseProgressBar, gbc);
        

        // Progress text under bar
        exProgressText = new JLabel(exThisWeek+" / " + weeklyExGoal + " sessions");
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        dataPanel.add(exProgressText, gbc);


        JButton updateExGoalButton = new JButton("Update Goal");
        gbc.gridy = 1;
        gbc.gridx = 2;
        gbc.gridwidth = 1;
        gbc.weightx = 0.7;
        //gbc.fill = GridBagConstraints.NONE;
        updateExGoalButton.setPreferredSize(defaultButtonSize);
        dataPanel.add(updateExGoalButton, gbc);

        updateExGoalButton.addActionListener(e->{
            Integer newGoal = getValidEx(this);
            user.setWeeklyExercises(newGoal);
            dbService.updateUserData(user);

            // Refresh progress bar and label
            int updatedGoal = user.getGoals().getWeeklyExercises();
            //int updatedCalories = user.getUserStats().getCaloriesLastWeek();

            exerciseProgressBar.setMaximum(updatedGoal);
            exerciseProgressBar.setValue(dbService.getExerciseLastWeek(user.getId()));

            exProgressText.setText(dbService.getExerciseLastWeek(user.getId()) + " / " + updatedGoal);
            refresh();
            updateExerciseUI();
        });

        updateExerciseUI();


        //-----CALORIES-----
        // Label above progress bar
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        JLabel calorieLabel = new JLabel("Progress towards Weekly Calorie Goal:");
        calorieLabel.setFont(new Font("Sans Serif", Font.BOLD, 14));
        dataPanel.add(calorieLabel, gbc);

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
        gbc.weightx = 2;
        dataPanel.add(calProgressBar, gbc);

        // "Log New Calories" button
        JButton logNewCaloriesButton = new JButton("Log New Calories");
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        //gbc.fill = GridBagConstraints.NONE;
        logNewCaloriesButton.setPreferredSize(defaultButtonSize);
        dataPanel.add(logNewCaloriesButton, gbc);

        // "Update Goal" button
        JButton updateGoalButton = new JButton("Update Goal");
        gbc.gridx = 2;
        gbc.gridwidth = 1;
        gbc.weightx = 0.7;
        updateGoalButton.setPreferredSize(logNewCaloriesButton.getPreferredSize());
        dataPanel.add(updateGoalButton, gbc);

        // Progress text under bar
        JLabel calProgressText = new JLabel(lastWeekCals+" / " + goalCals + " calories");
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

            calProgressText.setText(user.getUserStats().getCaloriesLastWeek() + " / " + updatedGoal+ " calories");
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

            calProgressText.setText(updatedCalories + " / " + user.getGoals().getGoalCalories()+ " calories");
            refresh();
        });

        //-----SLEEP-----
        // Label above progress bar
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 3;
        JLabel sleepLabel = new JLabel("Progress towards Weekly Sleep Goal:");
        sleepLabel.setFont(new Font("Sans Serif", Font.BOLD, 14));
        dataPanel.add(sleepLabel, gbc);


        // Progress bar
        JProgressBar sleepProgressBar = new JProgressBar();
        sleepProgressBar.setMinimum(0);
        Double goalSleep = user.getGoals().getGoalSleep();
        Double lastWeekSleep = user.getUserStats().getSleepLastWeek();
        sleepProgressBar.setMaximum((int)(goalSleep*10));
        sleepProgressBar.setValue((int)(lastWeekSleep*10));
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.weightx = 2;
        dataPanel.add(sleepProgressBar, gbc);

        // "Log New Sleep" button
        JButton logNewSleepButton = new JButton("Log New Sleep");
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        //gbc.fill = GridBagConstraints.NONE;
        logNewSleepButton.setPreferredSize(defaultButtonSize);
        dataPanel.add(logNewSleepButton, gbc);

        // "Update Goal" button
        JButton updateSleepGoalButton = new JButton("Update Goal");
        gbc.gridx = 2;
        gbc.gridwidth = 1;
        gbc.weightx = 0.7;
        //gbc.fill = GridBagConstraints.NONE;
        updateSleepGoalButton.setPreferredSize(logNewCaloriesButton.getPreferredSize());
        dataPanel.add(updateSleepGoalButton, gbc);

        // Progress text under bar
        JLabel sleepProgressText = new JLabel(lastWeekSleep+" / " + goalSleep+" hours");
        gbc.gridy = 8;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        dataPanel.add(sleepProgressText, gbc);


        updateSleepGoalButton.addActionListener(e->{
            Double newGoal = getValidSleep(this);
            user.setNewGoalSleep(newGoal);
            dbService.updateUserData(user);

            // Refresh progress bar and label
            double updatedGoal = user.getGoals().getGoalSleep();

            sleepProgressBar.setMaximum((int)(updatedGoal*10));
            sleepProgressBar.setValue((int)(user.getUserStats().getSleepLastWeek()*10));

            sleepProgressText.setText(user.getUserStats().getSleepLastWeek() + " / " + updatedGoal+" hours");
            refresh();
        });

        logNewSleepButton.addActionListener(e->{
            Double newSleep = getValidSleep(this);
            user.logNewSleep(newSleep);
            dbService.updateUserData(user);
            // Refresh progress bar and label
            //int updatedGoal = user.getGoals().getGoalCalories();
            double updatedSleep = user.getUserStats().getSleepLastWeek();

            //calProgressBar.setMaximum(updatedGoal);
            sleepProgressBar.setValue((int)(updatedSleep*10));

            sleepProgressText.setText(updatedSleep + " / " + user.getGoals().getGoalSleep()+" hours");
            refresh();
        });

        //----WEIGHT----
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 3;

        AtomicReference<Double> mostRecentWeight = new AtomicReference<>(0.0);
        Map<LocalDate, Double> weightLog = user.getUserStats().getWeightLog();
        if (!weightLog.isEmpty()) {
            LocalDate mostRecentDate = Collections.max(weightLog.keySet());
            mostRecentWeight.set(weightLog.get(mostRecentDate));
        }

        gbc.weightx = 2;
        JLabel currentWeightLabel = new JLabel("Current Weight: " + mostRecentWeight + " lbs");
        currentWeightLabel.setFont(new Font("Sans Serif", Font.BOLD, 14));
        dataPanel.add(currentWeightLabel,gbc);

        JButton logNewWeightButton = new JButton("Log New Weight");
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        //gbc.fill = GridBagConstraints.NONE;
        logNewWeightButton.setPreferredSize(new Dimension(280,30));
        dataPanel.add(logNewWeightButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 3;
        gbc.weightx = 2;
        JLabel currentGoalLabel = new JLabel("Goal Weight: " + user.getGoals().getGoalWeight() + " lbs");
        currentGoalLabel.setFont(new Font("Sans Serif", Font.BOLD, 14));
        dataPanel.add(currentGoalLabel,gbc);

        JButton updateWeightGoalButton = new JButton("Update Goal Weight");
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        //gbc.fill = GridBagConstraints.NONE;
        updateWeightGoalButton.setPreferredSize(logNewWeightButton.getPreferredSize());
        dataPanel.add(updateWeightGoalButton, gbc);

        updateWeightGoalButton.addActionListener(e->{
            Double newGoal = getValidWeight(this);
            user.setGoalWeight(newGoal);
            dbService.updateUserData(user);
            double updatedGoal = user.getGoals().getGoalWeight();
            currentGoalLabel.setText("Goal Weight: " + updatedGoal + " lbs");
            refresh();
        });

        logNewWeightButton.addActionListener(e->{
            Double newWeight = getValidWeight(this);
            user.recordWeight(LocalDate.now(),newWeight);
            dbService.updateUserData(user);
            // Refresh progress bar and label
            if (!weightLog.isEmpty()) {
                LocalDate mostRecentDate = Collections.max(weightLog.keySet());
                mostRecentWeight.set(weightLog.get(mostRecentDate));
            }

            currentWeightLabel.setText("Current Weight: " + mostRecentWeight + " lbs");
            refresh();
        });

        //display goals button
        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.gridwidth = 3;

        dataPanel.add(viewGoalsDisplayButton, gbc);

        rightContent.add(dataPanel);
        //breakdown of time spent this month, total exercises logged, goal progress, etc.

        // Add the content split to the center of the rightPanel
        rightPanel.add(rightContent, BorderLayout.CENTER);



        //Left: Workout History
        JPanel leftPanel = new JPanel(new BorderLayout());
        workoutHistoryUI = new WorkoutHistoryUI(dbService, screenManager, user, this);
        attendClassUI = new AttendClassUI(dbService, user, this);
        leftPanel.add(workoutHistoryUI, BorderLayout.CENTER);
        leftPanel.add(attendClassUI, BorderLayout.SOUTH);

        splitPane.setRightComponent(rightPanel);
        //splitPane.setLeftComponent(workoutHistoryUI);
        splitPane.setLeftComponent(leftPanel);

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
                int newCal = Integer.parseInt(input);
                if (newCal > 0) return newCal;
                JOptionPane.showMessageDialog(parent, "Calories must be positive", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(parent, "Please enter a valid number", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    private static Integer getValidEx(JPanel parent) {
        while (true) {
//            int dateResult = JOptionPane.showConfirmDialog(
//                    parent,
//
//                    "Select Workout Date",
//                    JOptionPane.OK_CANCEL_OPTION
//            );

            try {
                String input = JOptionPane.showInputDialog(parent, "Enter New Workout Goal:");
                if (input == null) return null;
                int newWorkout = Integer.parseInt(input);
                if (newWorkout > 0) return newWorkout;
                JOptionPane.showMessageDialog(parent, "Goal must be positive", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(parent, "Please enter a valid number", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    private static Double getValidSleep(JPanel parent) {
        while (true) {
//            int dateResult = JOptionPane.showConfirmDialog(
//                    parent,
//
//                    "Select Workout Date",
//                    JOptionPane.OK_CANCEL_OPTION
//            );

            try {
                String input = JOptionPane.showInputDialog(parent, "Enter New Sleep (hrs per week):");
                if (input == null) return null;
                Double newSleep = Double.parseDouble(input);
                if (newSleep > 0.0 && newSleep <168.0) return newSleep;
                JOptionPane.showMessageDialog(parent, "Sleep must be positive and less than 24 hrs/day", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(parent, "Please enter a valid number", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    private static Double getValidWeight(JPanel parent) {
        while (true) {
//            int dateResult = JOptionPane.showConfirmDialog(
//                    parent,
//
//                    "Select Workout Date",
//                    JOptionPane.OK_CANCEL_OPTION
//            );

            try {
                String input = JOptionPane.showInputDialog(parent, "Enter New Weight (lbs):");
                if (input == null) return null;
                Double newWeight = Double.parseDouble(input);
                if (newWeight > 0.0) return newWeight;
                JOptionPane.showMessageDialog(parent, "Weight must be positive", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(parent, "Please enter a valid number", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    public void updateExerciseUI() {
        int done  = dbService.getExerciseLastWeek(user.getId());
        int goal  = user.getGoals().getWeeklyExercises();
        exerciseProgressBar.setValue(done);
        exProgressText.setText(done + " / " + goal + " sessions");
    }

    public void refresh() {
        workoutHistoryUI.refresh();
        attendClassUI.refresh();
        pieChartPanel.refreshChart();
        updateExerciseUI();
    }
}
