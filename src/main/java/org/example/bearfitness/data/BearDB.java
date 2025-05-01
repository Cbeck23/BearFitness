package org.example.bearfitness.data;

import org.example.bearfitness.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BearDB extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
