package org.example.bearfitness.ui;

import org.example.bearfitness.data.DBService;
import org.example.bearfitness.user.User;
import org.example.bearfitness.user.UserType;

import javax.swing.*;
import java.util.List;
import java.util.Optional;

public class AdminManagementUI extends JPanel {

    JTable userTable;
    DBService dbService;
    ScreenManager screenManager;
    User user;



    AdminManagementUI(DBService dbService, ScreenManager screenManager, User user) {
        userTable = new JTable();
        this.dbService = dbService;
        this.screenManager = screenManager;
        this.user = user;
        JScrollPane userTableScrollPane = new JScrollPane(userTable);
        userTableScrollPane.setViewportView(userTable);
        JButton addUserButton = new JButton("Add User");

        addUserButton.addActionListener(e -> {
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
                dbService.createUser(
                        usernameField.getText(),
                        passwordField.getText(),
                        emailField.getText(),
                        (UserType) userTypeCombo.getSelectedItem()
                );
                populateUserTable(userTable); // Refresh the table
            }
        });

        JButton deleteUserButton = new JButton("Delete User");
        deleteUserButton.addActionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow != -1) {
                String username = userTable.getValueAt(selectedRow, 0).toString();
                Optional<User> userOpt = dbService.findUserByUsername(username);
                if (userOpt.isPresent()) {
                    User userSelected = userOpt.get();
                    dbService.deleteUser(userSelected);
                    populateUserTable(userTable); // Refresh the table
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a user to delete.");
            }
        });
        JButton editUserButton = new JButton("Edit User");
        editUserButton.addActionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow != -1) {
                String username = userTable.getValueAt(selectedRow, 0).toString();
                Optional<User> userOpt = dbService.findUserByUsername(username);

                if (userOpt.isPresent()) {
                    User userSelected = userOpt.get();
                    JTextField usernameField = new JTextField(userSelected.getUsername());
                    JTextField passwordField = new JTextField(userSelected.getPassword());
                    JTextField emailField = new JTextField(userSelected.getEmail());
                    JComboBox<UserType> userTypeCombo = new JComboBox<>(UserType.values());
                    userTypeCombo.setSelectedItem(userSelected.getUserType());

                    Object[] fields = {
                            "Username:", usernameField,
                            "Password:", passwordField,
                            "Email:", emailField,
                            "User Type:", userTypeCombo
                    };

                    int option = JOptionPane.showConfirmDialog(this, fields, "Edit User", JOptionPane.OK_CANCEL_OPTION);

                    if (option == JOptionPane.OK_OPTION) {
                        userSelected.setUsername(usernameField.getText());
                        userSelected.setPassword(passwordField.getText());
                        userSelected.setEmail(emailField.getText());
                        userSelected.setUserType((UserType) userTypeCombo.getSelectedItem());
                        dbService.updateUserData(userSelected);
                        populateUserTable(userTable); // Refresh the table
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a user to edit.");
            }
        });

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(userTableScrollPane);
        add(addUserButton);
        add(editUserButton);
        add(deleteUserButton);

        populateUserTable(userTable);
    }
    private void populateUserTable(JTable userTable) {
        List<User> users = dbService.getAllUsers(); // We'll add this method to DBService below.

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
