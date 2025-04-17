package org.example.bearfitness.ui;

import org.example.bearfitness.data.DBService;
import org.example.bearfitness.trainerJuice.Music;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;


public class TrainerUI extends JFrame {
  private JFrame frame;
  private static DBService dbService;
  private JTextArea plansDisplay;

  public TrainerUI(DBService dbService) {
    this.dbService = dbService;
    frame = new JFrame("BearFitness Trainer");
    frame.setLayout(new BorderLayout());
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JButton exerciseCreation = new JButton("Create an Exercise Plan");
    exerciseCreation.addActionListener(this::createPlan);

    //top buttons for app flow
    JPanel buttons = new JPanel(new GridLayout(1,3,20,20));
    buttons.add(new JButton("Workout"));
    buttons.add(exerciseCreation);
    JButton settings = new JButton("Settings");
    settings.addActionListener(this::Settings);
    buttons.add(settings);

    JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER));
    center.add(buttons);
    frame.add(center, BorderLayout.NORTH);
    //frame.setContentPane(center);


    // FIX: MAKE OWN CLASS TO LOAD TRAINEE INFORMATION ??
    DefaultTableModel trainees = new DefaultTableModel();
    trainees.addColumn("Name");
    trainees.addColumn("Age");
    JTable table = new JTable(trainees);
    JScrollPane scrollPane = new JScrollPane(table);

    JPanel formatter = new JPanel(new GridLayout(1,2));

    // FIX: ADD CALENDAR TO TRAINER PAGE INCLUDING WORKOUTS THEY HAVE
    formatter.add(new JTextField("Calendar"));
    formatter.add(scrollPane);

    frame.add(formatter, BorderLayout.CENTER);

    JPanel rightPanel = new JPanel(new BorderLayout());
    rightPanel.setBorder(BorderFactory.createTitledBorder("Your Plans"));

    plansDisplay = new JTextArea(15, 30);
    plansDisplay.setEditable(false);
    refreshPlansDisplay();
    JScrollPane planScroll = new JScrollPane(plansDisplay);

    rightPanel.add(planScroll, BorderLayout.CENTER);
    frame.add(rightPanel, BorderLayout.EAST);

    frame.pack();

    frame.setLocationRelativeTo(null);
    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    frame.setVisible(true);

  }

  private void createPlan(ActionEvent e){
    CreateExerciseUI planUI = new CreateExerciseUI(dbService);

    planUI.addWindowListener(new java.awt.event.WindowAdapter() {
      @Override
      public void windowClosed(java.awt.event.WindowEvent windowEvent) {
        refreshPlansDisplay(); // Refresh plan list when the window closes
      }
    });

    planUI.setVisible(true);
  }

  private void Settings(ActionEvent e){
    TrainerSettings settings = new TrainerSettings();
    settings.setVisible(true);
  }

  public void refreshPlansDisplay() {
    if (plansDisplay == null) return;

    try {
      java.util.List<String> planNames = dbService.getAllPlans();
      StringBuilder builder = new StringBuilder();
      for (String name : planNames) {
        builder.append("- ").append(name).append("\n");
      }
      plansDisplay.setText(builder.toString());
    } catch (Exception ex) {
      plansDisplay.setText("Failed to load plans.");
      ex.printStackTrace();
    }
  }

//  public static void main(String[] args) {
//    //SwingUtilities.invokeLater(() -> new TrainerUI().setVisible(false));
//    ConfigurableApplicationContext context = SpringApplication.run(BearFitnessApplication.class, args);
//    DBService dbService = context.getBean(DBService.class);
//
//
//    SwingUtilities.invokeLater(() ->  new TrainerUI(dbService).setVisible(true));
//  }
}
