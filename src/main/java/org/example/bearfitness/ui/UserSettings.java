package org.example.bearfitness.ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.Properties;

import org.example.bearfitness.data.DBService;
import org.example.bearfitness.fitness.WorkoutEntry;
import org.example.bearfitness.ui.ScreenManager;
import org.example.bearfitness.user.User;
import org.example.bearfitness.user.UserType;
import org.jdatepicker.impl.DateComponentFormatter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

public class UserSettings {
    private JPanel contentPane;
    private JButton logoutButton;
    private JTextField oldPassword;
    private JTextField newPassword;
    private JTextField confirmPassword;
    private JButton cancelButton;
    private JButton backButton1;
    private JButton confirmButton;
    private JLabel userDisplay;
    private JLabel accInfo;
    private JPanel statDisplay;
    private JLabel resetLabel;
    private JLabel oldPasswordLabel;
    private JLabel newPasswordLabel;
    private JLabel confirmPasswordLabel;

    private ScreenManager screenManager;

    private DBService dbService;
    private User user;
    private UserUI parentUI;

    public UserSettings(DBService dbService, ScreenManager screenManager, User user, UserUI parentUI){
        this.parentUI = parentUI;
        this.dbService = dbService;
        this.user = user;
        JFrame frame = new JFrame("Settings");

        frame.setContentPane(this.contentPane);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        //frame.setVisible(true);

        logoutButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(contentPane, "Logging Out . .","Have a Good Day!", JOptionPane.INFORMATION_MESSAGE);
            screenManager.showScreen(ScreenManager.Screen.LOGIN);
            //TO DO: unassign current user after logout (actually make this work)
        });

        backButton1.addActionListener(e -> {
            screenManager.showScreen(ScreenManager.Screen.USER_HOME, user);
        });

        if(user.getUserType() == UserType.ADMIN){
            JButton userManagementButton = new JButton("Manage Users");
            frame.getContentPane().add(userManagementButton);
            userManagementButton.addActionListener(e -> screenManager.showScreen(ScreenManager.Screen.ADMINISTRATION));
        }
    }

//    public static void main(String[] args) {
//        JFrame frame = new JFrame("My Form");
//        UserSettings form = new UserSettings();
//
//        frame.setContentPane(form.contentPane);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.pack();
//        frame.setVisible(true);
//    }
    public JPanel getPanel() {
        return contentPane;
    }

    public void setScreenManager(ScreenManager screenManager) {
        this.screenManager = screenManager;
    }

//    public static void show(DBService dbService, JFrame parent, User user, ScreenManager screenManager) {
//        this.parentUI = parentUI;
//        this.dbService = dbService;
//        this.user = user;
//
//        //screenManager.showScreen(ScreenManager.Screen.USER_HOME, user);
//    }
}
