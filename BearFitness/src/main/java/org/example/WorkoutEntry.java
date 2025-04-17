package org.example;

import java.util.Objects;

public class WorkoutEntry {

    //duration in mins
    private int duration = 0;
    private enum ExerciseType {
        WALK("Walk"),
        RUN("Run"),
        SWIM("Swim"),
        BIKE("Bike"),
        SPORTS("Sports"),
        WEIGHTS("Weightlifting"),
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
    private int caloriesBurned = 0;
    int stepCount = 0;




    public WorkoutEntry(int duration, ExerciseType exerciseType, int caloriesBurned, int stepCount) {
        this.duration = duration;
        this.exerciseType = exerciseType;
        this.caloriesBurned = caloriesBurned;
        this.stepCount = stepCount;
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

    public int getCaloriesBurned() {
        return caloriesBurned;
    }

    public void setCaloriesBurned(int caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }

    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        WorkoutEntry that = (WorkoutEntry) o;
        return Double.compare(getDuration(), that.getDuration()) == 0 && getCaloriesBurned() == that.getCaloriesBurned() && getStepCount() == that.getStepCount() && getExerciseType() == that.getExerciseType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDuration(), getExerciseType(), getCaloriesBurned(), getStepCount());
    }

    @Override
    public String toString() {
        return "WorkoutEntry: " +
                "Duration = " + duration +
                ", Exercise Type =" + exerciseType +
                ", Calories Burned =" + caloriesBurned +
                ", Step Count =" + stepCount;
    }
}
