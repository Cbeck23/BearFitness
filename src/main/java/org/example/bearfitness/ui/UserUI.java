package org.example.bearfitness.UI;

import org.example.bearfitness.data.DBService;
import org.example.bearfitness.user.*;
import org.example.bearfitness.UI.ScreenManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;


class UserUI extends JPanel {
    private DBService dbService;
    private ScreenManager screenManager;
    private User user;
    //private JTextArea plansDisplay;

    public UserUI(DBService dbService, ScreenManager screenManager, User user) {
        this.dbService = dbService;
        this.screenManager = screenManager;
        this.user = user;

        setLayout(new BorderLayout());

        JButton viewWorkoutHistory = new JButton("View Workout History");
        viewWorkoutHistory.addActionListener(e -> screenManager.showScreen(ScreenManager.Screen.WORKOUT_HISTORY));


        JPanel topButtons = new JPanel(new GridLayout(1, 3, 20, 20));
        topButtons.add(new JButton("Add New Workout Entry"));
        topButtons.add(viewWorkoutHistory);
        topButtons.add(new JButton("Settings"));

        JPanel topWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topWrapper.add(topButtons);
        add(topWrapper, BorderLayout.NORTH);


//        goalsDisplay = new JTextArea(15, 30);
//        goalsDisplay.setEditable(false);
//        refreshPlansDisplay();
//
//        JPanel rightPanel = new JPanel(new BorderLayout());
//        rightPanel.setBorder(BorderFactory.createTitledBorder("Your Plans"));
//        rightPanel.add(new JScrollPane(plansDisplay), BorderLayout.CENTER);
//        add(rightPanel, BorderLayout.EAST);
    }


//    private void refreshPlansDisplay() {
//        if (plansDisplay == null) return;
//        try {
//            java.util.List<String> planNames = dbService.getAllPlans();
//            StringBuilder builder = new StringBuilder();
//            for (String name : planNames) {
//                builder.append("- ").append(name).append(" ");
//            }
//            plansDisplay.setText(builder.toString());
//        } catch (Exception ex) {
//            plansDisplay.setText("Failed to load plans.");
//        }
//    }

//  public static void main(String[] args) {
//    //SwingUtilities.invokeLater(() -> new TrainerUI().setVisible(false));
//    ConfigurableApplicationContext context = SpringApplication.run(BearFitnessApplication.class, args);
//    DBService dbService = context.getBean(DBService.class);
//
//
//    SwingUtilities.invokeLater(() ->  new TrainerUI(dbService).setVisible(true));
//  }
}
