package org.example.users;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

public class ExercisePlan {
  private String planName;
  private List<String> requiredEquipment;
  private String recommendedFitnessLevel;
  private int averageSessionLength; //in minutes
  private int frequencyPerWeek; //e.g., X times per week
  private Map<Integer, WorkoutEntry> exercises;


  // Constructor
  public ExercisePlan(String planName, List<String> requiredEquipment, String reccomendedFitnessLevel,
                      int averageSessionLength, int frequencyPerWeek, Map<Integer, WorkoutEntry> exercises) {
    this.planName = planName;
    this.requiredEquipment = requiredEquipment;
    this.recommendedFitnessLevel = reccomendedFitnessLevel;
    this.averageSessionLength = averageSessionLength;
    this.frequencyPerWeek = frequencyPerWeek;
    this.exercises = exercises;
  }

  // Getters
  public String getPlanName() {
    return planName;
  }
  public List<String> getRequiredEquipment() {
    return requiredEquipment;
  }
  public String getRecommendedFitnessLevel() {
    return recommendedFitnessLevel;
  }
  public int getAverageSessionLength() {
    return averageSessionLength;
  }
  public int getFrequencyPerWeek() {
    return frequencyPerWeek;
  }
  public Map<Integer, WorkoutEntry> getExercises() {
    return exercises;
  }
  public WorkoutEntry getSpecificExercise(int exerciseId) {
    return exercises.get(exerciseId);
  }

  public void setPlanName(String planName) {
    this.planName = planName;}
  public void setRequiredEquipment(List<String> requiredEquipment) {
    this.requiredEquipment = requiredEquipment;}
  public void setRecommendedFitnessLevel(String recommendedFitnessLevel) {
    this.recommendedFitnessLevel = recommendedFitnessLevel;}
  public void setAverageSessionLength(int averageSessionLength) {
    this.averageSessionLength = averageSessionLength;}
  public void setFrequencyPerWeek(int frequencyPerWeek) {
    this.frequencyPerWeek = frequencyPerWeek;}


  //pass in frequencyPerWeek to set all exercises for a week
  //enter and create an individual WorkoutEntry for however many there are per week
  public void setExercises (int numberOfExercises){

    if(numberOfExercises <= 0){
      //error pop up and tell them to try again
      JOptionPane.showMessageDialog(null, "Invalid number of exercises! Must be at least 1.", "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }

    for(int i=0; i<numberOfExercises; i++){
      WorkoutEntry newExercise = new WorkoutEntry();

      try {
        //Adding a new exercise to the plan requires inputs from UI text boxes:

        //Ask for what day of the week the workout is planned to be on, and cannot be the same as a
          //day that's already on the plan, eg cannot have two exercises both on Monday (day = 2)
        int dayNumber = Integer.parseInt(dayField.getText());

        if (dayNumber < 1 || dayNumber > 7) {
          JOptionPane.showMessageDialog(null, "Invalid day! Enter a number between 1 and 7.", "Error", JOptionPane.ERROR_MESSAGE);
          return;
        }
        if (exercises.containsKey(dayNumber)) {
          JOptionPane.showMessageDialog(null, "This day already has an exercise! Choose another day.", "Error", JOptionPane.ERROR_MESSAGE);
          return;
        }

        try {
          //Get exercise duration from what user enters in the Duration text box
          int duration = Integer.parseInt(durationField.getText());
          if (duration <= 0) {
            JOptionPane.showMessageDialog(null, "Duration must be greater than 0!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
          }
          newExercise.setDuration(duration);
        } catch (NumberFormatException e) {
          JOptionPane.showMessageDialog(null, "Invalid duration! Enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
          return;
        }

        //I think it may be best to use a drop-down selector box to select the exercise type; here is an
        //example of using a JComboBox, please change or integrate as needed

        // Create JComboBox with ExerciseType values
        JComboBox<WorkoutEntry.ExerciseType> exerciseTypeComboBox = new JComboBox<>(WorkoutEntry.ExerciseType.values());

        //set ExerciseType to what is selected
        exerciseTypeComboBox.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            newExercise.setExerciseType((WorkoutEntry.ExerciseType) exerciseTypeComboBox.getSelectedItem());
          }
        });
        //pop-up dialog to select exercise type
        JOptionPane.showMessageDialog(null, exerciseTypeComboBox, "Select Exercise Type", JOptionPane.QUESTION_MESSAGE);
        newExercise.setExerciseType((WorkoutEntry.ExerciseType) exerciseTypeComboBox.getSelectedItem());

        //get exercise description from what is entered in the Description text box
        newExercise.setDescription(descriptionField.getText());

        //put new exercise on plan
        exercises.put(dayNumber, newExercise);

      } catch (NumberFormatException e) {
        //error if invalid input for day
        JOptionPane.showMessageDialog(null, "Invalid day input! Please enter a number between 1 and 7.", "Error", JOptionPane.ERROR_MESSAGE);
      }

    }
  }

  @Override
  public String toString() {
    return "ExercisePlan{" +
            "planName='" + planName + '\'' +
            ", requiredEquipment=" + requiredEquipment +
            ", recommendedFitnessLevel='" + recommendedFitnessLevel + '\'' +
            ", averageSessionLength=" + averageSessionLength +
            ", frequencyPerWeek=" + frequencyPerWeek +
            ", exercises=" + exercises +
            '}';
  }
}
      
