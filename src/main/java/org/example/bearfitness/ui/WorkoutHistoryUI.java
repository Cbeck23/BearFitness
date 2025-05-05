package org.example.bearfitness.ui;

import org.example.bearfitness.data.DBService;
import org.example.bearfitness.fitness.WorkoutEntry;
import org.example.bearfitness.user.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class WorkoutHistoryUI extends JPanel {
    private DBService dbService;
    private User user;
    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private List<WorkoutEntry> userEntries;
    private UserUI parentUI;

    public WorkoutHistoryUI(DBService dbService, ScreenManager screenManager, User user, UserUI parentUI) {
        this.parentUI = parentUI;
        this.dbService = dbService;
        this.user = user;
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Workout History", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

        String[] columnNames = {"Date", "Exercise", "Duration (min)", "Description"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  // Make table non-editable directly
            }
        };

        table = new JTable(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton addWorkout = new JButton("Add New Workout Entry");
        addWorkout.addActionListener(e -> AddWorkoutUI.show(
                dbService,
                (JFrame) SwingUtilities.getWindowAncestor(this),
                user,
                screenManager,
                parentUI  // Pass to trigger refresh
        ));
        JButton editButton = new JButton("Edit Selected");
        JButton deleteButton = new JButton("Delete Selected");
        JButton filterButton = new JButton("Filter");
        JButton clearFilterButton = new JButton("Clear Filter");
        //JButton backButton = new JButton("Back to Home");

        buttonPanel.add(addWorkout);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(filterButton);
        buttonPanel.add(clearFilterButton);
        //buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        loadUserEntries();

        editButton.addActionListener(e -> editSelectedEntry(this.parentUI));
        deleteButton.addActionListener(e -> deleteSelectedEntry(this.parentUI));
        filterButton.addActionListener(e -> applyFilter());
        clearFilterButton.addActionListener(e -> sorter.setRowFilter(null));
        //backButton.addActionListener(e -> screenManager.showScreen(ScreenManager.Screen.USER_HOME));
    }

    private void loadUserEntries() {
        tableModel.setRowCount(0);
        try {
            userEntries = dbService.getUserEntries(user.getId());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            for (WorkoutEntry entry : userEntries) {
                tableModel.addRow(new Object[]{
                        entry.getDate().format(formatter),
                        entry.getExerciseType().toString(),
                        entry.getDuration(),
                        entry.getDescription()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load workout history.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void editSelectedEntry(UserUI parentUI) {
        int viewRow = table.getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an entry to edit.");
            return;
        }

        int modelRow = table.convertRowIndexToModel(viewRow);
        WorkoutEntry entry = userEntries.get(modelRow);

        String newDescription = JOptionPane.showInputDialog(this, "Edit Description:", entry.getDescription());
        if (newDescription != null && !newDescription.trim().isEmpty()) {
            entry.setDescription(newDescription.trim());

            try {
                dbService.updateUserWorkoutEntry(user, entry);
                tableModel.setValueAt(newDescription.trim(), modelRow, 3);
                JOptionPane.showMessageDialog(this, "Entry updated successfully!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Failed to update entry in the database.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
        if (parentUI != null) {
            parentUI.refresh();
            parentUI.updateExerciseUI();
        }
    }


    private void deleteSelectedEntry(UserUI parentUI) {
        int viewRow = table.getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an entry to delete.");
            return;
        }

        int modelRow = table.convertRowIndexToModel(viewRow);
        WorkoutEntry entry = userEntries.get(modelRow);

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this entry?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                dbService.deleteUserWorkoutEntry(user, entry);
                userEntries.remove(modelRow);
                tableModel.removeRow(modelRow);
                JOptionPane.showMessageDialog(this, "Entry deleted successfully!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Failed to delete entry from the database.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
        if (parentUI != null) {
            parentUI.refresh();
            parentUI.updateExerciseUI();
        }
    }


    private void applyFilter() {
        String keyword = JOptionPane.showInputDialog(this, "Filter by Exercise Type or Description:");
        if (keyword != null && !keyword.trim().isEmpty()) {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + keyword));
        }
    }
    public void refresh() {
        tableModel.setRowCount(0);  // Clear existing rows
        loadUserEntries();         // Reload from DB
    }
}
