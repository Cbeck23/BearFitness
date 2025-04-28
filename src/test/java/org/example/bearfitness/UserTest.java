package org.example.bearfitness;

import org.example.bearfitness.fitness.ExercisePlan;
import org.example.bearfitness.user.User;
import org.example.bearfitness.user.UserGoals;
import org.example.bearfitness.user.UserStats;
import org.example.bearfitness.user.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("testUser", "password123", "test@example.com", UserType.BASIC);
    }

    @Test
    void constructor_shouldInitializeFieldsCorrectly() {
        assertEquals("testUser", user.getUsername());
        assertEquals("password123", user.getPassword());
        assertEquals("test@example.com", user.getEmail());
        assertEquals(UserType.BASIC, user.getUserType());
        assertNotNull(user.getUserStats());
        assertNotNull(user.getGoals());
        assertNotNull(user.getSubscribedPlans());
    }

    @Test
    void setUsername_shouldUpdateUsername() {
        user.setUsername("newUsername");
        assertEquals("newUsername", user.getUsername());
    }

    @Test
    void setPassword_shouldUpdatePassword() {
        user.setPassword("newPassword");
        assertEquals("newPassword", user.getPassword());
    }

    @Test
    void setEmail_shouldUpdateEmail() {
        user.setEmail("new@example.com");
        assertEquals("new@example.com", user.getEmail());
    }

    @Test
    void setUserType_shouldUpdateUserType() {
        user.setUserType(UserType.ADMIN);
        assertEquals(UserType.ADMIN, user.getUserType());
    }

    @Test
    void setUserStats_shouldUpdateUserStats() {
        UserStats newStats = new UserStats();
        user.setUserStats(newStats);
        assertEquals(newStats, user.getUserStats());
    }

    @Test
    void setGoals_shouldUpdateGoals() {
        UserGoals newGoals = new UserGoals();
        user.setGoals(newGoals);
        assertEquals(newGoals, user.getGoals());
    }

    @Test
    void addPlan_shouldAddExercisePlan() {
        ExercisePlan plan = new ExercisePlan();
        user.addPlan(plan);
        assertTrue(user.getSubscribedPlans().contains(plan));
    }

    @Test
    void setGoalWeight_shouldSetGoalWeightInGoals() {
        user.setGoalWeight(150);
        assertEquals(150, user.getGoals().getGoalWeight());
    }

    @Test
    void setWeeklyActivities_shouldSetWeeklyActivitiesInGoals() {
        user.setWeeklyActivities(5);
        assertEquals(5, user.getGoals().getWeeklyActivities());
    }

    @Test
    void logCalories_shouldRecordCaloriesInStats() {
        Date today = new Date();
        user.logCalories(today, 2000);
        Map<Date, Integer> caloriesLogged = user.getCaloriesLogged();

        assertEquals(2000, caloriesLogged.get(today));
    }

    @Test
    void logSleep_shouldRecordSleepInStats() {
        Date today = new Date();
        user.logSleep(today, 8);
        Map<Date, Integer> sleepLogged = user.getSleepLogged();

        assertEquals(8, sleepLogged.get(today));
    }

    @Test
    void recordWeight_shouldRecordWeightInStats() {
        Date today = new Date();
        user.recordWeight(today, 150.5);
        Map<Date, Double> weightLogged = user.getWeightLog();

        assertEquals(150.5, weightLogged.get(today));
    }
}
