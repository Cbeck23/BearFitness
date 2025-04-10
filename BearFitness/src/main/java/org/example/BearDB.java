package org.example.bearfitness;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BearDB extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
