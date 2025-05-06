package org.example.bearfitness.ui;

import org.example.bearfitness.user.User;
import org.example.bearfitness.data.DBService;
import org.example.bearfitness.user.UserType;
import org.example.bearfitness.ui.ScreenManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SignUpUI extends JPanel {
    private DBService dbService;
    private ScreenManager screenManager;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField emailField;
    private JComboBox<UserType> userTypeCombo;

    public SignUpUI(DBService dbService, ScreenManager screenManager) {
        this.dbService = dbService;
        this.screenManager = screenManager;
        setLayout(new GridBagLayout());

        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        inputPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        inputPanel.add(usernameField);

        inputPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        inputPanel.add(emailField);

        inputPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        inputPanel.add(passwordField);

        inputPanel.add(new JLabel("Confirm Password:"));
        confirmPasswordField = new JPasswordField();
        inputPanel.add(confirmPasswordField);

        inputPanel.add(new JLabel("User Type:"));
        //userTypeCombo = new JComboBox<>(UserType.values());
        userTypeCombo = new JComboBox<>();
        for (UserType type : UserType.values()) {
            if (type != UserType.ADMIN) {
                userTypeCombo.addItem(type);
            }
        }
        inputPanel.add(userTypeCombo);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton signUpButton = new JButton("Sign Up");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(signUpButton);
        buttonPanel.add(cancelButton);

        signUpButton.addActionListener(e -> handleSignUp());
        cancelButton.addActionListener(e -> screenManager.showScreen(ScreenManager.Screen.LOGIN));

        JPanel container = new JPanel(new BorderLayout());
        container.add(inputPanel, BorderLayout.CENTER);
        container.add(buttonPanel, BorderLayout.SOUTH);

        add(container);
    }

    private void handleSignUp() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        UserType userType = (UserType) userTypeCombo.getSelectedItem();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Incomplete", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Mismatch", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 🔥 NEW: Check if username already exists
        if (dbService.findUserByUsername(username).isPresent()) {
            JOptionPane.showMessageDialog(this, "Username already taken. Please choose another.", "Username Taken", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            dbService.createUser(username, password, email, userType);
            JOptionPane.showMessageDialog(this, "Account created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            screenManager.showScreen(ScreenManager.Screen.LOGIN);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to create an account: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}