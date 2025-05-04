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

public class TraineesList extends JPanel {
    private DBService dbService;
    private User user;
    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private List<WorkoutEntry> userEntries;
    private TrainerUI parentUI;


    public TraineesList(DBService dbService, ScreenManager screenManager, User user, TrainerUI parentUI){
        this.parentUI = parentUI;
        this.dbService = dbService;
        this.user = user;
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Trainees", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

        String[] columnNames = {"Trainee Name", "Email Contact"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  // Make table non-editable directly
            }
        };

        loadTrainees();

        table = new JTable(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadTrainees(){
        tableModel.setRowCount(0);

        try {
            //userEntries = dbService.getAllUsers();

            for (User user : this.dbService.getAllUsers()) {
                tableModel.addRow(new Object[]{
                        user.getUsername(),
                        user.getEmail(),
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load trainees", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
