package org.example.bearfitness.ui;

import org.example.bearfitness.data.DBService;
import org.example.bearfitness.data.PasswordHash;
import org.example.bearfitness.user.User;
import org.example.bearfitness.user.UserType;

import javax.swing.*;
import java.awt.*;

public class UserSettings extends JPanel {
    private JPasswordField oldPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    private JLabel exerciseStatLabel;
    private JLabel calorieStatLabel;
    private JPanel statPanel;
    private JComboBox<String> themeComboBox;
    private JButton logoutButton;
    private JButton backButton;
    private JButton confirmButton;
    private JButton cancelButton;

    private final DBService dbService;
    private final ScreenManager screenManager;
    private final User user;

    public UserSettings(DBService dbService, ScreenManager screenManager, User user) {
        this.dbService = dbService;
        this.screenManager = screenManager;
        this.user = user;
        setLayout(new BorderLayout(10, 10));
        initComponents();
        loadStats();
        registerListeners();
    }

    private void initComponents() {
        // Stats and theme at top
        statPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        exerciseStatLabel = new JLabel();
        calorieStatLabel = new JLabel();
        JPanel themePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        themePanel.add(new JLabel("Theme:"));
        themeComboBox = new JComboBox<>(new String[]{"Metal", "Nimbus", "CDE/Motif", "Windows", "Windows Classic"});
        themePanel.add(themeComboBox);
        statPanel.add(exerciseStatLabel);
        statPanel.add(calorieStatLabel);
        statPanel.add(themePanel);
        add(statPanel, BorderLayout.NORTH);

        // Password change form in center
        JPanel passwordPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        passwordPanel.setBorder(BorderFactory.createTitledBorder("Change Password"));
        oldPasswordField = new JPasswordField();
        newPasswordField = new JPasswordField();
        confirmPasswordField = new JPasswordField();
        passwordPanel.add(new JLabel("Old Password:"));
        passwordPanel.add(oldPasswordField);
        passwordPanel.add(new JLabel("New Password:"));
        passwordPanel.add(newPasswordField);
        passwordPanel.add(new JLabel("Confirm Password:"));
        passwordPanel.add(confirmPasswordField);
        add(passwordPanel, BorderLayout.CENTER);

        // Navigate buttons at bottom
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        backButton = new JButton("Back");
        logoutButton = new JButton("Logout");
        cancelButton = new JButton("Cancel");
        confirmButton = new JButton("Confirm");

        if(user.getUserType() == UserType.ADMIN){
            JButton adminButton = new JButton("Admin");
            adminButton.addActionListener(e -> screenManager.showScreen(ScreenManager.Screen.ADMINISTRATION));
            buttonPanel.add(adminButton);
        }
        buttonPanel.add(backButton);
        buttonPanel.add(logoutButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(confirmButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadStats() {
        int exThisWeek = dbService.getExerciseLastWeek(user.getId());
        int exGoal = user.getGoals().getWeeklyExMinutes();
        exerciseStatLabel.setText("Exercise This Week: " + exThisWeek + " / " + exGoal + " min");

        int calThisWeek = user.getGoals().getWeeklyExMinutes();
        int calGoal = user.getGoals().getGoalCalories();
        calorieStatLabel.setText("Calories This Week: " + calThisWeek + " / " + calGoal);
    }

    private void registerListeners() {
        themeComboBox.addActionListener(e -> {
            applyTheme((String) themeComboBox.getSelectedItem());
        });

        confirmButton.addActionListener(e -> handleChangePassword());
        cancelButton.addActionListener(e -> clearPasswordFields());

        logoutButton.addActionListener(e -> {
            screenManager.showScreen(ScreenManager.Screen.LOGIN);
            JOptionPane.showMessageDialog(this, user.getUsername() + " has logged out.", "Logout", JOptionPane.INFORMATION_MESSAGE);
        });

        backButton.addActionListener(e -> {
            if(user.getUserType() == UserType.TRAINER){
                screenManager.showScreen(ScreenManager.Screen.TRAINER);
            }
            else {
                screenManager.showScreen(ScreenManager.Screen.USER_HOME, user);
            }
        });
    }

    private void handleChangePassword() {
        String oldPwd = new String(oldPasswordField.getPassword());
        String newPwd = new String(newPasswordField.getPassword());
        String confirmPwd = new String(confirmPasswordField.getPassword());

        if(!PasswordHash.hashPassword(oldPwd).equals(user.getPassword())) {
            JOptionPane.showMessageDialog(this, "Old Password Incorrect.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(!newPwd.equals(confirmPwd)) {
            JOptionPane.showMessageDialog(this, "New passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        user.setPassword(PasswordHash.hashPassword(newPwd));
        dbService.updateUserData(user);
        JOptionPane.showMessageDialog(this, "Password Updated.", "Success", JOptionPane.INFORMATION_MESSAGE);
        clearPasswordFields();
    }

    private void clearPasswordFields() {
        oldPasswordField.setText("");
        newPasswordField.setText("");
        confirmPasswordField.setText("");
    }

    private void applyTheme(String themeName) {
        try {
            switch(themeName) {
                case "Nimbus":
                    UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                    break;
                case "CDE/Motif":
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
                    break;
                case "Windows":
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                    break;
                case "Windows Classic":
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
                    break;
                default:
                    UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            }
            SwingUtilities.updateComponentTreeUI(SwingUtilities.getWindowAncestor(this));
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to apply theme.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Expose this panel for CardLayout
     */
    public JPanel getPanel() {
        return this;
    }
}
