package org.example.bearfitness.ui;

import org.example.bearfitness.data.DBService;
//import org.example.bearfitness.data.PasswordHash;
import org.example.bearfitness.fitness.ExerciseClass;
import org.example.bearfitness.fitness.ExercisePlan;
import org.example.bearfitness.user.User;

import javax.swing.*;
import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
import java.util.List;

public class SearchForClassesAndPlansUI extends JPanel {
    private User user;
    private DBService dbService;
    private ScreenManager screenManager;

    // Exercise Classes
    private DefaultListModel<String> subscribedClassesModel;
    private DefaultListModel<String> searchClassesResultModel;
    private JTextField searchClassesField;
    private JList<String> subscribedClassesList;

    // Exercise Plans
    private DefaultListModel<String> subscribedPlansModel;
    private DefaultListModel<String> searchPlansResultModel;
    private JTextField searchPlansField;
    private JList<String> subscribedPlansList;

    public SearchForClassesAndPlansUI(DBService dbService, ScreenManager screenManager, User user) {
        this.user = user;
        this.dbService = dbService;
        this.screenManager = screenManager;

        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("Back to Home");
        backButton.addActionListener(e -> screenManager.showScreen(ScreenManager.Screen.USER_HOME, user));
        topPanel.add(backButton);
        add(topPanel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("Exercise Classes", createClassPanel());
        tabbedPane.add("Exercise Plans", createPlanPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createClassPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        subscribedClassesModel = new DefaultListModel<>();
        subscribedClassesList = new JList<>(subscribedClassesModel);
        JScrollPane subscribedScrollPane = new JScrollPane(subscribedClassesList);
        populateSubscribedClasses();

        JButton unsubscribeButton = new JButton("Unsubscribe Selected Class");
        unsubscribeButton.addActionListener(e -> unsubscribeFromClass(subscribedClassesList.getSelectedValue()));

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Your Subscribed Classes"));
        leftPanel.add(subscribedScrollPane, BorderLayout.CENTER);
        leftPanel.add(unsubscribeButton, BorderLayout.SOUTH);

        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        searchClassesField = new JTextField(20);
        JButton searchButton = new JButton("Search Classes");
        searchButton.addActionListener(e -> performClassSearch());
        searchPanel.add(searchClassesField, BorderLayout.CENTER);
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> {
            searchClassesField.setText("");
            performClassSearch();
        });
        searchPanel.add(clearButton, BorderLayout.WEST);
        searchPanel.add(searchButton, BorderLayout.EAST);

        searchClassesResultModel = new DefaultListModel<>();
        JList<String> searchResultList = new JList<>(searchClassesResultModel);
        JScrollPane searchResultScrollPane = new JScrollPane(searchResultList);

        JButton subscribeButton = new JButton("Subscribe to Selected Class");
        subscribeButton.addActionListener(e -> subscribeToClass(searchResultList.getSelectedValue()));

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Search Results"));
        rightPanel.add(searchPanel, BorderLayout.NORTH);
        rightPanel.add(searchResultScrollPane, BorderLayout.CENTER);
        rightPanel.add(subscribeButton, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(400);

        panel.add(splitPane, BorderLayout.CENTER);
        performClassSearch();
        return panel;
    }

    private JPanel createPlanPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        subscribedPlansModel = new DefaultListModel<>();
        subscribedPlansList = new JList<>(subscribedPlansModel);
        JScrollPane subscribedScrollPane = new JScrollPane(subscribedPlansList);
        populateSubscribedPlans();

        JButton unsubscribeButton = new JButton("Unsubscribe Selected Plan");
        unsubscribeButton.addActionListener(e -> unsubscribeFromPlan(subscribedPlansList.getSelectedValue()));

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Your Subscribed Plans"));
        leftPanel.add(subscribedScrollPane, BorderLayout.CENTER);
        leftPanel.add(unsubscribeButton, BorderLayout.SOUTH);

        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        searchPlansField = new JTextField(20);
        JButton searchButton = new JButton("Search Plans");
        searchButton.addActionListener(e -> performPlanSearch());
        searchPanel.add(searchPlansField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> {
            searchPlansField.setText("");
            performPlanSearch();
        });
        searchPanel.add(clearButton, BorderLayout.WEST);

        searchPlansResultModel = new DefaultListModel<>();
        JList<String> searchResultList = new JList<>(searchPlansResultModel);
        JScrollPane searchResultScrollPane = new JScrollPane(searchResultList);

        JButton subscribeButton = new JButton("Subscribe to Selected Plan");
        subscribeButton.addActionListener(e -> subscribeToPlan(searchResultList.getSelectedValue()));

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Search Results"));
        rightPanel.add(searchPanel, BorderLayout.NORTH);
        rightPanel.add(searchResultScrollPane, BorderLayout.CENTER);
        rightPanel.add(subscribeButton, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(400);

        panel.add(splitPane, BorderLayout.CENTER);
        performPlanSearch();
        return panel;
    }

    private void populateSubscribedClasses() {
        subscribedClassesModel.clear();
        List<ExerciseClass> classes = user.getSubscribedClasses();
        for (ExerciseClass ec : classes) {
            subscribedClassesModel.addElement(ec.getName());
        }
    }


    private void populateSubscribedPlans() {
        subscribedPlansModel.clear();
        List<ExercisePlan> plans = user.getSubscribedPlans();
        for (ExercisePlan ep : plans) {
            subscribedPlansModel.addElement(ep.getPlanName());
        }
    }

    private void performClassSearch() {
        searchClassesResultModel.clear();
        String keyword = searchClassesField.getText().trim().toLowerCase();
        List<String> allClasses = dbService.getAllExerciseClassNamesWithTrainer();

        for (String className : allClasses) {
            if (keyword.isEmpty() || className.toLowerCase().contains(keyword)) {
                searchClassesResultModel.addElement(className);
            }
        }
    }

    private void performPlanSearch() {
        searchPlansResultModel.clear();
        String keyword = searchPlansField.getText().trim().toLowerCase();
        List<String> allPlans = dbService.getAllExercisePlans();

        for (String planName : allPlans) {
            if (keyword.isEmpty() || planName.toLowerCase().contains(keyword)) {
                searchPlansResultModel.addElement(planName);
            }
        }
    }

    private void subscribeToClass(String name) {
        if (name == null) return;

        String actualClassName;
        try {
            actualClassName = name.split(" : ")[1].split(" - Hosted by")[0].trim();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to extract class name from selected item.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<ExerciseClass> classes = dbService.findExerciseClassByName(actualClassName);
        if (!classes.isEmpty()) {
            ExerciseClass selected = classes.get(0);

            // 🔥 Refresh user from DB first
            User refreshedUser = dbService.findUserByUsername(user.getUsername()).orElse(user);

            boolean exists = refreshedUser.getSubscribedClasses().stream()
                    .anyMatch(c -> c.getId().equals(selected.getId()));

            if (!exists) {
                refreshedUser.addClass(selected);
                dbService.updateUserData(refreshedUser);
                JOptionPane.showMessageDialog(this, "Successfully subscribed to class: " + name);
                // Update local reference
                this.user = refreshedUser;
                populateSubscribedClasses();
            } else {
                JOptionPane.showMessageDialog(this,
                        "You are already subscribed to the class \"" + name + "\".",
                        "Duplicate Subscription",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No matching class found for \"" + name + "\".", "Not Found", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void subscribeToPlan(String planString) {
        if (planString == null) return;

        String planName;
        try {
            planName = planString.split("Plan Name:")[1].split("\n")[0].trim();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to extract plan name from selected item.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<ExercisePlan> plans = dbService.findExercisePlanByName(planName);
        if (!plans.isEmpty()) {
            ExercisePlan selected = plans.get(0);

            // 🔥 Refresh the user from DB before checking subscription
            User refreshedUser = dbService.findUserByUsername(user.getUsername()).orElse(user);

            boolean exists = refreshedUser.getSubscribedPlans().stream()
                    .anyMatch(p -> p.getId().equals(selected.getId()));

            if (!exists) {
                refreshedUser.addPlan(selected);
                dbService.updateUserData(refreshedUser);
                JOptionPane.showMessageDialog(this, "Successfully subscribed to plan: " + planName);
                // Update local reference too
                this.user = refreshedUser;
                populateSubscribedPlans();
            } else {
                JOptionPane.showMessageDialog(this,
                        "You are already subscribed to the plan \"" + planName + "\".",
                        "Duplicate Subscription",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No matching plan found for \"" + planName + "\".", "Not Found", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void unsubscribeFromClass(String name) {
        if (name == null) return;
        ExerciseClass toRemove = user.getSubscribedClasses().stream().filter(c -> c.getName().equals(name)).findFirst().orElse(null);
        if (toRemove != null) {
            user.getSubscribedClasses().remove(toRemove);
            dbService.updateUserData(user);
            JOptionPane.showMessageDialog(this, "Unsubscribed from class: " + name);
            populateSubscribedClasses();
        }
    }

    private void unsubscribeFromPlan(String name) {
        if (name == null) return;
        ExercisePlan toRemove = user.getSubscribedPlans().stream().filter(p -> p.getPlanName().equals(name)).findFirst().orElse(null);
        if (toRemove != null) {
            user.getSubscribedPlans().remove(toRemove);
            dbService.updateUserData(user);
            JOptionPane.showMessageDialog(this, "Unsubscribed from plan: " + name);
            populateSubscribedPlans();
        }
    }
}
