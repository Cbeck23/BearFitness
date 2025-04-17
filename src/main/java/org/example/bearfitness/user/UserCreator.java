package org.example.bearfitness.user;

/**
 * Factory class to create User instances.
 */
public class UserCreator {

    /**
     * Creates a User instance based on user type.
     *
     * @param userType The type of user to create (ADMIN, TRAINER, BASIC).
     * @param username The user's username.
     * @param password The user's password.
     * @param email The user's email.
     *
     * Preconditions:
     * - userType, username, password, and email are non-null.
     *
     * Postconditions:
     * - Returns a User object of the specified type.
     * - Throws IllegalArgumentException if userType is unsupported.
     */
    public static User createUser(UserType userType, String username, String password, String email) {
        return new User(username, password, email, userType);
    }
}
