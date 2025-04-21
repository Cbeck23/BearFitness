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
        ADMIN
    }

    private final CardLayout layout;
    private final JPanel cards;
    private final DBService dbService;

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
                cards.add(new UserUI(dbService, this, user), Screen.USER.name());
                layout.show(cards, Screen.USER.name());
            }
            case ADMIN -> {
                cards.add(new AdminUI(dbService, this, user), Screen.ADMIN.name());
                layout.show(cards, Screen.ADMIN.name());
            }
        }

        add(cards);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    public void showScreen(Screen screen) {
        layout.show(cards, screen.name());
    }
}
