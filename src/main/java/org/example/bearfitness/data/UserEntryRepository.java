package org.example.bearfitness.data;

import org.example.bearfitness.fitness.UserWorkoutEntry;
import org.example.bearfitness.fitness.WorkoutEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserEntryRepository extends JpaRepository<UserWorkoutEntry, Integer> {
    List<UserWorkoutEntry> findByUserId(Long userId);
}