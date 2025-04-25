package org.example.bearfitness.user;

import jakarta.persistence.Embeddable;

/**
 * Stores and manages a user's fitness goals.
 */
@Embeddable
public class UserGoals {

    private Integer goalWeight = 0;
    private Integer weeklyExMinutes = 0;
    private Integer goalCalories = 0;
    private Double goalSleep = 0.0;

    public UserGoals() {}

    public UserGoals(Integer goalWeight, int weeklyExMinutes, Integer goalCalories, Double goalSleep) {
        this.goalWeight = goalWeight;
        this.weeklyExMinutes = weeklyExMinutes;
        this.goalCalories = goalCalories;
        this.goalSleep = goalSleep;
    }

    public Integer getGoalWeight() { return goalWeight; }

    public void setGoalWeight(Integer goalWeight) { this.goalWeight = goalWeight; }

    public Integer getWeeklyExMinutes() {
        if(weeklyExMinutes==null) {
            return 0;
        }
        return weeklyExMinutes;
    }

    public void setWeeklyExMinutes(Integer weeklyExMinutes) {
        this.weeklyExMinutes = weeklyExMinutes; }

    public Integer getGoalCalories() {return goalCalories;}

    public void setGoalCalories(Integer goalCalories) {this.goalCalories = goalCalories;}

    public Double getGoalSleep() {return goalSleep;}

    public void setGoalSleep(Double goalSleep) {this.goalSleep = goalSleep;}

}
