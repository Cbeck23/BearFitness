package org.example.bearfitness.data;

import org.example.bearfitness.fitness.ExerciseClass;
import org.example.bearfitness.fitness.ExercisePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassRepository extends JpaRepository<ExerciseClass, Long> {
}