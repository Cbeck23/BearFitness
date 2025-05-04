package org.example.bearfitness.user;

import jakarta.persistence.*;
import lombok.Getter;
import org.example.bearfitness.fitness.ExerciseClass;
import org.example.bearfitness.data.PasswordHash;
import org.example.bearfitness.fitness.ExercisePlan;

import java.time.LocalDate;
import java.util.*;

/**
 * Represents a user in the BearFitness application.
 * Stores personal details, goals, health stats, workout logs, and exercise plans.
 */
@Entity
public class User {
    // === Identification ===

    /** Unique identifier for the user. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // === Personal Information ===

    /** Username for login/authentication. */
    protected String username;

    /** Password (should be stored hashed in production). */
    protected String password;

    /** User's email address. */
    protected String email;

    /** The role or type of the user (e.g., ADMIN, REGULAR). */
    protected UserType userType;

    // === Health & Fitness Tracking ===

    /** User-specific health and fitness statistics. */
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    //@JoinColumn(name = "user_stats_id") // optional: customize the column name in DB
    private UserStats userStats = null;

    /** Fitness goals set by the user. */
    @Embedded
    private UserGoals goals;

    /** Logs of workouts performed by the user. */
//    @ElementCollection
//    private List<WorkoutEntry> entryList = new ArrayList<>();

    /** Exercise classes the user is currently subscribed to. */
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ExerciseClass> subscribedPlans;

    /** Exercise plans the user is currently subscribed to. */
    @Getter
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ExercisePlan> exercisePlans;

    // === Constructors ===

    /** Default constructor required by JPA. */
    public User() {}

    /**
     * Constructs a new User with login details and user type.
     *
     * @param username Username for login.
     * @param password User's password.
     * @param email User's email.
     * @param userType Type/role of the user.
     */
    public User(String username, String password, String email, UserType userType) {
        this.username = username;
        this.password = PasswordHash.hashPassword(password);
        this.email = email;
        this.userType = userType;
        this.userStats = new UserStats();
        this.goals = new UserGoals();
        this.subscribedPlans = new ArrayList();
        this.exercisePlans = new ArrayList();
    }

    // === Getters and Setters ===
    public void setExercisePlans(List<ExercisePlan> exercisePlans) {
        this.exercisePlans = exercisePlans;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public UserStats getUserStats() {
        if(userStats == null) {
            return new UserStats();
        }
        else{
            return userStats;
        }
    }

    public void setUserStats(UserStats userStats) {
        this.userStats = userStats;
    }

    public void logNewCalories(Integer newCalories) {
        if(userStats==null) {
            userStats = new UserStats();
        }
        //FIX?: maybe can change later to ask the user to enter a day/time
        LocalDate currentTime = LocalDate.now();
        userStats.logCalories(currentTime, newCalories);
    }

    public void setNewGoalCalories(Integer newGoal) {
        if(goals==null) {
            goals = new UserGoals();
        }
        goals.setGoalCalories(newGoal);
    }

    public UserGoals getGoals() {
        if(goals == null) {
            return new UserGoals();
        }
        return goals;
    }

    public void setGoals(UserGoals goals) {
        this.goals = goals;
    }


    public List<ExerciseClass> getSubscribedPlans() {
        return subscribedPlans;
    }

    public List<ExerciseClass> getSubscribedClasses() {
        return subscribedPlans;
    }

    public void setSubscribedPlans(List<ExerciseClass> subscribedPlans) {
        this.subscribedPlans = subscribedPlans;
    }

    // === Goal Management ===

    /**
     * Sets the user's goal weight.
     *
     * @param weight The target weight.
     */
    public void setGoalWeight(Integer weight) {
        this.goals.setGoalWeight(weight);
    }

    /**
     * Sets the number of weekly activities the user aims to complete.
     *
     * @param weeklyExHours Target number of weekly activities.
     */
    public void setWeeklyExMinutes(Integer weeklyExHours) {
        if(goals == null) {
            goals = new UserGoals();
        }
        this.goals.setWeeklyExMinutes(weeklyExHours);
    }

    // === Exercise Plan and Class Management ===

    /**
     * Adds a new exercise plan to the list of subscribed plans.
     *
     * @param plan The exercise plan to add.
     */
    public void addPlan(ExercisePlan plan) {
        exercisePlans.add(plan);
    }

    /**
     * Adds a new exercise class to the list of subscribed plans.
     *
     * @param plan The exercise plan to add.
     */
    public void addClass(ExerciseClass plan) {
        subscribedPlans.add(plan);
    }




    // === Daily Tracking ===

    /**
     * Logs calories consumed on a specific date.
     *
     * @param date Date of entry.
     * @param calories Number of calories.
     */
    public void logCalories(LocalDate date, Integer calories) {
        userStats.logCalories(date, calories);
    }

    /**
     * Logs hours of sleep on a specific date.
     *
     * @param date Date of entry.
     * @param sleep Hours of sleep.
     */
    public void logSleep(Date date, Integer sleep) {
        userStats.logSleep(date, sleep);
    }

    /**
     * Records the user's weight on a specific date.
     *
     * @param date Date of entry.
     * @param weight Weight in kilograms or pounds.
     */
    public void recordWeight(Date date, Double weight) {
        userStats.logWeight(date, weight);
    }

    // === Logs Accessors ===

    /** @return Map of calories logged by date. */
    public Map<LocalDate, Integer> getCaloriesLogged() {
        return userStats.getCaloriesLogged();
    }

    /** @return Map of sleep hours logged by date. */
    public Map<Date, Integer> getSleepLogged() {
        return userStats.getSleepLogged();
    }

    /** @return Map of weight entries by date. */
    public Map<Date, Double> getWeightLog() {
        return userStats.getWeightLog();
    }
}
