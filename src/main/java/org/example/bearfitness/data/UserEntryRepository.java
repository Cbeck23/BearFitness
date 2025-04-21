package org.example.bearfitness.data;

import org.example.bearfitness.fitness.WorkoutEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserEntryRepository extends JpaRepository<WorkoutEntry, Integer> {
    List<WorkoutEntry> findByUserId(Long userId);
}