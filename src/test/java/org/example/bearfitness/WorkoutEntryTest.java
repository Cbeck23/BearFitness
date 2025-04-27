package org.example.bearfitness;

import org.example.bearfitness.fitness.WorkoutEntry;
import org.example.bearfitness.fitness.WorkoutEntry.ExerciseType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class WorkoutEntryTest {

    private WorkoutEntry entry;

    @BeforeEach
    void setUp() {
        entry = new WorkoutEntry();
    }

    @Test
    void setAndGetDuration_shouldReturnCorrectValue() {
        entry.setDuration(45);
        assertEquals(45, entry.getDuration());
    }

    @Test
    void setAndGetExerciseType_shouldReturnCorrectValue() {
        entry.setExerciseType(ExerciseType.RUN);
        assertEquals(ExerciseType.RUN, entry.getExerciseType());
    }

    @Test
    void getExerciseTypeValue_shouldReturnOrdinalAsString() {
        entry.setExerciseType(ExerciseType.BIKE);
        assertEquals(String.valueOf(ExerciseType.BIKE.ordinal()), entry.getExerciseTypeValue());
    }

    @Test
    void setAndGetDescription_shouldReturnCorrectValue() {
        entry.setDescription("Morning Run");
        assertEquals("Morning Run", entry.getDescription());
    }

    @Test
    void setAndGetDate_shouldReturnCorrectValue() {
        LocalDate today = LocalDate.now();
        entry.setDate(today);
        assertEquals(today, entry.getDate());
    }

    @Test
    void equals_sameValues_shouldReturnTrue() {
        WorkoutEntry entry1 = new WorkoutEntry();
        WorkoutEntry entry2 = new WorkoutEntry();

        entry1.setDuration(30);
        entry1.setExerciseType(ExerciseType.YOGA);

        entry2.setDuration(30);
        entry2.setExerciseType(ExerciseType.YOGA);

        assertEquals(entry1, entry2);
    }
}