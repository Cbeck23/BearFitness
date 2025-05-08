package org.example.bearfitness.ui;

import org.example.bearfitness.fitness.FitnessLevel;
import org.example.bearfitness.fitness.ExercisePlan;
import org.example.bearfitness.data.DBService;
import org.example.bearfitness.fitness.*;
import org.example.bearfitness.user.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

class CreateExerciseUI extends JPanel {
  private DBService dbService;
  private Map<Integer, WorkoutEntry> exercises = new HashMap<>();
  private JTextField planNameField, equipmentField, sessionLengthField, frequencyField;
  private JComboBox fitnessLevelCombo;
  private JTextArea planOutput;
  private ExercisePlan originalPlan = null;

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
    int day = -1;
    int duration = -1;

    // Ask for Day (1-7), retry until valid
    while (true) {
      String input = JOptionPane.showInputDialog(this, "Enter Day (1-7):");
      if (input == null) return; // User canceled
      try {
        day = Integer.parseInt(input);
        if (day < 1 || day > 7) throw new NumberFormatException();
        break;
      } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Please enter a valid day between 1 and 7.");
      }
    }

    // Ask for Duration, retry until valid
    while (true) {
      String input = JOptionPane.showInputDialog(this, "Duration (minutes):");
      if (input == null) return;
      try {
        duration = Integer.parseInt(input);
        if (duration <= 0) throw new NumberFormatException();
        break;
      } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Please enter a positive number for duration.");
      }
    }

    // Ask for Exercise Type
    JComboBox<WorkoutEntry.ExerciseType> typeCombo = new JComboBox<>(WorkoutEntry.ExerciseType.values());
    if (JOptionPane.showConfirmDialog(this, typeCombo, "Select Type", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;

    // Ask for Description
    String desc = JOptionPane.showInputDialog(this, "Enter Description:");
    if (desc == null) return;
    WorkoutEntry entry = new WorkoutEntry(duration, (WorkoutEntry.ExerciseType) typeCombo.getSelectedItem(), 0, 0);
    entry.setDescription(desc);
    exercises.put(day, entry);
    displayCurrentExercises();
//    } catch (Exception ex) {
//      JOptionPane.showMessageDialog(this, "Invalid input.");
//    }
  }

  private void displayCurrentExercises() {
    StringBuilder builder = new StringBuilder("Current Exercises: ");
    for (int day : exercises.keySet()) {
      WorkoutEntry entry = exercises.get(day);
      builder.append('\n').append("Day ").append(day).append(": ")
              .append(entry.getExerciseType()).append(" - ")
              .append(entry.getDuration()).append(" min - ")
              .append(entry.getDescription()).append(" ");
    }
    planOutput.setText(builder.toString());
  }

  private void savePlan(ActionEvent e) {
    if (originalPlan != null) {
      ExercisePlan updated = new ExercisePlan(
              originalPlan.getPlanName(),
              Arrays.asList(equipmentField.getText().split(",")),
              ((FitnessLevel) fitnessLevelCombo.getSelectedItem()).getName(),
              Integer.parseInt(sessionLengthField.getText()),
              Integer.parseInt(frequencyField.getText()),
              exercises
      );
      dbService.updateExercisePlan(updated);
      JOptionPane.showMessageDialog(this, "Plan updated!");
    } else {
      List<String> existingNames = dbService.getAllPlans();
      if (existingNames.contains(planNameField.getText())) {
        JOptionPane.showMessageDialog(this, "A plan with this name already exists. Choose a different name.");
        return;
      }
      ExercisePlan plan = new ExercisePlan(
              planNameField.getText(),
              Arrays.asList(equipmentField.getText().split(",")),
              ((FitnessLevel) fitnessLevelCombo.getSelectedItem()).getName(),
              Integer.parseInt(sessionLengthField.getText()),
              Integer.parseInt(frequencyField.getText()),
              exercises
      );
      dbService.createExercisePlan(plan);
      JOptionPane.showMessageDialog(this, "New plan saved!");
    }
    Window window = SwingUtilities.getWindowAncestor(this);
    if (window != null) {
      window.dispose();
    }
  }

  public CreateExerciseUI(DBService dbService, ExercisePlan existingPlan) {
    this(dbService);
    this.originalPlan = existingPlan;

    planNameField.setText(existingPlan.getPlanName());
    planNameField.setEditable(false);

    equipmentField.setText(String.join(",", existingPlan.getRequiredEquipment()));
    fitnessLevelCombo.setSelectedItem(FitnessLevel.fromName(existingPlan.getRecommendedFitnessLevel()));
    sessionLengthField.setText(String.valueOf(existingPlan.getAverageSessionLength()));
    frequencyField.setText(String.valueOf(existingPlan.getFrequencyPerWeek()));
    this.exercises = new HashMap<>(existingPlan.getExercises());

    displayCurrentExercises();
  }

}
