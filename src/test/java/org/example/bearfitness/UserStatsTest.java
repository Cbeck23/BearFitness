package org.example.bearfitness;

import org.example.bearfitness.user.UserStats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UserStatsTest {

    private UserStats stats;
    private Date today;
    private LocalDate todayLocalDate;

    @BeforeEach
    void setUp() {
        stats = new UserStats();
        today = new Date();
        todayLocalDate = LocalDate.now();
    }

    @Test
    void setAndGetCaloriesLogged_shouldReturnCorrectMap() {
        Map<LocalDate, Integer> calories = Map.of(todayLocalDate, 2200);
        stats.setCaloriesLogged(calories);

        assertEquals(2200, stats.getCaloriesLogged().get(today));
    }

    @Test
    void logCalories_shouldAddOrUpdateCalories() {
        stats.logCalories(todayLocalDate, 500);
        stats.logCalories(todayLocalDate, 700);

        assertEquals(1200, stats.getCaloriesLogged().get(today));
    }

    @Test
    void setAndGetSleepLogged_shouldReturnCorrectMap() {
        Map<LocalDate, Double> sleep = Map.of(todayLocalDate, 8.0);
        stats.setSleepLogged(sleep);

        assertEquals(8, stats.getSleepLogged().get(todayLocalDate));
    }

    @Test
    void logSleep_shouldAddOrUpdateSleep() {
        stats.logSleep(todayLocalDate, 6.0);
        stats.logSleep(todayLocalDate, 3.0);

        assertEquals(9, stats.getSleepLogged().get(todayLocalDate));
    }

    @Test
    void setAndGetWeightLog_shouldReturnCorrectMap() {
        Map<LocalDate, Double> weight = Map.of(todayLocalDate, 150.5);
        stats.setWeightLog(weight);

        assertEquals(150.5, stats.getWeightLog().get(today));
    }

    @Test
    void logWeight_shouldAddOrUpdateWeight() {
        stats.logWeight(todayLocalDate, 140.0);
        stats.logWeight(todayLocalDate, 5.0);

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
        assertDoesNotThrow(() -> stats.logWeight(todayLocalDate, -5.0));
        assertNull(stats.getWeightLog().get(today));
    }
}
