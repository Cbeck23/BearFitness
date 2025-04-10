package org.example.bearfitness;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class FitnessHistory extends JFrame {
    private DefaultListModel<String> workoutListModel;
    private JList<String> workoutList;

    public FitnessHistory() {
        setTitle("Fitness History");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // center the window

        JLabel titleLabel = new JLabel("Workout History");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        workoutListModel = new DefaultListModel<>();
        workoutList = new JList<>(workoutListModel);
        workoutList.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(workoutList);

        // Example data - replace!!
        addWorkoutEntry("2025-04-09 - Upper Body - 45 mins");
        addWorkoutEntry("2025-04-08 - Cardio - 30 mins");
        addWorkoutEntry("2025-04-06 - Leg Day - 50 mins");

        setLayout(new BorderLayout());
        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    // Add workout entries
    public void addWorkoutEntry(String entry) {
        workoutListModel.addElement(entry);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FitnessHistory historyPage = new FitnessHistory();
            historyPage.setVisible(true);
        });
    }
}
