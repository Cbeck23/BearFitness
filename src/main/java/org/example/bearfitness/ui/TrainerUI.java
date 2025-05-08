package org.example.bearfitness.ui;

import org.example.bearfitness.data.DBService;
import org.example.bearfitness.fitness.ExercisePlan;
import org.example.bearfitness.user.*;

import javax.swing.*;
import java.awt.*;

class TrainerUI extends JPanel {
  private DBService dbService;
  private ScreenManager screenManager;
  private User user;
  private JList<String> plansList;
  private DefaultListModel<String> plansListModel;
  private TraineesList traineesList;

  public TrainerUI(DBService dbService, ScreenManager screenManager, User user) {
    this.dbService = dbService;
    this.screenManager = screenManager;
    this.user = user;

    setLayout(new BorderLayout());

    // Top Buttons
    JButton exerciseCreation = new JButton("Create an Exercise Plan");
    exerciseCreation.addActionListener(e -> openExerciseCreator());

    JPanel topButtons = new JPanel(new GridLayout(1, 3, 20, 20));
    JButton classCreation = new JButton("Create a Class");
    classCreation.addActionListener(e -> openClassCreator());
    topButtons.add(classCreation);
    topButtons.add(exerciseCreation);

    JButton settings = new JButton("Settings");
    settings.addActionListener(e -> screenManager.showScreen(ScreenManager.Screen.SETTINGS));
    topButtons.add(settings);

    JPanel topWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
    topWrapper.add(topButtons);
    add(topWrapper, BorderLayout.NORTH);

    // Center Content
    traineesList = new TraineesList(dbService, screenManager, user, this);
    JPanel formatter = new JPanel(new GridLayout(1, 2));
    formatter.add(new CalendarApp(user, dbService));
    formatter.add(traineesList);
    add(formatter, BorderLayout.CENTER);

    // Right Panel: Exercise Plans
    plansListModel = new DefaultListModel<>();
    plansList = new JList<>(plansListModel);
    plansList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    refreshPlansDisplay();

    JButton editButton = new JButton("Edit");
    editButton.addActionListener(e -> editSelectedPlan());

    JButton deleteButton = new JButton("Delete");
    deleteButton.addActionListener(e -> deleteSelectedPlan());

    JPanel buttonPanel = new JPanel();
    buttonPanel.add(editButton);
    buttonPanel.add(deleteButton);

    JPanel rightPanel = new JPanel(new BorderLayout());
    rightPanel.setBorder(BorderFactory.createTitledBorder("Your Plans"));
    rightPanel.add(new JScrollPane(plansList), BorderLayout.CENTER);
    rightPanel.add(buttonPanel, BorderLayout.SOUTH);

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
    plansListModel.clear();
    try {
      java.util.List<String> planNames = dbService.getAllPlans();
      for (String name : planNames) {
        plansListModel.addElement(name);
      }
    } catch (Exception ex) {
      plansListModel.addElement("Failed to load plans.");
    }
  }

  private void deleteSelectedPlan() {
    String selected = plansList.getSelectedValue();
    if (selected == null) return;

    int confirm = JOptionPane.showConfirmDialog(this, "Delete plan \"" + selected + "\"?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
      dbService.deletePlanByName(selected);
      refreshPlansDisplay();
    }
  }

  private void editSelectedPlan() {
    String selected = plansList.getSelectedValue();
    if (selected == null) return;

    ExercisePlan plan = dbService.getPlanByName(selected);
    if (plan == null) {
      JOptionPane.showMessageDialog(this, "Could not load the selected plan.");
      return;
    }

    CreateExerciseUI editPanel = new CreateExerciseUI(dbService, plan);
    JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Edit Exercise Plan", true);
    dialog.setContentPane(editPanel);
    dialog.pack();
    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);
    refreshPlansDisplay();
  }
}
