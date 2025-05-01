package org.example.bearfitness.data;

import org.example.bearfitness.fitness.ExerciseClass;
import org.example.bearfitness.fitness.ExercisePlan;
import org.example.bearfitness.fitness.UserWorkoutEntry;
import org.example.bearfitness.fitness.WorkoutEntry;
import org.example.bearfitness.user.User;
import org.example.bearfitness.user.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.time.DayOfWeek;
import java.time.LocalDate;
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

    @Autowired
    private ClassRepository classRepository;

    //Constructor for mock tests, Lauren added this lol
    public DBService(BearDB db, ExercisePlanRepository exercisePlanRepository, UserEntryRepository userEntryRepository, ClassRepository classRepository) {
        this.db = db;
        this.ExercisePlanRepository = exercisePlanRepository;
        this.userEntryRepository = userEntryRepository;
        this.classRepository = classRepository;
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

    public UserWorkoutEntry createUserWorkoutEntry(User user, WorkoutEntry entry) {
        UserWorkoutEntry userEntry = new UserWorkoutEntry(user, entry);
        return userEntryRepository.save(userEntry);
    }

    public ExerciseClass createClassEntry(User user, WorkoutEntry classEntry) {
        ExerciseClass entry = new ExerciseClass(user, classEntry);
        return classRepository.save(entry);
    }

    public List<WorkoutEntry> getUserEntries(Long userId) {
        List<UserWorkoutEntry> entries = userEntryRepository.findByUserId(userId);
//        return entries.stream()
//                .map(entry -> new String[]{
//                        String.valueOf(entry.getDate()),
//                        entry.getExerciseTypeValue(),
//                        String.valueOf(entry.getDuration()),
//                        entry.getDescription(),
//                })
//                .collect(Collectors.toList());
        return entries.stream()
                .map(UserWorkoutEntry::getWorkoutEntry)
                .collect(Collectors.toList());
    }

    //TO DO: verify these work
    public void updateUserWorkoutEntry(User user, WorkoutEntry updatedEntry) {
        List<UserWorkoutEntry> entries = userEntryRepository.findByUserId(user.getId());

        for (UserWorkoutEntry userEntry : entries) {
            if (userEntry.getWorkoutEntry().equals(updatedEntry)) {
                userEntry.setWorkoutEntry(updatedEntry);
                userEntryRepository.save(userEntry);
                return;
            }
        }
        throw new IllegalArgumentException("Workout entry not found for user.");
    }

    public void deleteUserWorkoutEntry(User user, WorkoutEntry entryToDelete) {
        List<UserWorkoutEntry> entries = userEntryRepository.findByUserId(user.getId());

        for (UserWorkoutEntry userEntry : entries) {
            if (userEntry.getWorkoutEntry().equals(entryToDelete)) {
                userEntryRepository.delete(userEntry);
                return;
            }
        }
        throw new IllegalArgumentException("Workout entry not found for user.");
    }

    public void updateUserData(User user){
        db.save(user);
    }

    public int getExerciseLastWeek(Long userId) {
        LocalDate today = LocalDate.now();
        // Find the Sunday on or before today:
        LocalDate startOfWeek = today.with(DayOfWeek.SUNDAY);
        // Saturday is 6 days later
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        return userEntryRepository.sumDurationByUserAndDateBetween(
                userId, startOfWeek, endOfWeek
        );

    }

}
