package org.example.bearfitness;

import org.example.bearfitness.user.UserStats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UserStatsTest {

    private UserStats stats;
    private Date today;

    @BeforeEach
    void setUp() {
        stats = new UserStats();
        today = new Date();
    }

    @Test
    void setAndGetCaloriesLogged_shouldReturnCorrectMap() {
        Map<Date, Integer> calories = Map.of(today, 2200);
        stats.setCaloriesLogged(calories);

        assertEquals(2200, stats.getCaloriesLogged().get(today));
    }

    @Test
    void logCalories_shouldAddOrUpdateCalories() {
        stats.logCalories(today, 500);
        stats.logCalories(today, 700); // should merge and sum

        assertEquals(1200, stats.getCaloriesLogged().get(today));
    }

    @Test
    void setAndGetSleepLogged_shouldReturnCorrectMap() {
        Map<Date, Integer> sleep = Map.of(today, 8);
        stats.setSleepLogged(sleep);

        assertEquals(8, stats.getSleepLogged().get(today));
    }

    @Test
    void logSleep_shouldAddOrUpdateSleep() {
        stats.logSleep(today, 6);
        stats.logSleep(today, 3); // should merge and sum

        assertEquals(9, stats.getSleepLogged().get(today));
    }

    @Test
    void setAndGetWeightLog_shouldReturnCorrectMap() {
        Map<Date, Double> weight = Map.of(today, 150.5);
        stats.setWeightLog(weight);

        assertEquals(150.5, stats.getWeightLog().get(today));
    }

    @Test
    void logWeight_shouldAddOrUpdateWeight() {
        stats.logWeight(today, 140.0);
        stats.logWeight(today, 5.0); // should merge and sum

        assertEquals(145.0, stats.getWeightLog().get(today));
    }

    @Test
    void logCalories_nullValues_shouldNotThrowError() {
        assertDoesNotThrow(() -> stats.logCalories(null, null));
    }

    @Test
    void logSleep_nullValues_shouldNotThrowError() {
        assertDoesNotThrow(() -> stats.logSleep(null, null));
    }

    @Test
    void logWeight_nullOrNegativeValues_shouldNotThrowError() {
        assertDoesNotThrow(() -> stats.logWeight(null, null));
        assertDoesNotThrow(() -> stats.logWeight(today, -5.0)); // Negative weight should not add
        assertNull(stats.getWeightLog().get(today)); // Should not create an entry
    }
}
