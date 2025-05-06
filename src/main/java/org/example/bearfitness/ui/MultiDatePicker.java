package org.example.bearfitness.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MultiDatePicker extends JPanel {
    private final JComboBox<String> dayCombo;
    private final JComboBox<String> monthCombo;
    private final JComboBox<String> yearCombo;
    private final JTextArea displayArea;
    private final JButton addDateButton;
    private final List<LocalDate> selectedDates = new ArrayList<>();

    public MultiDatePicker() {
        setLayout(new BorderLayout());

        // Date selection panel
        JPanel datePanel = new JPanel(new FlowLayout());
        dayCombo = new JComboBox<>();
        for (int i = 1; i <= 31; i++) dayCombo.addItem(String.valueOf(i));
        datePanel.add(new JLabel("Day:"));
        datePanel.add(dayCombo);

        monthCombo = new JComboBox<>(new String[]{
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        });
        datePanel.add(new JLabel("Month:"));
        datePanel.add(monthCombo);

        yearCombo = new JComboBox<>();
        for (int i = 2020; i <= 2030; i++) yearCombo.addItem(String.valueOf(i));
        datePanel.add(new JLabel("Year:"));
        datePanel.add(yearCombo);

        add(datePanel, BorderLayout.NORTH);
        LocalDate today = LocalDate.now();
        dayCombo.setSelectedItem(String.valueOf(today.getDayOfMonth()));
        monthCombo.setSelectedIndex(today.getMonthValue() - 1); // January = 0
        yearCombo.setSelectedItem(String.valueOf(today.getYear()));

        // Button and text display
        addDateButton = new JButton("Add Date");
        displayArea = new JTextArea(5, 30);
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        add(addDateButton, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        addDateButton.addActionListener(this::handleAddDate);
    }

    private void handleAddDate(ActionEvent e) {
        try {
            LocalDate selected = getSelectedDate();
            if (!selectedDates.contains(selected)) {
                selectedDates.add(selected);
                displayArea.append(selected.toString() + "\n");
            } else {
                JOptionPane.showMessageDialog(this, "Date already added.");
            }
        } catch (DateTimeException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid date selected!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private LocalDate getSelectedDate() {
        int day = Integer.parseInt((String) dayCombo.getSelectedItem());
        int month = monthCombo.getSelectedIndex() + 1;
        int year = Integer.parseInt((String) yearCombo.getSelectedItem());
        return LocalDate.of(year, month, day);
    }

    public List<LocalDate> getSelectedDates() {
        return new ArrayList<>(selectedDates);
    }
}
