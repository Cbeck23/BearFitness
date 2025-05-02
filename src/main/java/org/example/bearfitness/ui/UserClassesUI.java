package org.example.bearfitness.ui;

import org.example.bearfitness.data.DBService;
import org.example.bearfitness.fitness.ExercisePlan;
import org.example.bearfitness.user.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class UserClassesUI extends JPanel {
    private User user;
    private DBService dbService;
    private ScreenManager screenManager;

    private DefaultListModel<String> subscribedModel;
    private DefaultListModel<String> searchResultModel;
    private JTextField searchField;
    private JList<String> subscribedList;

    public UserClassesUI(User user, DBService dbService, ScreenManager screenManager) {
        this.user = user;
        this.dbService = dbService;
        this.screenManager = screenManager;

        setLayout(new BorderLayout(10, 10));

        // Subscribed classes panel
        subscribedModel = new DefaultListModel<>();
        subscribedList = new JList<>(subscribedModel);
        JScrollPane subscribedScrollPane = new JScrollPane(subscribedList);
        populateSubscribedPlans();

        JPanel subscribedPanel = new JPanel(new BorderLayout());
        subscribedPanel.setBorder(BorderFactory.createTitledBorder("Your Subscribed Classes"));
        subscribedPanel.add(subscribedScrollPane, BorderLayout.CENTER);

        JButton unsubscribeButton = new JButton("Unsubscribe Selected Plan");
        subscribedPanel.add(unsubscribeButton, BorderLayout.SOUTH);

        // Search panel
        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        searchField = new JTextField();
        JButton searchButton = new JButton("Search");

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        // Search results panel
        searchResultModel = new DefaultListModel<>();
        JList<String> searchResultList = new JList<>(searchResultModel);
        JScrollPane searchResultScrollPane = new JScrollPane(searchResultList);

        JPanel resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.setBorder(BorderFactory.createTitledBorder("Search Results"));
        resultsPanel.add(searchResultScrollPane, BorderLayout.CENTER);

        JButton subscribeButton = new JButton("Subscribe to Selected Plan");

        resultsPanel.add(subscribeButton, BorderLayout.SOUTH);

        // Main layout
        add(subscribedPanel, BorderLayout.NORTH);
        add(searchPanel, BorderLayout.CENTER);
        add(resultsPanel, BorderLayout.SOUTH);

        // Button actions
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });

        subscribeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedPlanName = searchResultList.getSelectedValue();
                if (selectedPlanName != null) {
                    subscribeToPlan(selectedPlanName);
                } else {
                    JOptionPane.showMessageDialog(UserClassesUI.this, "Please select a plan to subscribe.", "No Plan Selected", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        unsubscribeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedPlanName = subscribedList.getSelectedValue();
                if (selectedPlanName != null) {
                    unsubscribeFromPlan(selectedPlanName);
                } else {
                    JOptionPane.showMessageDialog(UserClassesUI.this, "Please select a plan to unsubscribe.", "No Plan Selected", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    private void populateSubscribedPlans() {
        subscribedModel.clear();
        List<ExercisePlan> plans = user.getSubscribedPlans();
        for (ExercisePlan plan : plans) {
            subscribedModel.addElement(plan.getPlanName());
        }
    }

    private void performSearch() {
        searchResultModel.clear();
        String keyword = searchField.getText().trim();

        if (!keyword.isEmpty()) {
            List<String> plans = dbService.getAllPlans();
            for (String planName : plans) {
                if (planName.toLowerCase().contains(keyword.toLowerCase())) {
                    searchResultModel.addElement(planName);
                }
            }
        }
    }

    private void subscribeToPlan(String planName) {
        List<ExercisePlan> matchingPlans = dbService.findExercisePlanByName(planName);

        if (!matchingPlans.isEmpty()) {
            ExercisePlan selectedPlan = matchingPlans.get(0);

            boolean alreadySubscribed = user.getSubscribedPlans().stream()
                    .anyMatch(plan -> plan.getId().equals(selectedPlan.getId()));

            if (!alreadySubscribed) {
                user.addPlan(selectedPlan);
                dbService.updateUserData(user);
                JOptionPane.showMessageDialog(this, "Successfully subscribed to: " + planName);
                populateSubscribedPlans();
            } else {
                JOptionPane.showMessageDialog(this, "You are already subscribed to this plan.", "Duplicate Subscription", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Plan not found in database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void unsubscribeFromPlan(String planName) {
        ExercisePlan planToRemove = user.getSubscribedPlans().stream()
                .filter(plan -> plan.getPlanName().equals(planName))
                .findFirst()
                .orElse(null);

        if (planToRemove != null) {
            user.getSubscribedPlans().remove(planToRemove);
            dbService.updateUserData(user);
            JOptionPane.showMessageDialog(this, "Successfully unsubscribed from: " + planName);
            populateSubscribedPlans();
        } else {
            JOptionPane.showMessageDialog(this, "Plan not found in your subscriptions.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
