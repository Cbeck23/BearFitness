package org.example.bearfitness.data;

import jakarta.transaction.Transactional;
import org.example.bearfitness.fitness.ExerciseClass;
import org.example.bearfitness.fitness.ExercisePlan;
import org.example.bearfitness.fitness.UserWorkoutEntry;
import org.example.bearfitness.fitness.WorkoutEntry;
import org.example.bearfitness.user.User;
import org.example.bearfitness.user.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.context.annotation.Configuration;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Configuration
public class DBService {
    @Autowired
    private BearDB db;

    @Autowired
    private ExercisePlanRepository exercisePlanRepository;

    @Autowired
    private UserEntryRepository userEntryRepository;

    @Autowired
    private ClassRepository classRepository;

    //Constructor for mock tests, Lauren added this lol
    public DBService(BearDB db, ExercisePlanRepository exercisePlanRepository, UserEntryRepository userEntryRepository, ClassRepository classRepository) {
        this.db = db;
        this.exercisePlanRepository = exercisePlanRepository;
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
        return exercisePlanRepository.save(exercisePlan);
    }

    public List<String> getAllPlans() {
        List<ExercisePlan> plans = exercisePlanRepository.findAll();
        return plans.stream()
                .map(ExercisePlan::getPlanName)
                .collect(Collectors.toList());
    }

    public UserWorkoutEntry createUserWorkoutEntry(User user, WorkoutEntry entry) {
        UserWorkoutEntry userEntry = new UserWorkoutEntry(user, entry);
        return userEntryRepository.save(userEntry);
    }

    public List<ExerciseClass> createClassEntry(User user, ExercisePlan classEntry, List<LocalDate> dates) {
        List<ExerciseClass> entries = new ArrayList<>();
        for (LocalDate date : dates) {
            entries.add(new ExerciseClass(
                    user,
                    classEntry.getPlanName(),
                    date,
                    classEntry.getRecommendedFitnessLevel(),
                    classEntry.getAverageSessionLength()
            ));
        }
        return classRepository.saveAll(entries);
    }

    public List<ExerciseClass> getClassesOnDate(LocalDate date) {
        return classRepository.findByDate(date);
    }



    public List<WorkoutEntry> getUserEntries(Long userId) {
        List<UserWorkoutEntry> entries = userEntryRepository.findByUserId(userId);
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
        LocalDate startOfWeek = today.minusWeeks(1).with(DayOfWeek.SUNDAY);
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        System.out.println("Calling countEntriesByUserAndDateBetween with:");
        System.out.println("userId = " + userId);
        System.out.println("startOfWeek = " + startOfWeek);
        System.out.println("endOfWeek = " + endOfWeek);
        return userEntryRepository.countEntriesByUserAndDateBetween(
                userId, startOfWeek, endOfWeek
        );
    }

    public int countByUserId(Long userId) {
        return userEntryRepository.countByUserId(userId);
    }

    public List<ExercisePlan> findExercisePlanByName(String planName) {
        return exercisePlanRepository.findExercisePlanByPlanName(planName);
    }

    public List<ExerciseClass> findExerciseClassByName(String name) {
        return classRepository.findExerciseClassByName(name);
    }

    public List<User> getAllUsers() {
        return db.findAll();
    }

    public void deleteUser(User user) {
        // First delete the user's workout entries
        List<UserWorkoutEntry> entries = userEntryRepository.findByUserId(user.getId());
        userEntryRepository.deleteAll(entries);

        // Then delete the user
        db.delete(user);
    }

    public Optional<User> findUserByUsername(String username) {
        return db.findByUsername(username);
    }

    //for GoalsDisplayUI
    public Map<LocalDate, Integer> getCalsLogged(User user) {
        return user.getUserStats().getCaloriesLogged();
    }

    public Map<LocalDate, Double> getSleepLogged(User user) {
        return user.getUserStats().getSleepLogged();
    }

    public Map<LocalDate, Double> getWeightLogged(User user) {
        return user.getUserStats().getWeightLog();
    }

    public List<WorkoutEntry> getWorkoutEntriesForUser(User user) {
        return userEntryRepository.findByUserId(user.getId())
                .stream()
                .map(UserWorkoutEntry::getWorkoutEntry)
                .collect(Collectors.toList());

    }

    public List<String> getAllExercisePlans() {
        return exercisePlanRepository.findAll().stream().map(ExercisePlan::toString).collect(Collectors.toList());
    }

//    List<ExercisePlan> findExercisePlansByName(String planName) {
//        return exercisePlanRepository.findExercisePlanByPlanName(planName);
//    }

    public List<String> getAllExerciseClassNames() {
        List<ExerciseClass> classes = classRepository.findAll();
        return classes.stream()
                .map(ExerciseClass::getName)
                .collect(Collectors.toList());
    }

    public List<String> getAllExerciseClassNamesWithTrainer() {
        List<ExerciseClass> classes = classRepository.findAll();
        System.out.println(classes.stream()
                .map(c -> c.getDate() + " : "+c.getName() + " - Hosted by " + c.getUser().getUsername() + " | Recommended Fitness Level: " + c.getFitnessLevel())
                .collect(Collectors.toList()));
        return classes.stream()
                .map(c -> c.getDate() + " : "+c.getName() + " - Hosted by " + c.getUser().getUsername() + " | Recommended Fitness Level: " + c.getFitnessLevel())
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletePlanByName(String name) {
        List<ExercisePlan> plans = exercisePlanRepository.findExercisePlanByPlanName(name);
        for (ExercisePlan plan : plans) {
            List<User> allUsers = db.findAll();
            for (User user : allUsers) {
                if (user.getSubscribedPlans().contains(plan)) {
                    user.getSubscribedPlans().remove(plan);
                    db.save(user);
                }
            }
            exercisePlanRepository.delete(plan);
        }
    }
    public ExercisePlan getPlanByName(String name) {
        List<ExercisePlan> plans = exercisePlanRepository.findExercisePlanByPlanName(name);
        if (!plans.isEmpty()) {
            return plans.get(0);
        }
        return null;
    }

    public void updateExercisePlan(ExercisePlan updatedPlan) {
        List<ExercisePlan> existing = exercisePlanRepository.findExercisePlanByPlanName(updatedPlan.getPlanName());
        if (!existing.isEmpty()) {
            ExercisePlan existingPlan = existing.get(0);

            existingPlan.getRequiredEquipment().clear();
            existingPlan.getRequiredEquipment().addAll(updatedPlan.getRequiredEquipment());

            existingPlan.setRecommendedFitnessLevel(updatedPlan.getRecommendedFitnessLevel());
            existingPlan.setAverageSessionLength(updatedPlan.getAverageSessionLength());
            existingPlan.setFrequencyPerWeek(updatedPlan.getFrequencyPerWeek());

            existingPlan.getExercises().clear();
            existingPlan.getExercises().putAll(updatedPlan.getExercises());

            exercisePlanRepository.save(existingPlan);
        }
    }

}
