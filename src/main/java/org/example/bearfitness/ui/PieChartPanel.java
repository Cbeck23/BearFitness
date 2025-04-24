package org.example.bearfitness.ui;

import org.example.bearfitness.data.DBService;
import org.example.bearfitness.fitness.WorkoutEntry;
import org.example.bearfitness.user.User;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PieChartPanel extends JPanel {

    private DBService dbService;
    private User user;

    public PieChartPanel(DBService dbService, User user) {
        this.dbService = dbService;
        this.user = user;
        setLayout(new BorderLayout());
        refreshChart();
    }

    public void refreshChart() {
        removeAll();  // clear old chart

        List<WorkoutEntry> userEntries = dbService.getUserEntries(user.getId());

        Map<WorkoutEntry.ExerciseType, Integer> exerciseCounts = new HashMap<>();
        for (WorkoutEntry entry : userEntries) {
            WorkoutEntry.ExerciseType type = entry.getExerciseType();
            exerciseCounts.put(type, exerciseCounts.getOrDefault(type, 0) + 1);
        }

        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Map.Entry<WorkoutEntry.ExerciseType, Integer> entry : exerciseCounts.entrySet()) {
            dataset.setValue(entry.getKey().toString(), entry.getValue());
        }

        JFreeChart chart = ChartFactory.createPieChart(
                "Exercise Type Distribution",
                dataset,
                true,
                true,
                false
        );
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {1} ({2})"));


        ChartPanel chartPanel = new ChartPanel(chart);
        //chartPanel.setPreferredSize(new Dimension(400, 300));


        add(chartPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}
