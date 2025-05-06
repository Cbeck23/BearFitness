package org.example.bearfitness.ui;

import org.example.bearfitness.data.DBService;
import org.example.bearfitness.fitness.WorkoutEntry;
import org.example.bearfitness.user.User;
import org.example.bearfitness.user.UserStats;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.ui.RectangleAnchor;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.category.DefaultCategoryDataset;
import org.example.bearfitness.fitness.UserWorkoutEntry;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class GoalsDisplayUI extends JPanel {
    private DBService dbService;
    private User user;
    private ScreenManager screenManager;

    private List<WorkoutEntry> userEntries;

    public GoalsDisplayUI(DBService dbService, ScreenManager screenManager, User user) {
        this.dbService = dbService;
        this.user = user;
        this.screenManager = screenManager;
        this.userEntries = dbService.getWorkoutEntriesForUser(user);

        setLayout(new BorderLayout());
        setBackground(Color.DARK_GRAY);

        JLabel titleLabel = new JLabel("Goals", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel, BorderLayout.NORTH);

        CardLayout cardLayout = new CardLayout();
        JPanel chartContainer = new JPanel(cardLayout);

        String defaultTime = "Week";

        ChartPanel progressChart = new ChartPanel(createProgressChart(defaultTime));
        ChartPanel calChart = new ChartPanel(createCalChart(defaultTime));
        ChartPanel weightChart = new ChartPanel(createWeightChart(defaultTime));
        ChartPanel sleepChart = new ChartPanel(createSleepChart(defaultTime));

        chartContainer.add(progressChart, "Progress");
        chartContainer.add(calChart, "Calories");
        chartContainer.add(weightChart, "Weight");
        chartContainer.add(sleepChart, "Sleep");

        JPanel buttonPanel = new JPanel(new BorderLayout());
        JPanel leftButtons = new JPanel(new GridBagLayout());
        JPanel rightButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 10);

        String[] labels = {"Progress", "Calories", "Weight", "Sleep"};
        for (String label : labels) {
            JButton button = new JButton(label);
            button.addActionListener(e -> cardLayout.show(chartContainer, label));
            leftButtons.add(button, gbc);
            gbc.gridx++;
        }

        String[] timeOptions = {"Week", "Month", "Year"};
        JComboBox<String> timePicker = new JComboBox<>(timeOptions);
        timePicker.addActionListener(e -> {
            String selectedTime = (String) timePicker.getSelectedItem();

            progressChart.setChart(createProgressChart(selectedTime));
            calChart.setChart(createCalChart(selectedTime));
            weightChart.setChart(createWeightChart(selectedTime));
            sleepChart.setChart(createSleepChart(selectedTime));

            chartContainer.revalidate();
            chartContainer.repaint();
        });

        leftButtons.add(timePicker, gbc);

        JButton backButton = new JButton("Back to Dashboard");
        backButton.addActionListener(e -> screenManager.showScreen(ScreenManager.Screen.USER_HOME));
        rightButtons.add(backButton);

        buttonPanel.add(leftButtons, BorderLayout.CENTER);
        buttonPanel.add(rightButtons, BorderLayout.EAST);

        add(buttonPanel, BorderLayout.NORTH);
        add(chartContainer, BorderLayout.CENTER);
    }

    private JFreeChart createProgressChart(String timeScale) {
        DefaultCategoryDataset dataset = createCategoryDataset("Workouts", timeScale);

        JFreeChart chart = createChart("Workout Progress Over Time", timeScale, "Workouts Completed", dataset);

        ValueMarker exerciseGoalLine = new ValueMarker(user.getGoals().getWeeklyExercises());
        exerciseGoalLine.setPaint(Color.BLUE);
        exerciseGoalLine.setStroke(new BasicStroke(2.0f));
        exerciseGoalLine.setLabel("Goal");
        exerciseGoalLine.setLabelAnchor(RectangleAnchor.TOP_LEFT);
        exerciseGoalLine.setLabelTextAnchor(TextAnchor.BOTTOM_LEFT);

        chart.getCategoryPlot().addRangeMarker(exerciseGoalLine);

        NumberAxis yAxis = (NumberAxis) chart.getCategoryPlot().getRangeAxis();
        yAxis.setRange(0, 250);
        yAxis.setTickUnit(new NumberTickUnit(10));

        return chart;
    }

    private JFreeChart createCalChart(String timeScale) {
        DefaultCategoryDataset dataset = createCategoryDataset("Calories", timeScale);

        JFreeChart chart = createChart("Calories Over Time", timeScale, "Calories Consumed", dataset);

        ValueMarker calGoalLine = new ValueMarker(user.getGoals().getGoalCalories());
        calGoalLine.setPaint(Color.BLUE);
        calGoalLine.setStroke(new BasicStroke(2.0f));
        calGoalLine.setLabel("Goal");
        calGoalLine.setLabelAnchor(RectangleAnchor.TOP_LEFT);
        calGoalLine.setLabelTextAnchor(TextAnchor.BOTTOM_LEFT);

        chart.getCategoryPlot().addRangeMarker(calGoalLine);

        NumberAxis yAxis = (NumberAxis) chart.getCategoryPlot().getRangeAxis();
        yAxis.setRange(0, 2400);
        yAxis.setTickUnit(new NumberTickUnit(200));

        return chart;
    }

    private JFreeChart createWeightChart(String timeScale) {
        DefaultCategoryDataset dataset = createCategoryDataset("Weight", timeScale);

        JFreeChart chart = createChart("Weight Over Time", timeScale, "Weight", dataset);

        ValueMarker weightGoalLine = new ValueMarker(user.getGoals().getGoalWeight());
        weightGoalLine.setPaint(Color.BLUE);
        weightGoalLine.setStroke(new BasicStroke(2.0f));
        weightGoalLine.setLabel("Goal");
        weightGoalLine.setLabelAnchor(RectangleAnchor.TOP_LEFT);
        weightGoalLine.setLabelTextAnchor(TextAnchor.BOTTOM_LEFT);

        chart.getCategoryPlot().addRangeMarker(weightGoalLine);

        NumberAxis yAxis = (NumberAxis) chart.getCategoryPlot().getRangeAxis();
        yAxis.setRange(0, 500);
        yAxis.setTickUnit(new NumberTickUnit(20));
        return chart;
    }

    private JFreeChart createSleepChart(String timeScale) {
        DefaultCategoryDataset dataset = createCategoryDataset("Sleep", timeScale);

        JFreeChart chart = createChart("Sleep Over Time", timeScale, "Hours Slept", dataset);

        ValueMarker sleepGoalLine = new ValueMarker(user.getGoals().getGoalSleep());
        sleepGoalLine.setPaint(Color.BLUE);
        sleepGoalLine.setStroke(new BasicStroke(2.0f));
        sleepGoalLine.setLabel("Goal");
        sleepGoalLine.setLabelAnchor(RectangleAnchor.TOP_LEFT);
        sleepGoalLine.setLabelTextAnchor(TextAnchor.BOTTOM_LEFT);

        chart.getCategoryPlot().addRangeMarker(sleepGoalLine);

        NumberAxis yAxis = (NumberAxis) chart.getCategoryPlot().getRangeAxis();
        yAxis.setRange(0, 24);
        yAxis.setTickUnit(new NumberTickUnit(1));

        return chart;
    }

    private JFreeChart createChart(String title, String timeScale, String yAxisLabel, DefaultCategoryDataset dataset) {
        JFreeChart chart = ChartFactory.createLineChart(
                title,
                timeScale,
                yAxisLabel,
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );
        return chart;
    }

    private DefaultCategoryDataset createCategoryDataset(String value, String timeScale) {
        LocalDate cutoffDate = switch (timeScale) {
            case "Week" -> LocalDate.now().minusDays(7);
            case "Month" -> LocalDate.now().minusDays(30);
            case "Year" -> LocalDate.now().minusDays(365);
            default -> LocalDate.MIN;
        };

        int workoutCount = 0;
        Map<LocalDate, Integer> calories = dbService.getCalsLogged(user);
        Map<LocalDate, Double> sleep = dbService.getSleepLogged(user);
        Map<LocalDate, Double> weights = dbService.getWeightLogged(user);

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        switch (value) {
           case "Weight":
               weights.entrySet().stream()
                       .filter(entry -> !entry.getKey().equals(cutoffDate))
                       .sorted(Map.Entry.comparingByKey())
                       .forEach(entry -> {
                           String formattedDate = entry.getKey().format(DateTimeFormatter.ofPattern("MM/dd"));
                           dataset.addValue(entry.getValue(), "Weight", formattedDate);
                       });
               break;

            case "Calories":
                calories.entrySet().stream()
                        .filter(entry -> !entry.getKey().isBefore(cutoffDate))
                        .sorted(Map.Entry.comparingByKey())
                        .forEach(entry -> {
                            String formattedDate = entry.getKey().format(DateTimeFormatter.ofPattern("MM/dd"));
                            dataset.addValue(entry.getValue(), "Calories", formattedDate);
                        });
                break;

            case "Sleep":
                sleep.entrySet().stream()
                        .filter(entry -> !entry.getKey().isBefore(cutoffDate))
                        .sorted(Map.Entry.comparingByKey())
                        .forEach(entry -> {
                            String formattedDate = entry.getKey().format(DateTimeFormatter.ofPattern("MM/dd"));
                            dataset.addValue(entry.getValue(), "Sleep", formattedDate);
                        });
                break;

            case "Workouts":
                Map<LocalDate, Integer> workoutDurationMap = userEntries.stream()
                        .filter(entry -> !entry.getDate().isBefore(cutoffDate))
                        .collect(Collectors.groupingBy(
                                WorkoutEntry::getDate,
                                Collectors.summingInt(WorkoutEntry::getDuration)
                        ));

                workoutDurationMap.entrySet().stream()
                        .sorted(Map.Entry.comparingByKey())
                        .forEach(entry -> {
                            String formattedDate = entry.getKey().format(DateTimeFormatter.ofPattern("MM/dd"));
                            dataset.addValue(entry.getValue(), "Workout Duration", formattedDate);
                        });
                break;


        }

        return dataset;
    }
}
