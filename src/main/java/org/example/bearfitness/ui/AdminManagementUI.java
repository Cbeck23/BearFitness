package org.example.bearfitness.ui;

import org.example.bearfitness.data.DBService;
import org.example.bearfitness.user.User;

import javax.swing.*;

public class AdminManagementUI extends JPanel {
    AdminManagementUI(DBService dbService, ScreenManager screenManager, User user) {
        JTable userTable = new JTable();
        JScrollPane userTableScrollPane = new JScrollPane(userTable);
        userTableScrollPane.setViewportView(userTable);
        JButton addUserButton = new JButton("Add User");
        addUserButton.addActionListener(e -> {

        });

        JButton deleteUserButton = new JButton("Delete User");
        deleteUserButton.addActionListener(e -> {

        });
        JButton editUserButton = new JButton("Edit User");
        editUserButton.addActionListener(e -> {

        });
    }

}
