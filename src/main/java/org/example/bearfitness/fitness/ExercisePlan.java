package org.example.bearfitness.fitness;

import jakarta.persistence.*;
import java.util.*;

@Entity
public class ExercisePlan {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String planName;

  @ElementCollection
  private List<String> requiredEquipment;

  private String recommendedFitnessLevel;
  private int averageSessionLength;
  private int frequencyPerWeek;

  @ElementCollection
  @CollectionTable(name = "exercise_plan_exercises", joinColumns = @JoinColumn(name = "plan_id"))
  @MapKeyColumn(name = "day_number")
  private Map<Integer, WorkoutEntry> exercises = new HashMap<>();

  public ExercisePlan() {}

  public ExercisePlan(String planName, List<String> requiredEquipment, String recommendedFitnessLevel,
                      int averageSessionLength, int frequencyPerWeek, Map<Integer, WorkoutEntry> exercises) {
    this.planName = planName;
    this.requiredEquipment = requiredEquipment;
    this.recommendedFitnessLevel = recommendedFitnessLevel;
    this.averageSessionLength = averageSessionLength;
    this.frequencyPerWeek = frequencyPerWeek;
    this.exercises = exercises != null ? exercises : new HashMap<>();
  }

  public ExercisePlan(ExercisePlan exercisePlan) {
    this.planName = exercisePlan.getPlanName();
    this.requiredEquipment = exercisePlan.getRequiredEquipment();
    this.recommendedFitnessLevel = exercisePlan.getRecommendedFitnessLevel();
    this.averageSessionLength = exercisePlan.getAverageSessionLength();
    this.frequencyPerWeek = exercisePlan.getFrequencyPerWeek();
    this.exercises = exercisePlan.getExercises();
  }

  public Long getId() { return id; }

  public String getPlanName() { return planName; }
  public void setPlanName(String planName) { this.planName = planName; }

  public List<String> getRequiredEquipment() { return requiredEquipment; }
  public void setRequiredEquipment(List<String> requiredEquipment) { this.requiredEquipment = requiredEquipment; }

  public String getRecommendedFitnessLevel() { return recommendedFitnessLevel; }
  public void setRecommendedFitnessLevel(String recommendedFitnessLevel) { this.recommendedFitnessLevel = recommendedFitnessLevel; }

  public int getAverageSessionLength() { return averageSessionLength; }
  public void setAverageSessionLength(int averageSessionLength) { this.averageSessionLength = averageSessionLength; }

  public int getFrequencyPerWeek() { return frequencyPerWeek; }
  public void setFrequencyPerWeek(int frequencyPerWeek) { this.frequencyPerWeek = frequencyPerWeek; }

  public Map<Integer, WorkoutEntry> getExercises() { return exercises; }
  public void setExercises(Map<Integer, WorkoutEntry> exercises) { this.exercises = exercises; }

  public boolean addExercise(int dayNumber, WorkoutEntry exercise) {
    if (dayNumber < 1 || dayNumber > 7 || exercises.containsKey(dayNumber)) {
      return false;
    }
    exercises.put(dayNumber, exercise);
    return true;
  }

  public WorkoutEntry getSpecificExercise(int dayNumber) {
    return exercises.get(dayNumber);
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("Plan Name: ").append(planName).append("\n")
            .append("Required Equipment: ").append(requiredEquipment).append("\n")
            .append("Recommended Fitness Level: ").append(recommendedFitnessLevel).append("\n")
            .append("Average Session Length: ").append(averageSessionLength).append(" minutes\n")
            .append("Frequency per Week: ").append(frequencyPerWeek).append(" days\n")
            .append("Exercises:\n");

    exercises.keySet().stream()
            .sorted()
            .forEach(day -> {
              WorkoutEntry entry = exercises.get(day);
              builder.append("  Day ").append(day).append(": ")
                      .append(entry.getExerciseType()).append(" - ")
                      .append(entry.getDuration()).append(" min - ")
                      .append(entry.getDescription()).append("\n");
            });

    return builder.toString();
  }
}
