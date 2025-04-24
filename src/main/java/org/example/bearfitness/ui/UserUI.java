package org.example.bearfitness.ui;

import org.example.bearfitness.data.DBService;
import org.example.bearfitness.user.*;
import org.example.bearfitness.ui.ScreenManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;


class UserUI extends JPanel {
    private DBService dbService;
    private ScreenManager screenManager;
    private User user;
    //private JTextArea plansDisplay;
    private WorkoutHistoryUI workoutHistoryUI;
    private PieChartPanel pieChartPanel;

    public UserUI(DBService dbService, ScreenManager screenManager, User user) {
        this.dbService = dbService;
        this.screenManager = screenManager;
        this.user = user;

        setLayout(new BorderLayout());

        JButton settingsButton = new JButton("Settings");

//        settingsButton.addActionListener(e-> UserSettings.show(
//                dbService,
//                (JFrame) SwingUtilities.getWindowAncestor(this),
//                user,
//                screenManager));
        //UserSettings settingsPage = new UserSettings(dbService, screenManager, user, this);
        settingsButton.addActionListener(e-> screenManager.showScreen(ScreenManager.Screen.SETTINGS));

        JPanel topButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        //topButtons.add(addWorkout);
        topButtons.add(settingsButton);
        add(topButtons, BorderLayout.NORTH);

        // SplitPane for left/right resizable layout
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.25);
        splitPane.setDividerLocation(0.25);

        // Right Panel Container
        JPanel rightPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Welcome, " + user.getUsername() + "!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        rightPanel.add(titleLabel, BorderLayout.NORTH);

        // Split the content below the title
        JPanel rightContent = new JPanel(new GridLayout(2, 1));  // Two rows, one for pie, one for future use
        pieChartPanel = new PieChartPanel(dbService, user);
        rightContent.add(pieChartPanel);

        //PLACEHOLDER
        JPanel futurePanel = new JPanel();
        futurePanel.add(new JLabel("More charts & info"));
        rightContent.add(futurePanel);
        //breakdown of time spent this month, total exercises logged, goal progress, etc.

        // Add the content split to the center of the rightPanel
        rightPanel.add(rightContent, BorderLayout.CENTER);



        //Left: Workout History
        workoutHistoryUI = new WorkoutHistoryUI(dbService, screenManager, user, this);

        splitPane.setRightComponent(rightPanel);
        splitPane.setLeftComponent(workoutHistoryUI);

        add(splitPane, BorderLayout.CENTER);

    }

    public void refresh() {
        workoutHistoryUI.refresh();
        pieChartPanel.refreshChart();
    }
}
