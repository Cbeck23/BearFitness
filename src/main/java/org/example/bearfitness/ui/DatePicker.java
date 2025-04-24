import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.DateTimeException;

public class DatePicker extends JFrame {
    private JComboBox<String> dayCombo, monthCombo, yearCombo;
    private JButton submitButton;

    public DatePicker() {
        setTitle("Date Picker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        // Day dropdown (1-31)
        dayCombo = new JComboBox<>();
        for (int i = 1; i <= 31; i++) {
            dayCombo.addItem(String.valueOf(i));
        }

        // Month dropdown (January-December)
        monthCombo = new JComboBox<>();
        String[] months = {"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};
        for (String month : months) {
            monthCombo.addItem(month);
        }

        // Year dropdown (1900-2030)
        yearCombo = new JComboBox<>();
        for (int i = 1900; i <= 2030; i++) {
            yearCombo.addItem(String.valueOf(i));
        }

        // Submit button
        submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> readSelectedDate());

        // Add components to the frame
        add(new JLabel("Day:"));
        add(dayCombo);
        add(new JLabel("Month:"));
        add(monthCombo);
        add(new JLabel("Year:"));
        add(yearCombo);
        add(submitButton);

        pack();
        setLocationRelativeTo(null); // Center the window
    }

    private void readSelectedDate() {
        try {
            int day = Integer.parseInt((String) dayCombo.getSelectedItem());
            int month = monthCombo.getSelectedIndex() + 1; // January = 1
            int year = Integer.parseInt((String) yearCombo.getSelectedItem());

            LocalDate selectedDate = LocalDate.of(year, month, day);
            JOptionPane.showMessageDialog(this, "Selected Date: " + selectedDate);
        } catch (DateTimeException ex) {
            JOptionPane.showMessageDialog(this, "Invalid date!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DatePicker().setVisible(true));
    }
}