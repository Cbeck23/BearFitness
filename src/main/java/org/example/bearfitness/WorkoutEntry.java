package org.example.bearfitness;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

import java.util.Date;
import java.util.Objects;

@Embeddable
public class WorkoutEntry {

    //duration in mins
    private int duration = 0;
    public enum ExerciseType {
        WALK("Walk"),
        RUN("Run"),
        SWIM("Swim"),
        BIKE("Bike"),
        FULLBODY("FullBody"),
        SPORTS("Sports"),
        WEIGHTS("Weightlifting"),
        CALISTHENICS("Calisthenics"),
        YOGA("Yoga"),
        OTHER("Other");

        private final String exercise;

        ExerciseType(String exercise) {
            this.exercise = exercise;
        }

        public String getExercise() {
            return exercise;
        }
    }
    private ExerciseType exerciseType = ExerciseType.OTHER;
    private String description = "";
    private Date date = new Date();

    public WorkoutEntry() {}

//save these for other personal Basic User workout entry
//    private int caloriesBurned = 0;
//    int stepCount = 0;




    public WorkoutEntry(int duration, ExerciseType exerciseType, int caloriesBurned, int stepCount) {
        this.duration = duration;
        this.exerciseType = exerciseType;
//        this.caloriesBurned = caloriesBurned;
//        this.stepCount = stepCount;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public ExerciseType getExerciseType() {
        return exerciseType;
    }

    public void setExerciseType(ExerciseType exerciseType) {
        this.exerciseType = exerciseType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
//    public int getCaloriesBurned() {
//        return caloriesBurned;
//    }
//
//    public void setCaloriesBurned(int caloriesBurned) {
//        this.caloriesBurned = caloriesBurned;
//    }
//
//    public int getStepCount() {
//        return stepCount;
//    }
//
//    public void setStepCount(int stepCount) {
//        this.stepCount = stepCount;
//    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        WorkoutEntry that = (WorkoutEntry) o;
        return Double.compare(getDuration(), that.getDuration()) == 0 && getExerciseType() == that.getExerciseType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDuration(), getExerciseType());
    }

    @Override
    public String toString() {
        return "WorkoutEntry: " +
                "Duration = " + duration +
                ", Exercise Type =" + exerciseType +
                ", Description = " + description;
    }
}
