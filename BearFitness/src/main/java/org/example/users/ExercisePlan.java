package org.example.users;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

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
    this.exercises = exercises != null ? exercises : new HashMap<>();
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
  public WorkoutEntry getSpecificExercise(int dayNumber) {
    return exercises.get(dayNumber);
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

  //Adds an exercise to the plan for a specific day (logic only, no UI)
  public boolean addExercise(int dayNumber, WorkoutEntry exercise) {
    if(dayNumber < 1 || dayNumber > 7) {
      return false;
    }
    if(exercises.containsKey(dayNumber)) {
      return false;
    }
    exercises.put(dayNumber, exercise);
    return true;
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
      
