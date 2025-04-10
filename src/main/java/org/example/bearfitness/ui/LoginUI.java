package org.example.bearfitness.ui;

import javax.swing.*;
import java.awt.*;

public class LoginUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton signUpButton;

    public LoginUI() {
        setTitle("Create Exercise");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        inputPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        inputPanel.add(usernameField);

        inputPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        inputPanel.add(passwordField);

        loginButton = new JButton("Login");
        inputPanel.add(loginButton);

        signUpButton = new JButton("Sign Up");
        inputPanel.add(signUpButton);

        add(inputPanel, BorderLayout.CENTER);
        
//        loginButton.addActionListener(this::userLogin);
//        signUpButton.addActionListener(this::userSignUp);
    }
//
//    private void userLogin(ActionEvent e) {
//        String username = usernameField.getText();
//        String password = new String(passwordField.getPassword());
//
//        if (authenticateUser(username, password)) {
//            JOptionPane.showMessageDialog(this, "Login successful!");
//            // Redirect to another UI
//            this.dispose(); // Close the login window
//            new OverallUI().setVisible(true);
//        } else {
//            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    private void userSignUp(ActionEvent e) {
//        //Redirect to create user UI
//        JOptionPane.showMessageDialog(this, "Redirecting to Create User Page...");
//        this.dispose();
//        new SignUpUI().setVisible(true); //Placeholder class
//    }
//
//    private boolean authenticateUser(String username, String password) {
//        return userDatabase.containsKey(username) && userDatabase.get(username).equals(password);
//    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> new LoginUI().setVisible(true));
//    }
}
