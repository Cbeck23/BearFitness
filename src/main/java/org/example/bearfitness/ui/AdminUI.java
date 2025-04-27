package org.example.bearfitness.ui;

import org.example.bearfitness.data.DBService;
import org.example.bearfitness.user.User;

import javax.swing.*;

public class AdminUI extends UserUI {

    //AdminUI(){}

    public AdminUI(DBService dbService, ScreenManager screenManager, User user) {
        super(dbService, screenManager, user);
    }
}
