package org.example.bearfitness.ui;

import org.example.bearfitness.data.DBService;
import org.example.bearfitness.user.User;
import org.example.bearfitness.user.UserType;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Optional;

public class AdminManagementUI extends JPanel {

    private JTable userTable;
    private DBService dbService;
    private ScreenManager screenManager;
    private User user;

    public AdminManagementUI(DBService dbService, ScreenManager screenManager, User user) {
        this.dbService = dbService;
        this.screenManager = screenManager;
        this.user = user;

        setLayout(new BorderLayout());

        // User table
        userTable = new JTable();
        JScrollPane userTableScrollPane = new JScrollPane(userTable);
        add(userTableScrollPane, BorderLayout.CENTER);

        // Buttons
        JButton addUserButton = new JButton("Add User");
        JButton editUserButton = new JButton("Edit User");
        JButton deleteUserButton = new JButton("Delete User");
        JButton backButton = new JButton("Back");

        // Button panel (horizontal center)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.add(addUserButton);
        buttonPanel.add(editUserButton);
        buttonPanel.add(deleteUserButton);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action listeners

        // Add user
        addUserButton.addActionListener(e -> addUser());

        // Edit user
        editUserButton.addActionListener(e -> editUser());

        // Delete user
        deleteUserButton.addActionListener(e -> deleteUser());

        // Back button
        backButton.addActionListener(e -> screenManager.showScreen(ScreenManager.Screen.ADMIN));

        populateUserTable();
    }

    private void addUser() {
        JTextField usernameField = new JTextField();
        JTextField passwordField = new JTextField();
        JTextField emailField = new JTextField();
        JComboBox<UserType> userTypeCombo = new JComboBox<>(UserType.values());

        Object[] fields = {
                "Username:", usernameField,
                "Password:", passwordField,
                "Email:", emailField,
                "User Type:", userTypeCombo
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Add User", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                dbService.createUser(
                        usernameField.getText(),
                        passwordField.getText(),
                        emailField.getText(),
                        (UserType) userTypeCombo.getSelectedItem()
                );
                JOptionPane.showMessageDialog(this, "User created successfully!");
                populateUserTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to create user: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow != -1) {
            String username = userTable.getValueAt(selectedRow, 0).toString();
            Optional<User> userOpt = dbService.findUserByUsername(username);

            if (userOpt.isPresent()) {
                User selectedUser = userOpt.get();

                JTextField usernameField = new JTextField(selectedUser.getUsername());
                JTextField passwordField = new JTextField(selectedUser.getPassword());
                JTextField emailField = new JTextField(selectedUser.getEmail());
                JComboBox<UserType> userTypeCombo = new JComboBox<>(UserType.values());
                userTypeCombo.setSelectedItem(selectedUser.getUserType());

                Object[] fields = {
                        "Username:", usernameField,
                        "Password:", passwordField,
                        "Email:", emailField,
                        "User Type:", userTypeCombo
                };

                int option = JOptionPane.showConfirmDialog(this, fields, "Edit User", JOptionPane.OK_CANCEL_OPTION);

                if (option == JOptionPane.OK_OPTION) {
                    selectedUser.setUsername(usernameField.getText());
                    selectedUser.setPassword(passwordField.getText());
                    selectedUser.setEmail(emailField.getText());
                    selectedUser.setUserType((UserType) userTypeCombo.getSelectedItem());

                    try {
                        dbService.updateUserData(selectedUser);
                        JOptionPane.showMessageDialog(this, "User updated successfully!");
                        populateUserTable();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Failed to update user: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a user to edit.");
        }
    }

    private void deleteUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow != -1) {
            String username = userTable.getValueAt(selectedRow, 0).toString();
            Optional<User> userOpt = dbService.findUserByUsername(username);

            if (userOpt.isPresent()) {
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete user: " + username + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        dbService.deleteUser(userOpt.get());
                        JOptionPane.showMessageDialog(this, "User deleted successfully!");
                        populateUserTable();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Failed to delete user: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a user to delete.");
        }
    }

    private void populateUserTable() {
        List<User> users = dbService.getAllUsers();

        String[] columnNames = {"Username", "Password", "Email", "User Type"};
        String[][] data = new String[users.size()][4];

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            data[i][0] = user.getUsername();
            data[i][1] = user.getPassword();
            data[i][2] = user.getEmail();
            data[i][3] = user.getUserType().toString();
        }

        userTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
    }
}
