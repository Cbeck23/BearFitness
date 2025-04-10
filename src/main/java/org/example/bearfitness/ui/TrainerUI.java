package org.example.bearfitness.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;


public class TrainerUI extends JFrame {
  private JFrame frame;

  public TrainerUI() {
    frame = new JFrame("BearFitness Trainer");
    frame.setLayout(new BorderLayout());
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JButton exerciseCreation = new JButton("Create an Exercise");
    exerciseCreation.addActionListener(this::createPlan);

    //top buttons for app flow
    JPanel buttons = new JPanel(new GridLayout(1,3,20,20));
    buttons.add(new JButton("Workout"));
    buttons.add(exerciseCreation);
    buttons.add(new JButton("Settings"));

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

    frame.pack();

    frame.setLocationRelativeTo(null);
    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    frame.setVisible(true);

  }

  private void createPlan(ActionEvent e){
    CreateExerciseUI plan = new CreateExerciseUI();
    plan.setVisible(true);
  }


  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new TrainerUI().setVisible(false));
  }
}
