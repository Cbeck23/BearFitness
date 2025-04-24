package org.example.bearfitness.trainerJuice;
import javazoom.jl.player.Player;

import java.io.*;

public class Music {
    FileInputStream audioFile;
    Player player;

    public synchronized void play(String fileName) {
        try {

            if (player == null) {
                stop();
            }

            audioFile = new FileInputStream("src/main/resources/secret/audio/" + fileName);
            player = new Player(audioFile);


            new Thread(() -> {
            try {
                player.play();
            }
            catch (Exception e) {
                e.printStackTrace();
            } finally {
                cleanup();
            }
        }).start();


        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (player != null) {
            player.close();
            cleanup();
        }
    }

    private void cleanup() {
        try {
            if (audioFile != null) {
                audioFile.close();
                audioFile = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            player = null;
        }
    }

}
