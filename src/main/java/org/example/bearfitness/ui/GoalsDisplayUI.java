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
    private UserUI parentUI;

    private List<WorkoutEntry> userEntries;

    public GoalsDisplayUI(DBService dbService, User user, UserUI parentUI) {
        this.dbService = dbService;
        this.user = user;
        this.parentUI = parentUI;
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

        JPanel buttonPanel = new JPanel(new FlowLayout());
        String[] labels = {"Progress", "Calories", "Weight", "Sleep"};
        for (String label : labels) {
            JButton button = new JButton(label);
            button.addActionListener(e -> cardLayout.show(chartContainer, label));
            buttonPanel.add(button);
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

        buttonPanel.add(timePicker);

        add(buttonPanel, BorderLayout.NORTH);
        add(chartContainer, BorderLayout.CENTER);
    }

    private JFreeChart createProgressChart(String timeScale) {
        DefaultCategoryDataset dataset = createCategoryDataset("Workouts");

        JFreeChart chart = createChart("Workout Progress Over Time", timeScale, "Workouts Completed", dataset);

        NumberAxis yAxis = (NumberAxis) chart.getCategoryPlot().getRangeAxis();
        yAxis.setRange(0, 20);
        yAxis.setTickUnit(new NumberTickUnit(1));

        return chart;
    }

    private JFreeChart createCalChart(String timeScale) {
        DefaultCategoryDataset dataset = createCategoryDataset("Calories");

        JFreeChart chart = createChart("Calories Over Time", timeScale, "Calories Consumed", dataset);

        NumberAxis yAxis = (NumberAxis) chart.getCategoryPlot().getRangeAxis();
        yAxis.setRange(0, 2400);
        yAxis.setTickUnit(new NumberTickUnit(200));

        return chart;
    }

    private JFreeChart createWeightChart(String timeScale) {
        DefaultCategoryDataset dataset = createCategoryDataset("Weight");

        JFreeChart chart = createChart("Weight Over Time", timeScale, "Weight", dataset);

        NumberAxis yAxis = (NumberAxis) chart.getCategoryPlot().getRangeAxis();
        yAxis.setRange(0, 500);
        yAxis.setTickUnit(new NumberTickUnit(20));
        return chart;
    }

    private JFreeChart createSleepChart(String timeScale) {
        DefaultCategoryDataset dataset = createCategoryDataset("Sleep");

        JFreeChart chart = createChart("Sleep Over Time", timeScale, "Hours Slept", dataset);

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

    private DefaultCategoryDataset createCategoryDataset(String value) {
        int workoutCount = 0;
        Map<LocalDate, Integer> calories = dbService.getCalsLogged(user);
        Map<LocalDate, Double> sleep = dbService.getSleepLogged(user);
        Map<LocalDate, Double> weights = dbService.getWeightLogged(user);

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();


        switch (value) {
           case "Weight":
               for (Map.Entry<LocalDate, Double> entry : weights.entrySet()) {
                   String formattedDate = new SimpleDateFormat("MM/dd").format(entry.getKey());
                   dataset.addValue(entry.getValue(), "Weight", formattedDate);
               }
               break;

            case "Calories":
                for (Map.Entry<LocalDate, Integer> entry : calories.entrySet()) {
                    String formattedDate = entry.getKey().format(DateTimeFormatter.ofPattern("MM/dd"));
                    dataset.addValue(entry.getValue(), "Calories", formattedDate);
                }
                break;

            case "Sleep":
                for (Map.Entry<LocalDate, Double> entry : sleep.entrySet()) {
                    String formattedDate = new SimpleDateFormat("MM/dd").format(entry.getKey());
                    dataset.addValue(entry.getValue(), "Sleep", formattedDate);
                }
                break;

            case "Workouts":
                Map<LocalDate, Long> workoutCountMap = userEntries.stream()
                        .collect(Collectors.groupingBy(
                                WorkoutEntry::getDate,
                                Collectors.counting()
                        ));

                for (Map.Entry<LocalDate, Long> entry : workoutCountMap.entrySet()) {
                    String formattedDate = entry.getKey().format(DateTimeFormatter.ofPattern("MM/dd"));
                    dataset.addValue(entry.getValue(), "Workouts", formattedDate);
                }
                break;

        }

        return dataset;
    }

    public static void main(String[] args) {
        //MOCK DATA FOR TESTING
        User dummyUser = new User();
        dummyUser.setId(1L); // must match test data
        dummyUser.setUsername("testUser");

        UserStats stats = new UserStats();

        Map<LocalDate, Double> weightLog = new HashMap<>();
        weightLog.put(LocalDate.now().minusDays(3), 150.0); // 3 days ago
        weightLog.put(LocalDate.now().minusDays(2), 149.2); // 2 days ago
        weightLog.put(LocalDate.now().minusDays(1), 148.6);     // yesterday
        stats.setWeightLog(weightLog);

        stats.setCaloriesLogged(Map.of(
                LocalDate.now().minusDays(2), 2000,
                LocalDate.now().minusDays(1), 2100
        ));
        stats.setSleepLogged(Map.of(
                LocalDate.now().minusDays(2), 7.0,
                LocalDate.now().minusDays(1), 8.0
        ));

        dummyUser.setUserStats(stats);

        DBService fakeDBService = new DBService(null, null, null, null) {
            @Override
            public Map<LocalDate, Integer> getCalsLogged(User user) {
                return user.getUserStats().getCaloriesLogged();
            }

            @Override
            public Map<LocalDate, Double> getSleepLogged(User user) {
                return user.getUserStats().getSleepLogged();
            }

            @Override
            public Map<LocalDate, Double> getWeightLogged(User user) {
                return user.getUserStats().getWeightLog();
            }

            @Override
            public List<WorkoutEntry> getWorkoutEntriesForUser(User user) {
                WorkoutEntry w1 = new WorkoutEntry();
                w1.setDate(LocalDate.now().minusDays(2));
                WorkoutEntry w2 = new WorkoutEntry();
                w2.setDate(LocalDate.now().minusDays(1));
                return List.of(w1, w2);
            }
        };

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Goals Display Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.add(new GoalsDisplayUI(fakeDBService, dummyUser, null));
            frame.setVisible(true);
        });
    }

}
