package org.example.bearfitness.ui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private JPanel daysPanel;
    private JTextArea datesDisplay;

    private Calendar[] buttonDates = new Calendar[42];  // To associate date with each button

    public CalendarApp() {
        setLayout(new BorderLayout());

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

        daysPanel = new JPanel(new GridLayout(0, 7));
        dayButtons = new JButton[42];

        for (int i = 0; i < 42; i++) {
            int index = i;  // needed for inner class lambda
            dayButtons[i] = new JButton("");
            dayButtons[i].setEnabled(false);
            dayButtons[i].addActionListener(e -> {
                Calendar date = buttonDates[index];
                if (date != null) {
                    datesDisplay.setText("Selected Date: " + date.getTime().toString());
                }
                /// This needs to return the events on the day. Query database and get
                /// classes for the day.  ? and times ?
            });
            daysPanel.add(dayButtons[i]);
        }

        calendarPanel.add(daysPanel, BorderLayout.CENTER);
        add(calendarPanel, BorderLayout.CENTER);

        datesDisplay = new JTextArea(3, 40);
        datesDisplay.setLineWrap(true);
        datesDisplay.setWrapStyleWord(true);
        datesDisplay.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(datesDisplay);
        add(scrollPane, BorderLayout.SOUTH);

        updateCalendar();
    }

    private void updateCalendar() {
        for (int i = 0; i < 42; i++) {
            dayButtons[i].setText("");
            dayButtons[i].setEnabled(false);
            buttonDates[i] = null;
        }

        calendar.set(currentYear, currentMonth, 1);
        int firstDayOfMonth = calendar.get(Calendar.DAY_OF_WEEK);
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        int dayIndex = firstDayOfMonth - 1;

        for (int i = 1; i <= daysInMonth; i++) {
            dayButtons[dayIndex].setText(String.valueOf(i));
            dayButtons[dayIndex].setEnabled(true);

            Calendar date = new GregorianCalendar(currentYear, currentMonth, i);
            buttonDates[dayIndex] = date;

            dayIndex++;
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

}
