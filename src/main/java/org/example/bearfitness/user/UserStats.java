package org.example.bearfitness.user;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Stores and manages a user's activity statistics.
 */
@Embeddable
public class UserStats {

    @ElementCollection
    private Map<Date, Integer> caloriesLogged = new HashMap<>();

    @ElementCollection
    private Map<Date, Integer> sleepLogged = new HashMap<>();

    @ElementCollection
    private Map<Date, Double> weightLog = new HashMap<>();

    public UserStats() {}

    public Map<Date, Integer> getCaloriesLogged() { return caloriesLogged; }

    public void setCaloriesLogged(Map<Date, Integer> caloriesLogged) { this.caloriesLogged = caloriesLogged; }

    public void logCalories(Date date, Integer calories) {
        if (date != null && calories != null) {
            caloriesLogged.merge(date, calories, Integer::sum);
        }
    }

    public Map<Date, Integer> getSleepLogged() { return sleepLogged; }

    public void setSleepLogged(Map<Date, Integer> sleepLogged) { this.sleepLogged = sleepLogged; }

    public void logSleep(Date date, Integer sleep) {
        if (date != null && sleep != null) {
            sleepLogged.merge(date, sleep, Integer::sum);
        }
    }

    public Map<Date, Double> getWeightLog() { return weightLog; }

    public void setWeightLog(Map<Date, Double> weightLog) { this.weightLog = weightLog; }

    public void logWeight(Date date, Double weight) {
        if (date != null && weight != null && weight >= 0) {
            weightLog.merge(date, weight, Double::sum);
        }
    }
}
