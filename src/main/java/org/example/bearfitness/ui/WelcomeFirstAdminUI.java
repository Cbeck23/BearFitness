package org.example.bearfitness.ui;

import org.example.bearfitness.data.DBService;
import org.example.bearfitness.user.UserType;

import javax.swing.*;
import java.awt.*;

public class WelcomeFirstAdminUI extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField emailField;
    private DBService dbService;
    private JFrame parentFrame;

    public WelcomeFirstAdminUI(DBService dbService, JFrame parentFrame) {
        this.dbService = dbService;
        this.parentFrame = parentFrame;
        setLayout(new GridLayout(5, 2, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(new JLabel("Username:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Email:"));
        emailField = new JTextField();
        add(emailField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        add(new JLabel("Confirm Password:"));
        confirmPasswordField = new JPasswordField();
        add(confirmPasswordField);

        JButton createButton = new JButton("Create Admin User");
        createButton.addActionListener(e -> handleCreateAdmin());
        add(createButton);
    }

    private void handleCreateAdmin() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Incomplete", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Mismatch", JOptionPane.ERROR_MESSAGE);
            return;
        }


        if (dbService.findUserByUsername(username).isPresent()) {
            JOptionPane.showMessageDialog(this, "Username already taken. Please choose another.", "Username Taken", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            dbService.createUser(username, password, email, UserType.ADMIN); // Forced Admin creation
            JOptionPane.showMessageDialog(this, "Admin user created successfully! Please login.");
            parentFrame.dispose(); // ✅ Close the frame only AFTER success
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to create Admin user: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
