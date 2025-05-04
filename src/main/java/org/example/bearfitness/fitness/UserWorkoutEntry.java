package org.example.bearfitness.fitness;

import org.example.bearfitness.fitness.WorkoutEntry;
import jakarta.persistence.*;
import org.example.bearfitness.user.User;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

@Entity
@Table(name = "user_entry_list") // Maps to the desired table name
public class UserWorkoutEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne // Many entries can belong to one user
    @JoinColumn(name = "user_id") // Foreign key to User
    private User user;

    @Embedded // Includes fields from WorkoutEntry (embedded object)
    private WorkoutEntry workoutEntry;

    // Constructors, getters, and setters
    public UserWorkoutEntry() {}

    public UserWorkoutEntry(User user, WorkoutEntry workoutEntry) {
        this.user = user;
        this.workoutEntry = workoutEntry;
    }

    // Getters and setters
    public Long getId() { return id; }
    public User getUser() { return user; }
    public WorkoutEntry getWorkoutEntry() { return workoutEntry; }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setWorkoutEntry(WorkoutEntry workoutEntry) {
        this.workoutEntry = workoutEntry;
    }

    @Column(name = "workout_date")
    private LocalDate date;

    private Double weight;
    private Integer sleep;
    private Integer calories;
    private Integer workoutsCompleted;

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public void setSleep(Integer sleep) {
        this.sleep = sleep;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public void setWorkoutsCompleted(Integer workoutsCompleted) {
        this.workoutsCompleted = workoutsCompleted;
    }

    public LocalDate getDate() {
        return date;
    }

    public Double getWeight() {
        return weight;
    }

    public Integer getSleep() {
        return sleep;
    }

    public Integer getCalories() {
        return calories;
    }

    public Integer getWorkoutsCompleted() {
        return workoutsCompleted;
    }
}