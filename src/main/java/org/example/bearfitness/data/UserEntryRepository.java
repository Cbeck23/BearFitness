package org.example.bearfitness.data;

import org.example.bearfitness.fitness.UserWorkoutEntry;
import org.example.bearfitness.fitness.WorkoutEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserEntryRepository extends JpaRepository<UserWorkoutEntry, Integer> {
    List<UserWorkoutEntry> findByUserId(Long userId);

    @Query(value = """
    SELECT COUNT(*)
    FROM user_entry_list
    WHERE user_id = :userId
      AND date BETWEEN :startOfWeek AND :endOfWeek
""", nativeQuery = true)
    int countEntriesByUserAndDateBetween(
            @Param("userId") Long userId,
            @Param("startOfWeek") LocalDate startOfWeek,
            @Param("endOfWeek") LocalDate endOfWeek
    );

    @Query("""
    SELECT COUNT(e)
    FROM UserWorkoutEntry e
    WHERE e.user.id = :userId
""")
    int countByUserId(@Param("userId") Long userId);
}