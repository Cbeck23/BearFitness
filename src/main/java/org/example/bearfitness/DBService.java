package org.example.bearfitness;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@Configuration
public class DBService {
    @Bean
    public CommandLineRunner testDatabase(BearDB db) {
        return args -> {
            // Save a user
            User user = new User("test", "test", "test", UserType.ADMIN);
            db.save(user);

        };
    }
}
