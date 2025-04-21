package org.example.bearfitness.data;

import org.example.bearfitness.fitness.ExercisePlan;
import org.example.bearfitness.fitness.WorkoutEntry;
import org.example.bearfitness.user.User;
import org.example.bearfitness.user.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Configuration
public class DBService {
    @Autowired
    private BearDB db;

    @Autowired
    private ExercisePlanRepository ExercisePlanRepository;

    @Autowired
    private UserEntryRepository userEntryRepository;


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

    public ExercisePlan createExercisePlan(ExercisePlan exercisePlan) {
        ExercisePlan newPlan = new ExercisePlan(exercisePlan);
        return ExercisePlanRepository.save(exercisePlan);
    }

    public List<String> getAllPlans() {
        List<ExercisePlan> plans = ExercisePlanRepository.findAll();
        return plans.stream()
                .map(ExercisePlan::getPlanName)
                .collect(Collectors.toList());
    }

    public List<String[]> getUserEntries(Long userId) {
        List<WorkoutEntry> entries = userEntryRepository.findByUserId(userId);
        return entries.stream()
                .map(entry -> new String[]{
                        String.valueOf(entry.getDate()),
                        entry.getExerciseTypeValue(),
                        String.valueOf(entry.getDuration()),
                        entry.getDescription(),
                })
                .collect(Collectors.toList());
    }


}
