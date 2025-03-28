package org.example.users;

public class UserCreator {

    public static User createUser(UserType userType, String username, String password, String email) {
        switch (userType) {
            case ADMIN:
                return new AdminUser(username, password, email);
            case TRAINER:
                return new TrainerUser(username, password, email);
            case BASIC:
                return new BasicUser(username, password, email);
            default:
                throw new IllegalArgumentException("Unsupported user type: " + userType);
        }
    }
}