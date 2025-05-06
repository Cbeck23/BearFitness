package org.example.bearfitness.ui;

import org.example.bearfitness.data.DBService;
import org.example.bearfitness.fitness.ExercisePlan;
import org.example.bearfitness.fitness.UserWorkoutEntry;
import org.example.bearfitness.fitness.WorkoutEntry;
import org.example.bearfitness.user.User;
import org.jdatepicker.impl.*;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class ViewPlansUI extends JPanel {
    private DBService dbService;
    private ScreenManager screenManager;
    private User user;
    private JList<String> planList;
    private JPanel workoutDetailsPanel;

    public ViewPlansUI(DBService dbService, ScreenManager screenManager, User user) {
        this.dbService = dbService;
        this.screenManager = screenManager;
        this.user = dbService.findUserByUsername(user.getUsername()).orElse(user);

        setLayout(new BorderLayout());

        // Top back button
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> screenManager.showScreen(ScreenManager.Screen.USER_HOME, user));
        add(backButton, BorderLayout.NORTH);

        // Left: List of Plans
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (ExercisePlan plan : user.getSubscribedPlans()) {
            listModel.addElement(plan.getPlanName());
        }
        planList = new JList<>(listModel);
        planList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane listScrollPane = new JScrollPane(planList);
        listScrollPane.setPreferredSize(new Dimension(200, getHeight()));
        add(listScrollPane, BorderLayout.WEST);

        // Right: Workout Details
        workoutDetailsPanel = new JPanel();
        workoutDetailsPanel.setLayout(new BoxLayout(workoutDetailsPanel, BoxLayout.Y_AXIS));
        add(new JScrollPane(workoutDetailsPanel), BorderLayout.CENTER);

        planList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedPlanName = planList.getSelectedValue();
                displayWorkoutDetails(selectedPlanName);
            }
        });
    }

    private void displayWorkoutDetails(String planName) {
        workoutDetailsPanel.removeAll();

        ExercisePlan selectedPlan = user.getSubscribedPlans().stream()
                .filter(plan -> plan.getPlanName().equals(planName))
                .findFirst()
                .orElse(null);

        if (selectedPlan == null) {
            workoutDetailsPanel.add(new JLabel("No plan selected."));
            revalidate();
            repaint();
            return;
        }

        for (int day = 1; day <= 7; day++) {
            WorkoutEntry entry = selectedPlan.getSpecificExercise(day);
            JPanel dayPanel = new JPanel(new BorderLayout());
            dayPanel.setBorder(BorderFactory.createTitledBorder("Day " + day));

            if (entry == null) {
                dayPanel.add(new JLabel("Rest Day"), BorderLayout.CENTER);
            } else {
                JTextArea entryDetails = new JTextArea(entry.getExerciseType() + " - " + entry.getDuration() + " min\n" + entry.getDescription());
                entryDetails.setEditable(false);
                dayPanel.add(entryDetails, BorderLayout.CENTER);

                JButton addButton = new JButton("Add to My Workouts");
                addButton.addActionListener(e -> {
                    LocalDate date = selectDate();
                    if (date != null) {
                        WorkoutEntry newEntry = new WorkoutEntry();
                        newEntry.setExerciseType(entry.getExerciseType());
                        newEntry.setDuration(entry.getDuration());
                        newEntry.setDescription(entry.getDescription());
                        newEntry.setDate(date);
                        dbService.createUserWorkoutEntry(user, newEntry);
                        JOptionPane.showMessageDialog(this, "Workout added for " + date);
                    }
                });
                dayPanel.add(addButton, BorderLayout.SOUTH);
            }

            workoutDetailsPanel.add(dayPanel);
        }

        revalidate();
        repaint();
    }

    private LocalDate selectDate() {
        UtilDateModel dateModel = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");

        JDatePanelImpl datePanel = new JDatePanelImpl(dateModel, p);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateComponentFormatter());

        JPanel panel = new JPanel();
        panel.add(datePicker);

        int result = JOptionPane.showConfirmDialog(this, panel, "Select Date", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            Date selectedDate = (Date) datePicker.getModel().getValue();
            if (selectedDate != null) {
                return selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            }
        }
        return null;
    }
}
