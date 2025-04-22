package org.example.bearfitness.ui;

import org.example.bearfitness.fitness.FitnessLevel;
import org.example.bearfitness.fitness.ExercisePlan;
import org.example.bearfitness.data.DBService;
import org.example.bearfitness.fitness.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class CreateExerciseUI extends JPanel {
  private DBService dbService;
  private Map<Integer, WorkoutEntry> exercises = new HashMap<>();
  private JTextField planNameField, equipmentField, sessionLengthField, frequencyField;
  private JComboBox fitnessLevelCombo;
  private JTextArea planOutput;

  public CreateExerciseUI(DBService dbService) {
    this.dbService = dbService;
    setLayout(new BorderLayout());

    JPanel inputPanel = new JPanel(new GridLayout(6, 2));
    planNameField = new JTextField();
    equipmentField = new JTextField();
    fitnessLevelCombo = new JComboBox<>(FitnessLevel.values());
    sessionLengthField = new JTextField();
    frequencyField = new JTextField();
    JButton addExerciseButton = new JButton("Add Exercise to Plan");
    JButton savePlanButton = new JButton("Save Full Plan");

    inputPanel.add(new JLabel("Plan Name:")); inputPanel.add(planNameField);
    inputPanel.add(new JLabel("Equipment (comma-separated):")); inputPanel.add(equipmentField);
    inputPanel.add(new JLabel("Fitness Level:")); inputPanel.add(fitnessLevelCombo);
    inputPanel.add(new JLabel("Avg Session Length (min):")); inputPanel.add(sessionLengthField);
    inputPanel.add(new JLabel("Frequency (days/week):")); inputPanel.add(frequencyField);
    inputPanel.add(addExerciseButton); inputPanel.add(savePlanButton);
    add(inputPanel, BorderLayout.NORTH);

    planOutput = new JTextArea();
    planOutput.setEditable(false);
    add(new JScrollPane(planOutput), BorderLayout.CENTER);

    addExerciseButton.addActionListener(this::addExerciseDialog);
    savePlanButton.addActionListener(this::savePlan);
  }

  private void addExerciseDialog(ActionEvent e) {
    try {
      int day = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Day (1-7):"));
      int duration = Integer.parseInt(JOptionPane.showInputDialog(this, "Duration (minutes):"));
      JComboBox<WorkoutEntry.ExerciseType> typeCombo = new JComboBox<>(WorkoutEntry.ExerciseType.values());
      if (JOptionPane.showConfirmDialog(this, typeCombo, "Select Type", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
      String desc = JOptionPane.showInputDialog(this, "Enter Description:");
      WorkoutEntry entry = new WorkoutEntry(duration, (WorkoutEntry.ExerciseType) typeCombo.getSelectedItem(), 0, 0);
      entry.setDescription(desc);
      exercises.put(day, entry);
      displayCurrentExercises();
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(this, "Invalid input.");
    }
  }

  private void displayCurrentExercises() {
    StringBuilder builder = new StringBuilder("Current Exercises: ");
    for (int day : exercises.keySet()) {
      WorkoutEntry entry = exercises.get(day);
      builder.append("Day ").append(day).append(": ")
              .append(entry.getExerciseType()).append(" - ")
              .append(entry.getDuration()).append(" min - ")
              .append(entry.getDescription()).append(" ");
    }
    planOutput.setText(builder.toString());
  }

  private void savePlan(ActionEvent e) {
    try {
      ExercisePlan plan = new ExercisePlan(
              planNameField.getText(),
              Arrays.asList(equipmentField.getText().split(",")),
              ((FitnessLevel) fitnessLevelCombo.getSelectedItem()).getName(),
              Integer.parseInt(sessionLengthField.getText()),
              Integer.parseInt(frequencyField.getText()),
              exercises
      );
      dbService.createExercisePlan(plan);
      displayCurrentExercises();
      planOutput.append(" --- Plan Saved ---  " + plan.toString());
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(this, "Please check inputs.");
    }
  }
  
//  public static void main(String[] args) {
//    SwingUtilities.invokeLater(() -> new CreateExerciseUI().setVisible(true));
//  }
}
