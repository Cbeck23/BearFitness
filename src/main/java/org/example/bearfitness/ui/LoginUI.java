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
        setLayout(new BorderLayout());

        // === Center Wrapper Panel ===
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;

        // === Vertical Panel: Image + Input Fields ===
        JPanel verticalPanel = new JPanel();
        verticalPanel.setLayout(new BoxLayout(verticalPanel, BoxLayout.Y_AXIS));

        // Logo Image
        JLabel imageLabel = new JLabel();
        try {
            ImageIcon originalIcon = new ImageIcon(getClass().getResource("/PlaygroundImage.jpg"));
            Image scaledImage = originalIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImage));
            imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        } catch (Exception e) {
            System.err.println("Image not found or failed to load.");
        }
        verticalPanel.add(imageLabel);

        verticalPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Space between image and form

        // Input Panel
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

        verticalPanel.add(inputPanel);

        centerWrapper.add(verticalPanel, gbc);
        add(centerWrapper, BorderLayout.CENTER);

        // === Bottom Close Button ===
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> System.exit(0));
        bottomPanel.add(closeButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }




}
