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
