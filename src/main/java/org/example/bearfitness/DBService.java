package org.example.bearfitness;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Configuration
public class DBService {
    @Autowired
    private BearDB db;

    @Autowired
    private ExercisePlanRepository ExercisePlanRepository;


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

    public  ExercisePlan createExercisePlan(ExercisePlan exercisePlan) {
        ExercisePlan newPlan = new ExercisePlan(exercisePlan);
        return ExercisePlanRepository.save(exercisePlan);
    }

    public List<String> getAllPlans() {
        List<ExercisePlan> plans = ExercisePlanRepository.findAll();
        return plans.stream()
                .map(ExercisePlan::getPlanName)
                .collect(Collectors.toList());
    }
}
