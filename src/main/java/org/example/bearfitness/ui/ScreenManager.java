package org.example.bearfitness.ui;

import org.example.bearfitness.ui.*;
import org.example.bearfitness.data.DBService;
import org.example.bearfitness.user.User;
import org.example.bearfitness.user.UserType;

import javax.swing.*;
import java.awt.*;

public class ScreenManager extends JFrame {
    public enum Screen {
        LOGIN,
        SIGNUP,
        TRAINER,
        USER,
        ADMIN,
        USER_HOME,
        ADD_WORKOUT,
        SETTINGS,
        ADMINISTRATION,
        VIEW_CLASSES
    }

    private final CardLayout layout;
    private final JPanel cards;
    private final DBService dbService;

    private UserUI userHome;
    private WorkoutHistoryUI workoutHistory;
    private AddWorkoutUI addWorkout;
    //private final User user;

    public ScreenManager(DBService dbService) {
        super("BearFitness");
        this.dbService = dbService;

        layout = new CardLayout();
        cards = new JPanel(layout);

        cards.add(new LoginUI(dbService, this), Screen.LOGIN.name());
        cards.add(new SignUpUI(dbService, this), Screen.SIGNUP.name());

        add(cards);
        layout.show(cards, Screen.LOGIN.name());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    public ScreenManager(DBService dbService, User user) {
        super("BearFitness - " + user.getUsername());
        this.dbService = dbService;

        layout = new CardLayout();
        cards = new JPanel(layout);

        switch (user.getUserType()) {
            case TRAINER -> {
                cards.add(new TrainerUI(dbService, this, user), Screen.TRAINER.name());
                layout.show(cards, Screen.TRAINER.name());
            }
            case BASIC -> {
                cards.add(new UserUI(dbService, this, user), Screen.USER_HOME.name());
                cards.add(new UserSettings(dbService, this, user).getPanel(), Screen.SETTINGS.name());
                cards.add(new UserClassesUI(dbService, this, user), Screen.VIEW_CLASSES.name());
                layout.show(cards, Screen.USER_HOME.name());
            }
            case ADMIN -> {
                cards.add(new UserUI(dbService, this, user), Screen.ADMIN.name());
                cards.add(new UserSettings(dbService, this, user).getPanel(), Screen.SETTINGS.name());
                cards.add(new AdminManagementUI(dbService, this, user), Screen.ADMINISTRATION.name());
                cards.add(new UserClassesUI(dbService, this, user), Screen.VIEW_CLASSES.name());
                layout.show(cards, Screen.ADMIN.name());
            }
        }

        add(cards);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }
    private boolean isScreenAdded(Screen screen) {
        for (Component comp : cards.getComponents()) {
            if (screen.name().equals(cards.getLayout().toString())) {
                return true;
            }
        }
        return false;
    }
    public void showScreen(Screen screen) {
        showScreen(screen, null);
    }
    public void showScreen(Screen screen, User user) {
        if (screen == Screen.USER_HOME && user != null) {
            // Only add if it hasn't already been added
            if (!isScreenAdded(screen)) {
                UserUI userUI = new UserUI(dbService, this, user);
                cards.add(userUI, screen.name());
            }
        }
        layout.show(cards, screen.name());
    }

}
