package org.example.bearfitness.ui;

import org.example.bearfitness.data.DBService;
import org.example.bearfitness.data.PasswordHash;
import org.example.bearfitness.ui.ScreenManager;
import org.example.bearfitness.user.User;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginUI extends JPanel {
    private DBService dbService;
    private ScreenManager screenManager;
    private JTextField usernameField;
    private JPasswordField passwordField;
    PasswordHash passwordHash = new PasswordHash();

//    public LoginUI(DBService dbService, ScreenManager screenManager) {
//        this.dbService = dbService;
//        this.screenManager = screenManager;
//        setLayout(new GridBagLayout());
//
//        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
//        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
//
//        inputPanel.add(new JLabel("Username:"));
//        usernameField = new JTextField();
//        inputPanel.add(usernameField);
//
//        inputPanel.add(new JLabel("Password:"));
//        passwordField = new JPasswordField();
//        inputPanel.add(passwordField);
//
//        JButton loginButton = new JButton("Login");
//        loginButton.addActionListener(e -> userLogin());
//        inputPanel.add(loginButton);
//
//        JButton signUpButton = new JButton("Sign Up" );
//        signUpButton.addActionListener(e -> screenManager.showScreen(ScreenManager.Screen.SIGNUP));
//        inputPanel.add(signUpButton);
//
//        add(inputPanel);
//
//        // Add alternate signup handler
//        //signUpButton.addActionListener(this::userSignUp);
//    }

    private void userLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        password = passwordHash.hashPassword(password);

        if (dbService.authenticateUser(username, password) != null) {
            JOptionPane.showMessageDialog(this, "Login successful!");
            SwingUtilities.getWindowAncestor(this).dispose();
            // FIX ME: Later route to correct UI based on user type
            new ScreenManager(dbService, dbService.authenticateUser(username, password)).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    public LoginUI(DBService dbService, ScreenManager screenManager) {
        this.dbService = dbService;
        this.screenManager = screenManager;
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Image above login box
        JLabel imageLabel = new JLabel();
        try {
            ImageIcon originalIcon = new ImageIcon(getClass().getResource("/PlaygroundImage.jpg")); // Adjust path as needed
            Image scaledImage = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImage));
            add(imageLabel, gbc);
        } catch (Exception e) {
            System.err.println("Image not found or failed to load.");
        }

        // Login form
        gbc.gridy = 1;  // Move input panel below image
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        inputPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        inputPanel.add(usernameField);

        inputPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        inputPanel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> userLogin());
        inputPanel.add(loginButton);

        JButton signUpButton = new JButton("Sign Up");
        signUpButton.addActionListener(e -> screenManager.showScreen(ScreenManager.Screen.SIGNUP));
        inputPanel.add(signUpButton);

        add(inputPanel, gbc);
    }


}
