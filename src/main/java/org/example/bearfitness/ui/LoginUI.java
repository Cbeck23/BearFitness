package org.example.bearfitness.ui;

import org.example.bearfitness.data.DBService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton signUpButton;

    private static DBService dbService;


    public LoginUI(DBService dbService) {
        this.dbService = dbService;
        System.out.println("Initializing LoginUI");
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
        
        loginButton.addActionListener(this::userLogin);
        signUpButton.addActionListener(this::userSignUp);
    }

    private void userLogin(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (dbService.authenticateUser(username, password) != null) {
            JOptionPane.showMessageDialog(this, "Login successful!");
            this.dispose();
            //FIX ME
            // this has to be false... don't change it!
            new TrainerUI(dbService).setVisible(false); //trainer ui for now, later switch to basic ui/whatever ui for user type
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void userSignUp(ActionEvent e) {
        //Redirect to create user UI
        JOptionPane.showMessageDialog(this, "Redirecting to Create User Page...");
        this.dispose();
        new SignUpUI(dbService).setVisible(true);
    }

//    private boolean authenticateUser(String username, String password) {
//        if(authenticateUser(username,password) != null){
//            return true;
//        };
//    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> new LoginUI(dbService).setVisible(true));
//    }
}
