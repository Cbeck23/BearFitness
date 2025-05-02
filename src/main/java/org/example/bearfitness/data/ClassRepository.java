package org.example.bearfitness.data;

import org.example.bearfitness.fitness.ExerciseClass;
import org.example.bearfitness.fitness.ExercisePlan;
import org.example.bearfitness.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ClassRepository extends JpaRepository<ExerciseClass, Long> {
    List<ExerciseClass> findByUserAndDate(User user, LocalDate date);
}