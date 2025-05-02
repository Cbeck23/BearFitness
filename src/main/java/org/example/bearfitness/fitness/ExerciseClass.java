package org.example.bearfitness.fitness;

import jakarta.persistence.*;
import org.example.bearfitness.user.User;

import java.time.LocalDate;

@Entity
public class ExerciseClass {
    private String name;
    private LocalDate date;
    private String fitnessLevel;
    private int sessionLength; // in minutes

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getFitnessLevel() {
        return fitnessLevel;
    }

    public int getSessionLength() {
        return sessionLength;
    }

    public User getUser() {
        return user;
    }

    public ExerciseClass(User user, String name, LocalDate date, String fitnessLevel, int sessionLength) {
        this.user = user;
        this.name = name;
        this.date = date;
        this.fitnessLevel = fitnessLevel;
        this.sessionLength = sessionLength;
    }


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public ExerciseClass() {}


    @Override
    public String toString() {
        return "ExerciseClass{" +
                "name='" + name + '\'' +
                ", date=" + date +
                ", fitnessLevel='" + fitnessLevel + '\'' +
                ", sessionLength=" + sessionLength +
                '}';
    }

    public Long getId() {
        return id;
    }
}
