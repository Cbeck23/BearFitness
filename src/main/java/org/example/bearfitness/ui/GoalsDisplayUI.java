package org.example.bearfitness.ui;

import org.example.bearfitness.data.DBService;
import org.example.bearfitness.user.User;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.example.bearfitness.fitness.UserWorkoutEntry;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.time.LocalDate;
import java.util.Map;

public class GoalsDisplayUI extends JPanel {
    //private DBService dbService;
    //private User user;
    //private UserUI parentUI;

    public GoalsDisplayUI() {
        /*this.dbService = dbService;
        this.user = user;
        this.parentUI = parentUI;*/

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
        //Default data for example purposes only
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(1, value, "Week 1");
        dataset.addValue(3, value, "Week 2");
        dataset.addValue(6, value, "Week 3");
        dataset.addValue(9, value, "Week 4");

        return dataset;

        /*List<UserWorkoutEntry> entries = dbService.getEntriesForUser(user.getId());
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (UserWorkoutEntry entry : entries) {
            LocalDate date = entry.getDate();
            dataset.addValue(entry.getCalories(), "Calories", date.toString());
            dataset.addValue(entry.getSleep(), "Sleep", date.toString());
            dataset.addValue(entry.getWeight(), "Weight", date.toString());
            dataset.addValue(entry.getWorkoutsCompleted(), "Workouts", date.toString());
        }

        return dataset;*/
    }


    public static void main (String[] args) {
        //DBService dbService1 = new DBService();
        //User user = new User();

        //GoalsDisplayUI ui = new GoalsDisplayUI(dbService, user);

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setTitle("Goals Display");
            frame.setSize(800, 600);
            frame.add(new GoalsDisplayUI());
            frame.setVisible(true);
        });
    }


}
