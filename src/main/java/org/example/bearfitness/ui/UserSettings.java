package org.example.bearfitness.ui;

import com.formdev.flatlaf.FlatDarculaLaf;
import org.example.bearfitness.data.DBService;
import org.example.bearfitness.data.PasswordHash;
import org.example.bearfitness.trainerJuice.Music;
import org.example.bearfitness.user.User;
import org.example.bearfitness.user.UserType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class UserSettings extends JPanel {
    private JPasswordField oldPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    private JLabel exerciseStatLabel;
    private JLabel calorieStatLabel;
    private JLabel sleepStatLabel;
    private JPanel statPanel;
    private JComboBox<String> themeComboBox;
    private JButton logoutButton;
    private JButton backButton;
    private JButton confirmButton;
    private JButton cancelButton;
    private JButton musicButton;

    private final DBService dbService;
    private final ScreenManager screenManager;
    private final User user;
    private final Music musicPlayer = new Music();

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
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;

        // LEFT PANEL (Navigation & Username)
        JPanel leftPanel = new JPanel(new BorderLayout());

//        JLabel usernameLabel = new JLabel(user.getUsername(), SwingConstants.LEFT);
//        usernameLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
//        leftPanel.add(usernameLabel, BorderLayout.WEST);

        backButton = new JButton("Back");
        logoutButton = new JButton("Logout");
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.add(backButton, BorderLayout.NORTH);
        navPanel.add(logoutButton, BorderLayout.SOUTH);
        leftPanel.add(navPanel, BorderLayout.WEST);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 3;
        gbc.weightx = 0.3;
        gbc.weighty = 1.0;
        add(leftPanel, gbc);

        // RIGHT PANEL (Stats, Theme, Password)
        JPanel rightPanel = new JPanel(new GridBagLayout());
        GridBagConstraints rGbc = new GridBagConstraints();
        rGbc.insets = new Insets(10, 10, 10, 10);
        rGbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel usernameLabel = new JLabel("Username: " + user.getUsername(), SwingConstants.CENTER);
        usernameLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        rGbc.gridx = 0;
        rGbc.gridy = 0;
        rGbc.gridwidth = 2;
        rGbc.weightx = 1.0;
        rightPanel.add(usernameLabel, rGbc);

        JLabel titleLabel = new JLabel("Account Statistics", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        rGbc.gridx = 0;
        rGbc.gridy = 1;
        rGbc.gridwidth = 2;
        rGbc.weightx = 1.0;
        rightPanel.add(titleLabel, rGbc);

        // Stat panel
        statPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        exerciseStatLabel = new JLabel();
        calorieStatLabel = new JLabel();
        sleepStatLabel = new JLabel();
        statPanel.add(exerciseStatLabel);
        statPanel.add(calorieStatLabel);
        statPanel.add(sleepStatLabel);
        // If trainer, add music control
        if(user.getUserType() == UserType.TRAINER) {
            JPanel musicPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            musicPanel.add(new JLabel("Music:"));
            musicButton = new JButton("Select & Play");
            musicPanel.add(musicButton);
            statPanel.add(musicPanel);
        }
        rGbc.gridy = 2;
        rightPanel.add(statPanel, rGbc);

        // Theme panel
        JPanel themePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        themeComboBox = new JComboBox<>(new String[]{"Metal", "Dark Mode","Nimbus", "CDE/Motif", "Windows", "Windows Classic"});
        JLabel themeLabel = new JLabel("Theme: ");
        themePanel.add(themeLabel);
        themePanel.add(themeComboBox);


        rGbc.gridy = 3;
        rightPanel.add(themePanel, rGbc);

        // Password panel
        JPanel passwordPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        passwordPanel.setBorder(BorderFactory.createTitledBorder("Change Password"));
        oldPasswordField = new JPasswordField();
        newPasswordField = new JPasswordField();
        confirmPasswordField = new JPasswordField();
        JLabel passwordLabel = new JLabel("Old Password: ");
        passwordPanel.add(passwordLabel);
        passwordPanel.add(oldPasswordField);

        JLabel newPasswordLabel = new JLabel("New Password: ");
        passwordPanel.add(newPasswordLabel);
        passwordPanel.add(newPasswordField);

        JLabel confirmPasswordLabel = new JLabel("Confirm Password: ");
        passwordPanel.add(confirmPasswordLabel);
        passwordPanel.add(confirmPasswordField);
        //add(passwordPanel, BorderLayout.CENTER);

        rGbc.gridy = 4;
        rightPanel.add(passwordPanel, rGbc);

        // Confirm/Cancel button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        confirmButton = new JButton("Confirm");
        cancelButton = new JButton("Cancel");
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);

        rGbc.gridy = 5;
        rightPanel.add(buttonPanel, rGbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 3;
        gbc.weightx = 0.7;
        add(rightPanel, gbc);

        // Admin Button (optional)
        if (user.getUserType() == UserType.ADMIN) {
            JButton adminButton = new JButton("Admin");
            adminButton.addActionListener(e -> screenManager.showScreen(ScreenManager.Screen.ADMINISTRATION));
            rGbc.gridy = 6;
            rightPanel.add(adminButton, rGbc);
        }

        // Optional: scale components on resize
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int width = getWidth();
                int fontSize = Math.max(12, width / 60);
                Font scaledFont = new Font("SansSerif", Font.PLAIN, fontSize);
                for (JComponent comp : new JComponent[]{backButton, logoutButton, confirmButton, cancelButton,
                        titleLabel, exerciseStatLabel, calorieStatLabel, sleepStatLabel,
                        themeLabel, themeComboBox,
                        oldPasswordField, newPasswordField, confirmPasswordField, passwordLabel, newPasswordLabel,
                        confirmPasswordLabel, passwordPanel}) {
                    comp.setFont(scaledFont);
                }
                revalidate();
                repaint();
            }
        });
    }


    private void loadStats() {
//        int exThisWeek = dbService.getExerciseLastWeek(user.getId());
//        int exGoal = user.getGoals().getWeeklyExercises();

        int totalEntries = dbService.countByUserId(user.getId());
        exerciseStatLabel.setText("Total Number of Exercises Logged: " + totalEntries);

        int totalCalories = user.getTotalCaloriesLogged(user.getUserStats());
        calorieStatLabel.setText("Total Number of Calories Logged: " + totalCalories);

        double totalSleep = user.getTotalSleepLogged(user.getUserStats());
        sleepStatLabel.setText("Total Number of Sleep Hours Logged: " + totalSleep);
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

        if (musicButton != null) {
            musicButton.addActionListener(e -> {
                SelectSong selector = new SelectSong();
                selector.setVisible(true);
            });
        }
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
                case "Dark Mode":
                    UIManager.setLookAndFeel("com.formdev.flatlaf.FlatDarculaLaf");
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
