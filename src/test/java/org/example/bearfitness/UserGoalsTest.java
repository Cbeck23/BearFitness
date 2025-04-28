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
    void setAndGetWeeklyExMinutes_shouldReturnCorrectValue() {
        goals.setWeeklyExMinutes(300);
        assertEquals(300, goals.getWeeklyExMinutes());
    }

    @Test
    void constructorWithParameters_shouldInitializeFieldsCorrectly() {
        UserGoals newGoals = new UserGoals(160, 300, 2000, 8.5);

        assertEquals(160, newGoals.getGoalWeight());
        assertEquals(300, newGoals.getWeeklyExMinutes());
        assertEquals(2000, newGoals.getGoalCalories());
        assertEquals(8.5, newGoals.getGoalSleep());
    }
}
