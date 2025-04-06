package org.example.bearfitness;

import jakarta.persistence.*;
import java.util.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<ExercisePlan> subscribedPlans = new ArrayList<>();

    @Embedded
    private UserStats userStats;

    @Embedded
    private UserGoals goals;

    @ElementCollection
    private List<WorkoutEntry> entryList = new ArrayList<>();

    protected String username;
    protected String password;
    protected String email;
    protected UserType userType;

    public User() {}

    public User(String username, String password, String email, UserType userType) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.userType = userType;
    }

    public Long getId() { return id; }

    public List<ExercisePlan> getSubscribedPlans() { return subscribedPlans; }
    public void setSubscribedPlans(List<ExercisePlan> subscribedPlans) { this.subscribedPlans = subscribedPlans; }
    public void addPlan(ExercisePlan plan) { subscribedPlans.add(plan); }

    public UserGoals getGoals() { return goals; }
    public void setGoals(UserGoals goals) { this.goals = goals; }
    public void setGoalWeight(Integer weight) { this.goals.setGoalWeight(weight); }
    public void setWeeklyActivities(Integer weeklyActivities) { this.goals.setWeeklyActivities(weeklyActivities); }

    public void addWorkoutEntry(WorkoutEntry entry) { entryList.add(entry); }
    public void workoutEntryCreated(int duration, WorkoutEntry.ExerciseType exerciseType, int caloriesBurned, int stepCount) {
        addWorkoutEntry(new WorkoutEntry(duration, exerciseType, caloriesBurned, stepCount));
    }

    public void logCalories(Date date, Integer calories) { userStats.logCalories(date, calories); }
    public void logSleep(Date date, Integer sleep) { userStats.logSleep(date, sleep); }

    public List<WorkoutEntry> getEntryList() { return entryList; }
    public Map<Date, Integer> getCaloriesLogged() { return userStats.getCaloriesLogged(); }
    public Map<Date, Integer> getSleepLogged() { return userStats.getSleepLogged(); }
    public Map<Date, Double> getWeightLog() { return userStats.getWeightLog(); }
    public void recordWeight(Date date, Double weight) { userStats.logWeight(date, weight); }
}
