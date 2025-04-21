package org.example.bearfitness.ui;

import org.example.bearfitness.data.*;
import org.example.bearfitness.ui.ScreenManager;
import org.example.bearfitness.user.User;

import javax.swing.*;

public class UserUI extends JPanel{
    DBService dbService;
    ScreenManager screenManager;
    User user;

    UserUI(){}

    UserUI(DBService dbService, ScreenManager screenManager, User user) {
        this.dbService = dbService;
        this.screenManager = screenManager;
        this.user = user;
    }
}
