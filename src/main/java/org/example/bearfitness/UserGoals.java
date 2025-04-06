package org.example.bearfitness;

import jakarta.persistence.Embeddable;

/**
 * Stores and manages a user's fitness goals.
 */
@Embeddable
public class UserGoals {

    private Integer goalWeight = null;
    private Integer weeklyActivities = null;

    public UserGoals() {}

    public Integer getGoalWeight() { return goalWeight; }

    public void setGoalWeight(Integer goalWeight) { this.goalWeight = goalWeight; }

    public Integer getWeeklyActivities() { return weeklyActivities; }

    public void setWeeklyActivities(Integer weeklyActivities) { this.weeklyActivities = weeklyActivities; }
}
