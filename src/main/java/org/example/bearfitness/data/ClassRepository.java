package org.example.bearfitness.data;

import org.example.bearfitness.fitness.ExerciseClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ClassRepository extends JpaRepository<ExerciseClass, Long> {
    List<ExerciseClass> findByDate(LocalDate date);
    List<ExerciseClass> findExerciseClassByName(String name);
    List<ExerciseClass> findByTrainerID(long id);
}