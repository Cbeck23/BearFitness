package org.example.bearfitness.ui;

import org.example.bearfitness.data.DBService;
import org.example.bearfitness.fitness.ExerciseClass;
import org.example.bearfitness.fitness.WorkoutEntry;
import org.example.bearfitness.user.User;
import org.hibernate.jdbc.Work;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class AttendClassUI extends JPanel {
    private DBService dbService;
    private User user;
    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private List<ExerciseClass> subscribedClasses;
    private UserUI parentUI;
    LocalDate date;

    public AttendClassUI(DBService dbService, User user, UserUI parentUI) {

        this.dbService = dbService;
        this.user = user;
        this.parentUI = parentUI;

        setLayout(new BorderLayout());
        JLabel title = new JLabel(("Attend Class"), SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        String[] columnNames = {"Class", "Fitness Level"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  // Make table non-editable directly
            }
        };

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton attendButton = new JButton("Attend Class");
        attendButton.addActionListener(e -> markAttendance());
        JButton refreshButton = new JButton("Refresh Classes");
        refreshButton.addActionListener(e -> reload());

        buttonPanel.add(refreshButton);
        buttonPanel.add(attendButton);
        add(buttonPanel, BorderLayout.SOUTH);

        table = new JTable(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        loadUserSubscribedClasses();

    }

    private void loadUserSubscribedClasses() {
        tableModel.setRowCount(0);
        date = LocalDate.now();
        try {
            // 1. Get the user's subscribed classes
            subscribedClasses = user.getSubscribedClasses().stream()
                    .filter(exerciseClass -> exerciseClass.getDate().equals(date))
                    .toList(); // Java 16+; otherwise use .collect(Collectors.toList())

            // 2. Display them
            for (ExerciseClass entry : subscribedClasses) {
                tableModel.addRow(new Object[]{
                        entry.getName(),
                        entry.getFitnessLevel(),
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load your classes.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }


    private void markAttendance() {
        int viewRow = table.getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a class to attend", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String className = tableModel.getValueAt(viewRow, 0).toString();

        ExerciseClass selectedClass = null;
        for (ExerciseClass exerciseClass : subscribedClasses) {
            if (exerciseClass.getName().equals(className)) {
                selectedClass = exerciseClass;
                break;
            }
        }

        if (selectedClass == null) {
            JOptionPane.showMessageDialog(this, "Selected class not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        WorkoutEntry entry = new WorkoutEntry(selectedClass);

        if (dbService.getUserEntries(user.getId()).contains(entry)) {
            JOptionPane.showMessageDialog(this, "You have already attended this class!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                dbService.createUserWorkoutEntry(user, entry);
                JOptionPane.showMessageDialog(this, "Entry added successfully!");

                parentUI.refresh();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Failed to update entry in the database.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    public void reload() {
        this.user = dbService.findUserByUsername(user.getUsername()).orElse(user);
        loadUserSubscribedClasses();
    }

}
