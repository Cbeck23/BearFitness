package org.example.users;

import java.util.list;

public class ExercisePlan {
  private String planName;
  private List<String> requiredEquipment;
  private String recommendedFitnessLevel;
  private int averageSessionLength; //in minutes
  private String suggestedFrequency; //e.g., "3 times a week", "Daily"

// Constructor
public ExercisePlan(String planName, List<String> requiredEquipment, String reccomendedFitnessLevel,
                    int averageSessionLength, String suggestedFrequency) {
  this.planName = planName;
  this.requiredEquipment = requiredEquipment;
  this.reccomendedFitnessLevel = reccomendedFitnessLevel;
  this.averageSessionLength = averageSessionLength;
  this.suggestedFrequency = suggestedFrequency;
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

public String getSuggestedFrequency() {
  return suggestedFrequency;
}

// ToString for easy display
@Override
  public String toString() {
    return "Exercise Plan: " + planName + "\n" +
      "Required Equipment: " + String.join(", ", requiredEquipment) + "\n" +
      "Recommended Fitness Level: " + recommendedFitnessLevel + "\n" +
      "Average Session Length: " + averageSessionLength + "minutes\n" +
      "Suggested Frequency: " + suggestedFrequency;
  }
}
      
