package org.example.bearfitness;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;


public class TrainerUI extends JFrame {
  private JFrame frame;

  public TrainerUI() {
    frame = new JFrame("BearFitness Trainer");
    frame.setLayout(new BorderLayout());
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JButton exerciseCreation = new JButton("Create an Exercise");
    exerciseCreation.setSize(80,80);
    exerciseCreation.addActionListener(this::createPlan);

    JPanel buttons = new JPanel(new GridLayout(1,0,20,20));
    buttons.add(exerciseCreation);

    JPanel center = new JPanel(new GridBagLayout());
    center.add(buttons);
    frame.setContentPane(center);
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
