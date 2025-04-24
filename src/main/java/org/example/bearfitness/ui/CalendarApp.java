package org.example.bearfitness.ui;
import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalendarApp extends JPanel {

    private JPanel calendarPanel;
    private JLabel monthLabel;
    private JButton prevButton;
    private JButton nextButton;
    private JButton[] dayButtons;
    private Calendar calendar;
    private int currentMonth;
    private int currentYear;

    public CalendarApp() {
        setSize(400, 300);

        calendar = new GregorianCalendar();
        currentMonth = calendar.get(Calendar.MONTH);
        currentYear = calendar.get(Calendar.YEAR);

        calendarPanel = new JPanel(new BorderLayout());

        JPanel headerPanel = new JPanel(new FlowLayout());
        prevButton = new JButton("<");
        prevButton.addActionListener(e -> changeMonth(-1));
        monthLabel = new JLabel(getMonthName(currentMonth) + " " + currentYear);
        nextButton = new JButton(">");
        nextButton.addActionListener(e -> changeMonth(1));

        headerPanel.add(prevButton);
        headerPanel.add(monthLabel);
        headerPanel.add(nextButton);

        calendarPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel daysPanel = new JPanel(new GridLayout(0, 7));
        dayButtons = new JButton[42]; // 6 weeks * 7 days
        for (int i = 0; i < 42; i++) {
            dayButtons[i] = new JButton("");
            dayButtons[i].setEnabled(false);
            daysPanel.add(dayButtons[i]);
        }
        calendarPanel.add(daysPanel, BorderLayout.CENTER);

        add(calendarPanel);
        updateCalendar();
        setVisible(true);
    }

    private void updateCalendar() {
        calendar.set(currentYear, currentMonth, 1);
        int firstDayOfMonth = calendar.get(Calendar.DAY_OF_WEEK);
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        int dayIndex = firstDayOfMonth - 1;
        for (int i = 1; i <= daysInMonth; i++) {
            dayButtons[dayIndex].setText(String.valueOf(i));
            dayButtons[dayIndex].setEnabled(true);
            dayIndex++;
        }

        for (int i = dayIndex; i < 42; i++) {
            dayButtons[i].setText("");
            dayButtons[i].setEnabled(false);
        }
    }


    private void changeMonth(int amount) {
        currentMonth += amount;
        if (currentMonth < 0) {
            currentMonth = 11;
            currentYear--;
        } else if (currentMonth > 11) {
            currentMonth = 0;
            currentYear++;
        }
        monthLabel.setText(getMonthName(currentMonth) + " " + currentYear);
        updateCalendar();
    }

    private String getMonthName(int month) {
        return new java.text.DateFormatSymbols().getMonths()[month];
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CalendarApp::new);
    }
}
