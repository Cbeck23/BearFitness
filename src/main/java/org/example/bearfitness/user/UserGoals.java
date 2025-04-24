package org.example.bearfitness.user;

import jakarta.persistence.Embeddable;

/**
 * Stores and manages a user's fitness goals.
 */
@Embeddable
public class UserGoals {

    private Integer goalWeight = 0;
    private Double weeklyExHours = 0.0;
    private Integer goalCalories = 0;
    private Double goalSleep = 0.0;

    public UserGoals() {}

    public UserGoals(Integer goalWeight, Double weeklyExHours, Integer goalCalories, Double goalSleep) {
        this.goalWeight = goalWeight;
        this.weeklyExHours = weeklyExHours;
        this.goalCalories = goalCalories;
        this.goalSleep = goalSleep;
    }

    public Integer getGoalWeight() { return goalWeight; }

    public void setGoalWeight(Integer goalWeight) { this.goalWeight = goalWeight; }

    public Double getWeeklyExHours() { return weeklyExHours; }

    public void setWeeklyExHours(Double weeklyExHours) { this.weeklyExHours = weeklyExHours; }

    public Integer getGoalCalories() {return goalCalories;}

    public void setGoalCalories(Integer goalCalories) {this.goalCalories = goalCalories;}

    public Double getGoalSleep() {return goalSleep;}

    public void setGoalSleep(Double goalSleep) {this.goalSleep = goalSleep;}

}
