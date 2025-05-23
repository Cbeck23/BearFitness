package org.example.bearfitness;

import org.example.bearfitness.fitness.ExercisePlan;
import org.example.bearfitness.fitness.WorkoutEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ExercisePlanTest {

    private ExercisePlan plan;

    @BeforeEach
    void setUp() {
        plan = new ExercisePlan();
    }

    @Test
    void setAndGetPlanName_shouldReturnCorrectName() {
        plan.setPlanName("Strength Training");
        assertEquals("Strength Training", plan.getPlanName());
    }

    @Test
    void setAndGetRequiredEquipment_shouldReturnCorrectList() {
        List<String> equipment = Arrays.asList("Dumbbells", "Bench");
        plan.setRequiredEquipment(equipment);
        assertEquals(equipment, plan.getRequiredEquipment());
    }

    @Test
    void setAndGetRecommendedFitnessLevel_shouldReturnCorrectLevel() {
        plan.setRecommendedFitnessLevel("Intermediate");
        assertEquals("Intermediate", plan.getRecommendedFitnessLevel());
    }

    @Test
    void setAndGetAverageSessionLength_shouldReturnCorrectLength() {
        plan.setAverageSessionLength(45);
        assertEquals(45, plan.getAverageSessionLength());
    }

    @Test
    void setAndGetFrequencyPerWeek_shouldReturnCorrectFrequency() {
        plan.setFrequencyPerWeek(3);
        assertEquals(3, plan.getFrequencyPerWeek());
    }

    @Test
    void addExercise_validDayNumber_shouldReturnTrue() {
        WorkoutEntry entry = new WorkoutEntry();
        boolean added = plan.addExercise(3, entry);
        assertTrue(added);
        assertEquals(entry, plan.getSpecificExercise(3));
    }

    @Test
    void addExercise_invalidDayNumber_shouldReturnFalse() {
        WorkoutEntry entry = new WorkoutEntry();
        boolean addedLow = plan.addExercise(0, entry);  // Invalid day 0
        boolean addedHigh = plan.addExercise(8, entry); // Invalid day 8

        assertFalse(addedLow);
        assertFalse(addedHigh);
    }

    @Test
    void addExercise_dayAlreadyExists_shouldReturnFalse() {
        WorkoutEntry entry1 = new WorkoutEntry();
        WorkoutEntry entry2 = new WorkoutEntry();
        plan.addExercise(4, entry1);

        boolean addedAgain = plan.addExercise(4, entry2);

        assertFalse(addedAgain);
        assertEquals(entry1, plan.getSpecificExercise(4)); // Should still be the original
    }

    @Test
    void getSpecificExercise_validDay_shouldReturnCorrectExercise() {
        WorkoutEntry entry = new WorkoutEntry();
        plan.addExercise(5, entry);

        WorkoutEntry result = plan.getSpecificExercise(5);

        assertEquals(entry, result);
    }

    @Test
    void toString_shouldContainPlanDetails() {
        plan.setPlanName("Plan A");
        plan.setRecommendedFitnessLevel("Beginner");
        plan.setAverageSessionLength(30);
        plan.setFrequencyPerWeek(4);

        String result = plan.toString();

        assertTrue(result.contains("Plan Name: Plan A"));
        assertTrue(result.contains("Recommended Fitness Level: Beginner"));
        assertTrue(result.contains("30 minutes"));
        assertTrue(result.contains("4 days"));
    }
}
