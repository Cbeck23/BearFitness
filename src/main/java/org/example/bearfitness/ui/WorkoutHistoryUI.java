package org.example.bearfitness.UI;

import org.example.bearfitness.data.DBService;
import org.example.bearfitness.user.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class WorkoutHistoryUI extends JPanel {
    private DBService dbService;
    private User user;
    private JTable table;
    private DefaultTableModel tableModel;

    public WorkoutHistoryUI(DBService dbService, ScreenManager screenManager, User user) {
        this.dbService = dbService;
        this.user = user;

        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Workout History", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Table Setup
        String[] columnNames = {"Date", "Exercise", "Duration", "Description"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Back to Home");
        backButton.addActionListener(e -> screenManager.showScreen(ScreenManager.Screen.USER_HOME));
        add(backButton, BorderLayout.SOUTH);

        loadUserEntries();
    }

    private void loadUserEntries() {
        try {
            List<String[]> entries = dbService.getUserEntries(user.getId());  // Assuming your User has getId()

            tableModel.setRowCount(0);  // Clear existing rows
            for (String[] entry : entries) {
                tableModel.addRow(entry);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load workout history.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
