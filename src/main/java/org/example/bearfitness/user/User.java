package org.example.bearfitness.user;

import jakarta.persistence.*;
import org.example.bearfitness.fitness.ExercisePlan;
import org.example.bearfitness.data.PasswordHash;
import org.example.bearfitness.fitness.WorkoutEntry;

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

    /** Exercise plans the user is currently subscribed to. */
    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<ExercisePlan> subscribedPlans = new ArrayList<>();

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
        //this.entryList = new ArrayList();
        this.subscribedPlans = new ArrayList();
    }

    // === Getters and Setters ===

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

    public void setNewGoalExercises(Integer newGoal) {
        if(goals==null) {
            goals = new UserGoals();
        }
        goals.setWeeklyExercises(newGoal);
    }

    public void logNewSleep(Double newSleep) {
        if(userStats==null) {
            userStats = new UserStats();
        }
        //FIX?: maybe can change later to ask the user to enter a day/time
        LocalDate currentTime = LocalDate.now();
        userStats.logSleep(currentTime, newSleep);
    }

    public void setNewGoalSleep(Double newGoal) {
        if(goals==null) {
            goals = new UserGoals();
        }
        goals.setGoalSleep(newGoal);
    }
//    public List<WorkoutEntry> getEntryList() {
//        return entryList;
//    }
//
//    public void setEntryList(List<WorkoutEntry> entryList) {
//        this.entryList = entryList;
//    }


    public List<ExercisePlan> getSubscribedPlans() {

        if(subscribedPlans.isEmpty()) {
            return new ArrayList<>();
        }
        return subscribedPlans;
    }

    public void setSubscribedPlans(List<ExercisePlan> subscribedPlans) {
        this.subscribedPlans = subscribedPlans;
    }

    // === Goal Management ===

    /**
     * Sets the user's goal weight.
     *
     * @param weight The target weight.
     */
    public void setGoalWeight(Double weight) {
        this.goals.setGoalWeight(weight);
    }

    /**
     * Sets the number of weekly activities the user aims to complete.
     *
     * @param weeklyEx Target number of weekly activities.
     */
    public void setWeeklyExercises(Integer weeklyEx) {
        if(goals == null) {
            goals = new UserGoals();
        }
        this.goals.setWeeklyExercises(weeklyEx);
    }

    // === Exercise Plan Management ===

    /**
     * Adds a new exercise plan to the list of subscribed plans.
     *
     * @param plan The exercise plan to add.
     */
    public void addPlan(ExercisePlan plan) {
        subscribedPlans.add(plan);
    }

    // === Workout Logging ===

//    /**
//     * Adds a workout entry to the user's workout log.
//     *
//     * @param entry The workout entry to add.
//     */
//    public void addWorkoutEntry(WorkoutEntry entry) {
//        entryList.add(entry);
//    }

//    /**
//     * Creates and logs a workout entry.
//     *
//     * @param duration Duration of the workout.
//     * @param exerciseType Type of exercise.
//     * @param caloriesBurned Calories burned.
//     * @param stepCount Number of steps taken.
//     */
//    public void workoutEntryCreated(int duration, WorkoutEntry.ExerciseType exerciseType,
//                                    int caloriesBurned, int stepCount) {
//        addWorkoutEntry(new WorkoutEntry(duration, exerciseType, caloriesBurned, stepCount));
//    }

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
    public void logSleep(LocalDate date, Double sleep) {
        userStats.logSleep(date, sleep);
    }

    /**
     * Records the user's weight on a specific date.
     *
     * @param date Date of entry.
     * @param weight Weight in kilograms or pounds.
     */
    public void recordWeight(LocalDate date, Double weight) {
        userStats.logWeight(date, weight);
    }

    // === Logs Accessors ===

    /** @return Map of calories logged by date. */
    public Map<LocalDate, Integer> getCaloriesLogged() {
        return userStats.getCaloriesLogged();
    }

    /** @return Map of sleep hours logged by date. */
    public Map<LocalDate, Double> getSleepLogged() {
        return userStats.getSleepLogged();
    }

    /** @return Map of weight entries by date. */
    public Map<LocalDate, Double> getWeightLog() {
        return userStats.getWeightLog();
    }

    public int getTotalCaloriesLogged(UserStats stats) {
        return stats.getCaloriesLogged()
                .values()
                .stream()
                .mapToInt(Integer::intValue)
                .sum();
    }
    public double getTotalSleepLogged(UserStats stats) {
        return stats.getSleepLogged()
                .values()
                .stream()
                .mapToDouble(Double::doubleValue)
                .sum();
    }
}
