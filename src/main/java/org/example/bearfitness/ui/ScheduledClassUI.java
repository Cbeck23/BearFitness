package org.example.bearfitness.ui;

import org.example.bearfitness.data.DBService;
import org.example.bearfitness.fitness.ExercisePlan;
import org.example.bearfitness.fitness.FitnessLevel;
import org.example.bearfitness.fitness.WorkoutEntry;
import org.example.bearfitness.user.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.*;
import java.util.List;


public class ScheduledClassUI extends JPanel {

    private DBService dbService;
    private Map<Integer, WorkoutEntry> exercises = new HashMap<>();
    private JTextField planNameField, sessionLengthField;
    private MultiDatePicker datesButton;
    private JComboBox<FitnessLevel> fitnessLevelCombo;
    private JTextArea planOutput;

    public ScheduledClassUI(DBService dbService, User user) {
        this.dbService = dbService;
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(6, 2));
        planNameField = new JTextField();
        fitnessLevelCombo = new JComboBox<>(FitnessLevel.values());
        sessionLengthField = new JTextField();
        datesButton = new MultiDatePicker();
        JButton savePlanButton = new JButton("Save Class Plan");

        inputPanel.add(new JLabel("Class Name:")); inputPanel.add(planNameField);
        inputPanel.add(new JLabel("Fitness Level:")); inputPanel.add(fitnessLevelCombo);
        inputPanel.add(new JLabel("Session Length (min):")); inputPanel.add(sessionLengthField);
        inputPanel.add(new JLabel("Class Dates:")); inputPanel.add(datesButton);
        inputPanel.add(savePlanButton);
        add(inputPanel, BorderLayout.NORTH);

        planOutput = new JTextArea();
        planOutput.setEditable(false);
        add(new JScrollPane(planOutput), BorderLayout.CENTER);

        savePlanButton.addActionListener(e -> savePlan(e, user));
    }

    private void savePlan(ActionEvent e, User user) {
        try {
            List<LocalDate> selectedDates = datesButton.getSelectedDates();

            List<String> dateStrings = new ArrayList<>();
            for (LocalDate selectedDate : selectedDates) {
                dateStrings.add(selectedDate.toString());
            }

            ExercisePlan plan = new ExercisePlan(
                    planNameField.getText(),
                    dateStrings,
                    ((FitnessLevel) fitnessLevelCombo.getSelectedItem()).getName(),
                    Integer.parseInt(sessionLengthField.getText()),
                    selectedDates.size(),
                    exercises
            );
            dbService.createClassEntry(user, plan, selectedDates);

            StringBuilder dateStr = new StringBuilder();
            for (LocalDate d : selectedDates) {
                dateStr.append("\n  - ").append(d);
            }

            planOutput.append("\n--- Class Saved ---\n" + plan + "\nDates:" + dateStr);

            Window window = SwingUtilities.getWindowAncestor(this);
            if (window != null) {
                window.dispose();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Please check inputs.");
        }
    }

}
