package org.example.bearfitness.ui;

import javax.swing.*;
import java.awt.*;


public class FitnessHistory extends JFrame {
    private DefaultListModel<String> workoutListModel;
    private JList<String> workoutList;
    static JButton addWorkoutButton;
    static JLabel workoutLabel;


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

        setLayout(new BorderLayout());
        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        addWorkoutButton = new JButton("Add Workout");
        addWorkoutButton.addActionListener(e -> {
            String newWorkout = JOptionPane.showInputDialog(this, "Enter new workout:");
            String date = JOptionPane.showInputDialog(this, "Enter date:");
            String time = JOptionPane.showInputDialog(this, "Enter duration:");
            if (newWorkout != null && !newWorkout.trim().isEmpty()) {
                addWorkoutEntry(date.trim() + " -- " + newWorkout.trim() + " -- " + time.trim());
            }
        });

        add(addWorkoutButton, BorderLayout.SOUTH);
    }

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
