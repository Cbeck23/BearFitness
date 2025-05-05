package org.example.bearfitness.ui;

import org.example.bearfitness.trainerJuice.Music;
import org.example.bearfitness.trainerJuice.Song;
import org.example.bearfitness.trainerJuice.musicFileLoader;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SelectSong extends JFrame {

    private Music music;
    private List<Song> songs;
    private JTable table;
    private JButton playButton, stopButton;

    public SelectSong() {
        super("Select Song");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        music = new Music(); // Create a new Music player

        // Load songs
        musicFileLoader loader = new musicFileLoader();
        try {
            songs = loader.LoadMusicFile();
        } catch (Exception e) {
            e.printStackTrace();
            songs = new ArrayList<>();
        }

        // Table of songs
        String[] headers = {"Name", "Artist"};
        DefaultTableModel model = new DefaultTableModel(headers, 0);
        table = new JTable(model);
        for (Song s : songs) {
            model.addRow(new Object[]{s.getName(), s.getArtist()});
        }
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());

        playButton = new JButton("Play ▶️");
        playButton.addActionListener(e -> playSelectedSong());

        stopButton = new JButton("Stop ⏹️");
        stopButton.addActionListener(e -> music.stop());

        buttonPanel.add(playButton);
        buttonPanel.add(stopButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void playSelectedSong() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a song.");
            return;
        }

        String filePath = songs.get(row).getFile();
        music.play(filePath);
    }
}
