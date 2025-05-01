package org.example.bearfitness.ui;

import org.example.bearfitness.data.DBService;
import org.example.bearfitness.user.User;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class GoalsDisplayUI extends JPanel {
    private DBService dbService;
    private User user;
    private UserUI parentUI;

    public GoalsDisplayUI() {
        setLayout(new BorderLayout());
        setBackground(Color.DARK_GRAY);

        JLabel titleLabel = new JLabel("Goals", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel, BorderLayout.NORTH);

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(1, "Progress", "Week 1");
        dataset.addValue(3, "Progress", "Week 2");
        dataset.addValue(6, "Progress", "Week 3");
        dataset.addValue(9, "Progress", "Week 4");

        JFreeChart chart = ChartFactory.createLineChart(
            "Workout Progress Over Time",
                "Week",
                "Workouts Completed",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
                );

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        add(chartPanel, BorderLayout.CENTER);
    }

    public static void main (String[] args) {
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
