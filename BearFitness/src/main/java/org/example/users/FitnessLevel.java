<<<<<<<< HEAD:src/main/java/org/example/bearfitness/fitness/FitnessLevel.java
package org.example.bearfitness.fitness;
========
package org.example.users;
>>>>>>>> JosephWang:BearFitness/src/main/java/org/example/users/FitnessLevel.java

public enum FitnessLevel {
    LEVEL_1(1, "Beginner"),
    LEVEL_2(2, "Novice"),
    LEVEL_3(3,"Intermediate"),
    LEVEL_4(4, "Advanced"),
    LEVEL_5(5, "Expert");

    private final int level;
    private final String name;

    FitnessLevel(int level, String name) {
        this.level = level;
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return level + " " + name;
    }

}
