public class Workout {

    private int duration;
    private String workoutType;
    private double caloriesBurned;
    private int stepCount;

    // No-argument constructor
    public Workout() {}

    // Setters and Getters
    public void setDuration(int d) {
        this.duration = d;
    }

    public int getDuration() {
        return duration;
    }

    public void setType(String type) {
        this.workoutType = type;
    }

    public String getType() {
        return workoutType;
    }

    public void setCalories(double cal) {
        this.caloriesBurned = cal;
    }

    public double getCalories() {
        return caloriesBurned;
    }

    public void setSteps(int steps) {
        this.stepCount = steps;
    }

    public int getStepCount() {
        return stepCount;
    }
}
