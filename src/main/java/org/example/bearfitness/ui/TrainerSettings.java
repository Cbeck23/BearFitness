package org.example.bearfitness.ui;

import org.example.bearfitness.trainerJuice.Music;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class TrainerSettings extends JFrame {
    private Music music;

    public TrainerSettings() {
        setTitle("Trainer Settings");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(800, 600);

        setLayout(new BorderLayout());

        JButton music = new JButton("Music");
        music.setSize(50,50);
        music.addActionListener(this::music);
        add(music, BorderLayout.CENTER);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void music(ActionEvent e) {
            SelectSong song = new SelectSong();
            song.setVisible(true);

//        if (music != null) {
//            music.stop();
//        }
//
//        music = new Music();
//        music.play();
    }



}
