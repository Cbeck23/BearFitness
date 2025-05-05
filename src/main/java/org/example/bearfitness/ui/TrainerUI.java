package org.example.bearfitness.ui;

import org.example.bearfitness.data.DBService;
import org.example.bearfitness.user.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;


class TrainerUI extends JPanel {
  private DBService dbService;
  private ScreenManager screenManager;
  private User user;
  private JTextArea plansDisplay;
  private JTextArea datesDisplay;
  private TraineesList traineesList;

  public TrainerUI(DBService dbService, ScreenManager screenManager, User user) {
    this.dbService = dbService;
    this.screenManager = screenManager;
    this.user = user;

    setLayout(new BorderLayout());

    JButton exerciseCreation = new JButton("Create an Exercise Plan");
    exerciseCreation.addActionListener(e -> openExerciseCreator());

    JPanel topButtons = new JPanel(new GridLayout(1, 3, 20, 20));
    JButton classCreation = new JButton("Create a Class");
    topButtons.add(classCreation);
    classCreation.addActionListener(e -> openClassCreator());

    topButtons.add(exerciseCreation);
    JButton settings = new JButton("Settings");
    settings.addActionListener(e-> screenManager.showScreen(ScreenManager.Screen.SETTINGS));
    topButtons.add(settings);

    JButton music = new JButton("Music");
    music.addActionListener(this::music);
    topButtons.add(music);

    JPanel topWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
    topWrapper.add(topButtons);
    add(topWrapper, BorderLayout.NORTH);


    traineesList = new TraineesList(dbService, screenManager, user, this);

    JPanel formatter = new JPanel(new GridLayout(1, 2));
    formatter.add(new CalendarApp(user, dbService));
    formatter.add(traineesList);
    add(formatter, BorderLayout.CENTER);

    plansDisplay = new JTextArea(15, 30);
    plansDisplay.setEditable(false);
    refreshPlansDisplay();

    JPanel rightPanel = new JPanel(new BorderLayout());
    rightPanel.setBorder(BorderFactory.createTitledBorder("Your Plans"));
    rightPanel.add(new JScrollPane(plansDisplay), BorderLayout.CENTER);
    add(rightPanel, BorderLayout.EAST);
  }

  private void openExerciseCreator() {
    CreateExerciseUI createPanel = new CreateExerciseUI(dbService);
    JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Create Exercise Plan", true);
    dialog.setContentPane(createPanel);
    dialog.pack();
    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);
    refreshPlansDisplay();
  }

  private void openClassCreator() {
    ScheduledClassUI createPanel = new ScheduledClassUI(dbService, user);
    JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Create Class", true);
    dialog.setContentPane(createPanel);
    dialog.pack();
    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);
    refreshPlansDisplay();
  }

  private void refreshPlansDisplay() {
    if (plansDisplay == null) return;
    try {
      java.util.List<String> planNames = dbService.getAllPlans();
      StringBuilder builder = new StringBuilder();
      for (String name : planNames) {
        builder.append("- ").append(name).append(" ");
      }
      plansDisplay.setText(builder.toString());
    } catch (Exception ex) {
      plansDisplay.setText("Failed to load plans.");
    }
  }

//  private void Settings(ActionEvent e){
//    TrainerSettings settings = new TrainerSettings();
//    settings.setVisible(true);
//  }

  private void music(ActionEvent e) {
    SelectSong song = new SelectSong();
    song.setVisible(true);
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
