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
    private String song;

    public SelectSong() {
        String[] headers = {"Name", "Artist"};

        musicFileLoader loader = new musicFileLoader();
        List<Song> songs = new ArrayList<>();

        try {
             songs = loader.LoadMusicFile();
        } catch (Exception e) { e.printStackTrace(); }


        setTitle("Select Song");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel songName = new DefaultTableModel(headers, 0);

        for(int i = 0; i < songs.size(); i++ ){
            String s = songs.get(i).getName();
            String artist = songs.get(i).getArtist();

            songName.addRow(new String[]{s, artist});
        }

        JTable table = new JTable(songName);
        JScrollPane scrollPane = new JScrollPane(table);

        panel.add(scrollPane, BorderLayout.CENTER);
        add(panel, BorderLayout.CENTER);


        JButton playButton = new JButton("Play");
        List<Song> finalSongs = songs;
        playButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();

            if(selectedRow != -1){
                Song selectedSong = finalSongs.get(selectedRow);
                song = selectedSong.getFile();
            }
            else{
                JOptionPane.showMessageDialog(this, "No song selected");
            }

            if (music != null) {
                music.stop();
            }

            music = new Music();
            music.play(song);
        });

        add(playButton, BorderLayout.SOUTH);
        setVisible(true);

    }


}
