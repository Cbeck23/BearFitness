package org.example.bearfitness;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Configuration
public class DBService {
    @Autowired
    private BearDB db;


    @Bean
    public CommandLineRunner testDatabase(BearDB db) {
        return args -> {
            // Save a user
            Date testDate = new Date();
            int calories = 100;
            int sleep = 8;
            User user = new User("test", "test", "test", UserType.ADMIN);
            user.logCalories(testDate, calories);
            user.logSleep(testDate, sleep);
            user.workoutEntryCreated(10, WorkoutEntry.ExerciseType.RUN, calories, sleep);
            user.setGoals(new UserGoals(195, 5 ));
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


    public User createUser(String username, String password, String email, UserType userType) {
        User newUser = new User(username, password, email, userType);
        return db.save(newUser);
    }
}
