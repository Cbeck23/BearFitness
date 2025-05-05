package org.example.bearfitness.user;

import jakarta.persistence.Embeddable;

/**
 * Stores and manages a user's fitness goals.
 */
@Embeddable
public class UserGoals {

    private Double goalWeight = 0.0;
    private Integer weeklyExercises = 0;
    private Integer goalCalories = 0;
    private Double goalSleep = 0.0;

    public UserGoals() {}

    public UserGoals(Double goalWeight, int weeklyExMinutes, Integer goalCalories, Double goalSleep) {
        this.goalWeight = goalWeight;
        this.weeklyExercises = weeklyExMinutes;
        this.goalCalories = goalCalories;
        this.goalSleep = goalSleep;
    }

    public Double getGoalWeight() { return goalWeight; }

    public void setGoalWeight(Double goalWeight) { this.goalWeight = goalWeight; }

    public Integer getWeeklyExercises() {
        if(weeklyExercises==null) {
            return 0;
        }
        return weeklyExercises;
    }

    public void setWeeklyExercises(Integer weeklyExercises) {
        this.weeklyExercises = weeklyExercises; }

    public Integer getGoalCalories() {return goalCalories;}

    public void setGoalCalories(Integer goalCalories) {this.goalCalories = goalCalories;}

    public Double getGoalSleep() {return goalSleep;}

    public void setGoalSleep(Double goalSleep) {this.goalSleep = goalSleep;}

}
