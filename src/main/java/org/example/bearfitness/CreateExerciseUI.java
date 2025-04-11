package org.example.bearfitness;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateExerciseUI extends JFrame {
  private JTextField planNameField;
  private JTextField equipmentField;
  private JComboBox fitnessLevelCombo;
  private JTextField sessionLengthField;
  private JTextField frequencyField;
  private JButton addExerciseButton;
  private JButton savePlanButton;
  private JTextArea planOutput;

  private static DBService dbService;

  private Map<Integer, WorkoutEntry> exercises = new HashMap<>();

  public CreateExerciseUI(DBService dbService) {
    this.dbService = dbService;
    setTitle("Create Exercise");
    setSize(600, 500);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLayout(new BorderLayout());

    //Top panel for basic plan info
    JPanel inputPanel = new JPanel(new GridLayout(6, 2));

    inputPanel.add(new JLabel("Plan Name:"));
    planNameField = new JTextField();
    inputPanel.add(planNameField);

    inputPanel.add(new JLabel("Required Equipment (comma-separated):"));
    equipmentField = new JTextField();
    inputPanel.add(equipmentField);

    inputPanel.add(new JLabel("Recommended Fitness Level:"));
    fitnessLevelCombo = new JComboBox<>(FitnessLevel.values());
    inputPanel.add(fitnessLevelCombo);

    inputPanel.add(new JLabel("Avg Session Length (min):"));
    sessionLengthField = new JTextField();
    inputPanel.add(sessionLengthField);

    inputPanel.add(new JLabel("Frequency (days/week):"));
    frequencyField = new JTextField();
    inputPanel.add(frequencyField);

    addExerciseButton = new JButton("Add Exercise to Plan");
    inputPanel.add(addExerciseButton);

    savePlanButton = new JButton("Save Full Plan");
    inputPanel.add(savePlanButton);

    add(inputPanel, BorderLayout.NORTH);

    //Output Area
    planOutput = new JTextArea();
    planOutput.setEditable(false);
    add(new JScrollPane(planOutput), BorderLayout.CENTER);

    //Button Actions
    addExerciseButton.addActionListener(this::addExerciseDialog);
    savePlanButton.addActionListener(this::savePlan);
  }

  private void addExerciseDialog(ActionEvent e) {
    try {
      int day = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Day (1-7):"));
      if(day < 1 || day > 7) {
        JOptionPane.showMessageDialog(this, "Day must be between 1 and 7.");
        return;
      }
      if(exercises.containsKey(day)) {
        JOptionPane.showMessageDialog(this, "That day already has an exercise.");
        return;
      }

      int duration = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Duration (minutes):"));
      if(duration <= 0) {
        JOptionPane.showMessageDialog(this, "Duration must be positive.");
        return;
      }

      JComboBox<WorkoutEntry.ExerciseType> exerciseTypeCombo = new JComboBox<>(WorkoutEntry.ExerciseType.values());
      int result = JOptionPane.showConfirmDialog(this, exerciseTypeCombo, "Select Exercise Type", JOptionPane.OK_CANCEL_OPTION);
      if(result != JOptionPane.OK_OPTION) return;

      String description = JOptionPane.showInputDialog(this, "Enter Description:");

      WorkoutEntry entry = new WorkoutEntry();
      entry.setDuration(duration);
      entry.setExerciseType((WorkoutEntry.ExerciseType) exerciseTypeCombo.getSelectedItem());
      entry.setDescription(description);

      exercises.put(day, entry);
      JOptionPane.showMessageDialog(this, "Exercise added for day " + day);
      displayCurrentExercises();

    } catch (NumberFormatException ex) {
      JOptionPane.showMessageDialog(this, "Invalid input format.");
      }
  }

  private void displayCurrentExercises() {
    StringBuilder builder = new StringBuilder("Current Exercises:\n");
    exercises.keySet().stream()
            .sorted()
            .forEach(day -> {
              WorkoutEntry entry = exercises.get(day);
              builder.append("Day ").append(day).append(": ")
                      .append(entry.getExerciseType()).append(" - ")
                      .append(entry.getDuration()).append(" min - ")
                      .append(entry.getDescription()).append("\n");
            });

    planOutput.setText(builder.toString());
  }

  private void savePlan(ActionEvent e) {
    try {
      String name = planNameField.getText();
      FitnessLevel fitness = (FitnessLevel) fitnessLevelCombo.getSelectedItem();
      int sessionLength = Integer.parseInt(sessionLengthField.getText());
      int frequency = Integer.parseInt(frequencyField.getText());
      List<String> equipment = Arrays.asList(equipmentField.getText().split("\\s*,\\s*"));

      ExercisePlan plan = new ExercisePlan(name, equipment, fitness.getName(), sessionLength, frequency, exercises);
      //planOutput.setText(plan.toString());
      dbService.createExercisePlan(plan);

      StringBuilder builder = new StringBuilder();

      // Show current exercises (sorted)
      exercises.keySet().stream()
              .sorted()
              .forEach(day -> {
                WorkoutEntry entry = exercises.get(day);
                builder.append("Day ").append(day).append(": ")
                        .append(entry.getExerciseType()).append(" - ")
                        .append(entry.getDuration()).append(" min - ")
                        .append(entry.getDescription()).append("\n");
              });

      // Add plan summary at the end
      builder.append("\n--- Full Plan Summary ---\n")
              .append(plan.toString());

      planOutput.setText(builder.toString());

    } catch(NumberFormatException ex) {
      JOptionPane.showMessageDialog(this, "Please enter valid numbers for session length and frequency.");
    }
  }
  
//  public static void main(String[] args) {
//    SwingUtilities.invokeLater(() -> new CreateExerciseUI().setVisible(true));
//  }
}
