package org.example.bearfitness;

import org.example.bearfitness.data.DBService;
import org.example.bearfitness.data.BearDB;
import org.example.bearfitness.data.ExercisePlanRepository;
import org.example.bearfitness.data.UserEntryRepository;

import org.example.bearfitness.fitness.ExercisePlan;
import org.example.bearfitness.fitness.UserWorkoutEntry;
import org.example.bearfitness.fitness.WorkoutEntry;
import org.example.bearfitness.user.User;
import org.example.bearfitness.user.UserType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DBServiceTest {

        private DBService dbService;
        private BearDB mockDb;
        private ExercisePlanRepository mockExercisePlanRepo;
        private UserEntryRepository mockUserEntryRepo;

        @BeforeEach
        void setUp() {
            mockDb = mock(BearDB.class);
            mockExercisePlanRepo = mock(ExercisePlanRepository.class);
            mockUserEntryRepo = mock(UserEntryRepository.class);

            dbService = new DBService(mockDb, mockExercisePlanRepo, mockUserEntryRepo);
        }

        @Test
        void authenticateUser_validCredentials_returnsUser() {
            User user = new User("testUser", "password123", "test@example.com", UserType.BASIC);
            when(mockDb.findByUsername("testUser")).thenReturn(Optional.of(user));

            User result = dbService.authenticateUser("testUser", "password123");

            assertNotNull(result);
            assertEquals("testUser", result.getUsername());
        }

        @Test
        void authenticateUser_invalidCredentials_returnsNull() {
            when(mockDb.findByUsername("testUser")).thenReturn(Optional.empty());

            User result = dbService.authenticateUser("testUser", "wrongPassword");

            assertNull(result);
        }

        @Test
        void createUser_validInput_returnsSavedUser() {
            User user = new User("newUser", "newPass", "new@example.com", UserType.BASIC);
            when(mockDb.save(any(User.class))).thenReturn(user);

            User result = dbService.createUser("newUser", "newPass", "new@example.com", UserType.BASIC);

            assertNotNull(result);
            assertEquals("newUser", result.getUsername());
        }

        @Test
        void createExercisePlan_validInput_returnsSavedPlan() {
            ExercisePlan plan = new ExercisePlan();
            when(mockExercisePlanRepo.save(plan)).thenReturn(plan);

            ExercisePlan result = dbService.createExercisePlan(plan);

            assertNotNull(result);
            assertEquals(plan, result);
        }

        @Test
        void getAllPlans_returnsListOfPlanNames() {
            ExercisePlan plan = new ExercisePlan();
            plan.setPlanName("Plan A");
            when(mockExercisePlanRepo.findAll()).thenReturn(Collections.singletonList(plan));

            List<String> plans = dbService.getAllPlans();

            assertEquals(1, plans.size());
            assertEquals("Plan A", plans.get(0));
        }

        @Test
        void createUserWorkoutEntry_validInput_returnsSavedEntry() {
            User user = new User();
            WorkoutEntry entry = new WorkoutEntry();
            UserWorkoutEntry userWorkoutEntry = new UserWorkoutEntry(user, entry);

            when(mockUserEntryRepo.save(any(UserWorkoutEntry.class))).thenReturn(userWorkoutEntry);

            UserWorkoutEntry result = dbService.createUserWorkoutEntry(user, entry);

            assertNotNull(result);
        }

        @Test
        void getUserEntries_validUserId_returnsWorkoutEntries() {
            UserWorkoutEntry userWorkoutEntry = new UserWorkoutEntry();
            WorkoutEntry workoutEntry = new WorkoutEntry();
            userWorkoutEntry.setWorkoutEntry(workoutEntry);

            when(mockUserEntryRepo.findByUserId(1L)).thenReturn(Collections.singletonList(userWorkoutEntry));

            List<WorkoutEntry> entries = dbService.getUserEntries(1L);

            assertEquals(1, entries.size());
            assertEquals(workoutEntry, entries.get(0));
        }

        @Test
        void updateUserWorkoutEntry_existingEntry_updatesSuccessfully() {
            User user = new User();
            user.setId(1L);
            WorkoutEntry entry = new WorkoutEntry();
            UserWorkoutEntry userWorkoutEntry = new UserWorkoutEntry(user, entry);

            when(mockUserEntryRepo.findByUserId(1L)).thenReturn(Collections.singletonList(userWorkoutEntry));

            dbService.updateUserWorkoutEntry(user, entry);

            verify(mockUserEntryRepo, times(1)).save(any(UserWorkoutEntry.class));
        }

        @Test
        void updateUserWorkoutEntry_nonExistingEntry_throwsException() {
            User user = new User();
            user.setId(1L);
            when(mockUserEntryRepo.findByUserId(1L)).thenReturn(Collections.emptyList());

            assertThrows(IllegalArgumentException.class, () -> dbService.updateUserWorkoutEntry(user, new WorkoutEntry()));
        }

        @Test
        void deleteUserWorkoutEntry_existingEntry_deletesSuccessfully() {
            User user = new User();
            user.setId(1L);
            WorkoutEntry entry = new WorkoutEntry();
            UserWorkoutEntry userWorkoutEntry = new UserWorkoutEntry(user, entry);

            when(mockUserEntryRepo.findByUserId(1L)).thenReturn(Collections.singletonList(userWorkoutEntry));

            dbService.deleteUserWorkoutEntry(user, entry);

            verify(mockUserEntryRepo, times(1)).delete(userWorkoutEntry);
        }

        @Test
        void deleteUserWorkoutEntry_nonExistingEntry_throwsException() {
            User user = new User();
            user.setId(1L);
            when(mockUserEntryRepo.findByUserId(1L)).thenReturn(Collections.emptyList());

            assertThrows(IllegalArgumentException.class, () -> dbService.deleteUserWorkoutEntry(user, new WorkoutEntry()));
        }
    }

