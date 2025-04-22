package org.example.bearfitness.fitness;

import org.example.bearfitness.fitness.WorkoutEntry;
import jakarta.persistence.*;
import org.example.bearfitness.user.User;

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
    // ... setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setWorkoutEntry(WorkoutEntry workoutEntry) {
        this.workoutEntry = workoutEntry;
    }
}