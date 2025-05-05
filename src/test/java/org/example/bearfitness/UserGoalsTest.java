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
        goals.setGoalWeight(150.0);
        assertEquals(150, goals.getGoalWeight());
    }

    @Test
    void setAndGetWeeklyExMinutes_shouldReturnCorrectValue() {
        goals.setWeeklyExercises(3);
        assertEquals(3, goals.getWeeklyExercises());
    }

    @Test
    void constructorWithParameters_shouldInitializeFieldsCorrectly() {
        UserGoals newGoals = new UserGoals(160, 300, 2000, 8.5);

        assertEquals(160, newGoals.getGoalWeight());
        assertEquals(300, newGoals.getWeeklyExercises());
        assertEquals(2000, newGoals.getGoalCalories());
        assertEquals(8.5, newGoals.getGoalSleep());
    }
}
