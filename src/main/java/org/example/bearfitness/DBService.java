package org.example.bearfitness;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Configuration
public class DBService {
    @Autowired
    private BearDB db;

    @Bean
    public CommandLineRunner testDatabase(BearDB db) {
        return args -> {
            // Save a user
            User user = new User("test", "test", "test", UserType.ADMIN);
            db.save(user);

        };
    }

    public User authenticateUser(String username, String password) {
        Optional<User> optionalUser = db.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (user.getPassword().equals(password)) {
                return user;
            }
        }

        return null;

    }
}
