package org.example.bearfitness.ui;

import org.example.bearfitness.data.DBService;
import org.example.bearfitness.fitness.WorkoutEntry;
import org.example.bearfitness.user.User;
import org.jdatepicker.impl.DateComponentFormatter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Properties;
import java.util.List;

public class AddWorkoutUI{
    //public class AddWorkoutDialog {

    public static void show(DBService dbService, JFrame parent, User user, ScreenManager screenManager, UserUI userUI) {


        UtilDateModel dateModel = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");

        JDatePanelImpl datePanel = new JDatePanelImpl(dateModel, p);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateComponentFormatter());
        JPanel datePickerPanel = new JPanel();
        datePickerPanel.add(datePicker);

        LocalDate workoutDate = getValidDate(datePickerPanel, parent);
        if (workoutDate == null) return;

        Integer duration = getValidDuration(parent);
        if (duration == null) return;

        WorkoutEntry.ExerciseType exerciseType = getValidExerciseType(parent);
        if (exerciseType == null) return;

        String description = getValidDescription(parent);
        if (description == null) return;

        WorkoutEntry entry = new WorkoutEntry(duration, exerciseType, 0, 0);
        entry.setDescription(description);
        entry.setDate(workoutDate);
        dbService.createUserWorkoutEntry(user, entry);

        if (userUI != null) {
            userUI.refresh();  // Update the history table with the new entry
        }
        //screenManager.showScreen(ScreenManager.Screen.USER_HOME);
        screenManager.showScreen(ScreenManager.Screen.USER_HOME, user);
    }

    private static LocalDate getValidDate(JPanel datePickerPanel, JFrame parent) {
        while (true) {
            int dateResult = JOptionPane.showConfirmDialog(
                    parent,
                    datePickerPanel,
                    "Select Workout Date",
                    JOptionPane.OK_CANCEL_OPTION
            );

            if (dateResult != JOptionPane.OK_OPTION) return null;

            Date selectedDate = (Date) ((JDatePickerImpl) datePickerPanel.getComponent(0)).getModel().getValue();
            if (selectedDate != null) {
                return selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            }
            JOptionPane.showMessageDialog(parent, "Please select a valid date", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static Integer getValidDuration(JFrame parent) {
        while (true) {
            try {
                String input = JOptionPane.showInputDialog(parent, "Duration (minutes):");
                if (input == null) return null;
                int duration = Integer.parseInt(input);
                if (duration > 0) return duration;
                JOptionPane.showMessageDialog(parent, "Duration must be positive", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(parent, "Please enter a valid number", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static WorkoutEntry.ExerciseType getValidExerciseType(JFrame parent) {
        JComboBox<WorkoutEntry.ExerciseType> typeCombo = new JComboBox<>(WorkoutEntry.ExerciseType.values());
        while (true) {
            int result = JOptionPane.showConfirmDialog(
                    parent,
                    typeCombo,
                    "Select Type",
                    JOptionPane.OK_CANCEL_OPTION
            );
            if (result != JOptionPane.OK_OPTION) return null;
            return (WorkoutEntry.ExerciseType) typeCombo.getSelectedItem();
        }
    }

    private static String getValidDescription(JFrame parent) {
        while (true) {
            String desc = JOptionPane.showInputDialog(parent, "Enter Description:");
            if (desc == null) return null;
            if (!desc.trim().isEmpty()) return desc.trim();
            JOptionPane.showMessageDialog(parent, "Description cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
