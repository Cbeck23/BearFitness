package org.example.bearfitness;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SignUpUI extends JFrame {
  private JTextField usernameField;
  private JPasswordField passwordField;
  private JPasswordField confirmPasswordField;
  private JTextField emailField;
  private JComboBox<UserType> userTypeCombo;
  private JButton signUpButton;
  private JButton cancelButton;

  public SignUpUI() {
    setTitle("Sign Up");
    setSize(400, 300);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    //Input Panel
    JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
    inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

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
    userTypeCombo = new JComboBox<>(UserType.values());
    inputPanel.add(userTypeCombo);

    add(inputPanel, BorderLayout.CENTER);

    //Button Panel
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    signUpButton = new JButton("Sign Up");
    cancelButton = new JButton("Cancel");
    buttonPanel.add(signUpButton);
    buttonPanel.add(cancelButton);
    add(buttonPanel, BorderLayout.SOUTH);

    //Action listeners for buttons
    signUpButton.addActionListener(this::handleSignUp);
    cancelButton.addActionListener(e -> {
      this.dispose();
      new LoginUI().setVisible(true);
    });
  }

  private void handleSignUp(ActionEvent e) {
    String username = usernameField.getText().trim();
    String email = emailField.getText().trim();
    String password = new String(passwordField.getPassword());
    String confirmPassword = new String(confirmPasswordField.getPassword());
    UserType userType = (UserType) userTypeCombo.getSelectedItem();

    //Check that all fields are filled
    if(username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
      JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Incomplete", JOptionPane.ERROR_MESSAGE);
      return;
    }
    //Check that password and confirmed password match
    if(!password.equals(confirmPassword)) {
      JOptionPane.showMessageDialog(this, "Passwords do not match.", "Mismatch", JOptionPane.ERROR_MESSAGE);
      return;
    }

    //Create a new user using the UserCreator factory method
    User newUser;
    try {
      newUser = UserCreator.createUser(userType, username, password, email);
    } catch(IllegalArgumentException ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }

    try {
      //FIX ME: replace temporary success statement with actually saving account to database
      JOptionPane.showMessageDialog(this, "Account created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

      //Redirect the user to the login UI
      this.dispose();
      new LoginUI().setVisible(true);
    } catch(Exception ex) {
      JOptionPane.showMessageDialog(this, "Failed to create an account: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  //Main method for standalone testing
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new SignUpUI().setVisible(true));
  }
}
