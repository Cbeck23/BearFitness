package org.example.bearfitness.user;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Stores and manages a user's activity statistics.
 */
@Entity
public class UserStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection(fetch = FetchType.EAGER)
    private Map<LocalDate, Integer> caloriesLogged = new HashMap<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private Map<LocalDate, Double> sleepLogged = new HashMap<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private Map<LocalDate, Double> weightLog = new HashMap<>();

    public UserStats() {}

    public Map<LocalDate, Integer> getCaloriesLogged() { return caloriesLogged; }

    public void setCaloriesLogged(Map<LocalDate, Integer> caloriesLogged) { this.caloriesLogged = caloriesLogged; }

    public void logCalories(LocalDate date, Integer calories) {
        if (date != null && calories != null) {
            caloriesLogged.merge(date, calories, Integer::sum);
        }
    }

    public Map<LocalDate, Double> getSleepLogged() { return sleepLogged; }

    public void setSleepLogged(Map<LocalDate, Double> sleepLogged) { this.sleepLogged = sleepLogged; }

    public void logSleep(LocalDate date, Double sleep) {
        if (date != null && sleep != null) {
            sleepLogged.merge(date, sleep, Double::sum);
        }
    }

    public Map<LocalDate, Double> getWeightLog() { return weightLog; }

    public void setWeightLog(Map<LocalDate, Double> weightLog) { this.weightLog = weightLog; }

    public void logWeight(LocalDate date, Double weight) {
        if (date != null && weight != null && weight >= 0) {
            weightLog.put(date, weight);
        }
    }

    //get sum of calories of past week
    public Integer getCaloriesLastWeek() {
        Map<LocalDate, Integer> calorieLog = this.caloriesLogged;
        LocalDate today = LocalDate.now();
        LocalDate oneWeekAgo = today.minusDays(7);

        int totalCalories = 0;

        for (Map.Entry<LocalDate, Integer> entry : calorieLog.entrySet()) {
            LocalDate date = entry.getKey();

            if ((date.isEqual(oneWeekAgo) || date.isAfter(oneWeekAgo)) &&
                    (date.isEqual(today) || date.isBefore(today))) {
                totalCalories += entry.getValue();
            }
        }

        return totalCalories;
    }

    public Double getSleepLastWeek() {
        Map<LocalDate, Double> sleepLog = this.sleepLogged;
        LocalDate today = LocalDate.now();
        LocalDate oneWeekAgo = today.minusDays(7);

        Double totalSleep = 0.0;

        for (Map.Entry<LocalDate, Double> entry : sleepLog.entrySet()) {
            LocalDate date = entry.getKey();

            if ((date.isEqual(oneWeekAgo) || date.isAfter(oneWeekAgo)) &&
                    (date.isEqual(today) || date.isBefore(today))) {
                totalSleep += entry.getValue();
            }
        }

        return totalSleep;
    }
}
