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
    /**
     * Listener interface for song selection events
     */
    public interface SongSelectedListener {
        void songSelected(String filePath);
    }

    private SongSelectedListener listener;
    private List<Song> songs;

    public SelectSong() {
        super("Select Song");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        //Load songs using the correct loader class
        musicFileLoader loader = new musicFileLoader();
        try {
            songs = loader.LoadMusicFile();
        } catch(Exception e) {
            e.printStackTrace();
            songs = new ArrayList<>();
        }

        //Table of songs
        String[] headers = {"Name", "Artist"};
        DefaultTableModel model = new DefaultTableModel(headers, 0);
        JTable table = new JTable(model);
        for(Song s: songs) {
            model.addRow(new Object[]{s.getName(), s.getArtist()});
        }
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Select button triggers listener
        JButton selectButton = new JButton("Select");
        selectButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if(row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a song.");
                return;
            }
            String filePath = songs.get(row).getFile();
            if(listener != null) {
                listener.songSelected(filePath);
            }
            dispose();
        });
        add(selectButton, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void setSongSelectedListener(SongSelectedListener listener) {
        this.listener = listener;
    }
}
