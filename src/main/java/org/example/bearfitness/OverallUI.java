package org.example.bearfitness;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;


public class OverallUI extends JFrame {
//  private JTextField username;
//  private JTextField password;
//  private JButton loginButton;

  public OverallUI() {
    setTitle("BearFitness");
    setSize(800,800);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    // CREATE EXERCISE PANEL

    JButton exerciseCreation = new JButton("Create an Exercise");
    exerciseCreation.addActionListener(this::createPlan);
    add(exerciseCreation, BorderLayout.CENTER);


    setVisible(true);
  }

  private void createPlan(ActionEvent e){
    CreateExerciseUI plan = new CreateExerciseUI();
    plan.setVisible(true);
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new OverallUI().setVisible(true));
  }
}
