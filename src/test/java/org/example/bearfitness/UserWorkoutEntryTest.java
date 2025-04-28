package org.example.bearfitness;

import org.example.bearfitness.fitness.UserWorkoutEntry;
import org.example.bearfitness.fitness.WorkoutEntry;
import org.example.bearfitness.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserWorkoutEntryTest {

    private UserWorkoutEntry userWorkoutEntry;
    private User user;
    private WorkoutEntry workoutEntry;

    @BeforeEach
    void setUp() {
        user = new User();
        workoutEntry = new WorkoutEntry();
        userWorkoutEntry = new UserWorkoutEntry(user, workoutEntry);
    }

    @Test
    void constructor_shouldInitializeFieldsCorrectly() {
        assertEquals(user, userWorkoutEntry.getUser());
        assertEquals(workoutEntry, userWorkoutEntry.getWorkoutEntry());
    }

    @Test
    void setUser_shouldUpdateUser() {
        User newUser = new User();
        userWorkoutEntry.setUser(newUser);

        assertEquals(newUser, userWorkoutEntry.getUser());
    }

    @Test
    void setWorkoutEntry_shouldUpdateWorkoutEntry() {
        WorkoutEntry newEntry = new WorkoutEntry();
        userWorkoutEntry.setWorkoutEntry(newEntry);

        assertEquals(newEntry, userWorkoutEntry.getWorkoutEntry());
    }

    @Test
    void setAndGetId_shouldReturnCorrectId() {
        userWorkoutEntry.setId(123L);

        assertEquals(123L, userWorkoutEntry.getId());
    }
}
