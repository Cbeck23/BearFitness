package org.example.bearfitness;

import org.example.bearfitness.user.UserGoals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserGoalsTest {

    private UserGoals goals;

    @BeforeEach
    void setUp() {
        goals = new UserGoals();
    }

    @Test
    void setAndGetGoalWeight_shouldReturnCorrectValue() {
        goals.setGoalWeight(150);
        assertEquals(150, goals.getGoalWeight());
    }

    @Test
    void setAndGetWeeklyActivities_shouldReturnCorrectValue() {
        goals.setWeeklyActivities(5);
        assertEquals(5, goals.getWeeklyActivities());
    }

    @Test
    void constructorWithParameters_shouldInitializeFieldsCorrectly() {
        UserGoals newGoals = new UserGoals(160, 4);

        assertEquals(160, newGoals.getGoalWeight());
        assertEquals(4, newGoals.getWeeklyActivities());
    }
}
