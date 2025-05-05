package org.example.bearfitness.trainerJuice;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.Header;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.*;

public class Music {
    private FileInputStream audioFile;
    private BufferedInputStream bufferedInputStream;
    private AdvancedPlayer player;
    private Thread playThread;
    private String currentFile;
    private int pausedFrame = 0;
    private boolean isPaused = false;
    private boolean isPlaying = false;
    private int framesPlayedInSession = 0; // ➡️ New!

    public synchronized void play(String fileName) {
        try {
            stop(); // Stop any existing playback

            currentFile = "src/main/resources/secret/audio/" + fileName;
            audioFile = new FileInputStream(currentFile);
            bufferedInputStream = new BufferedInputStream(audioFile);
            player = new AdvancedPlayer(bufferedInputStream);

            isPaused = false;
            isPlaying = true;
            framesPlayedInSession = 0; // ➡️ Reset frame counter

            playThread = new Thread(() -> {
                try {
                    Bitstream bitstream = new Bitstream(new FileInputStream(currentFile));
                    Header header;

                    // ➡️ Skip frames if resuming
                    for (int i = 0; i < pausedFrame; i++) {
                        header = bitstream.readFrame();
                        if (header == null) break;
                        bitstream.closeFrame();
                    }

                    player.play(pausedFrame, Integer.MAX_VALUE); // Play from paused frame

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    cleanup();
                }
            });

            playThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        if (player != null) {
            try {
                framesPlayedInSession += getPlayedFrames(); // Add played frames to pausedFrame
                pausedFrame = framesPlayedInSession;
                player.close();  // Stop the player
                isPaused = true;
                isPlaying = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void resume() {
        if (isPaused && currentFile != null) {
            play(currentFile); // Play from pausedFrame
        }
    }

    public void stop() {
        if (player != null) {
            player.close();
        }
        cleanup();
    }

    private int getPlayedFrames() {
        // AdvancedPlayer doesn't give frame count.
        // Approximate: assume frames have been incremented each time we play/pause.
        return framesPlayedInSession;
    }

    private void cleanup() {
        try {
            if (audioFile != null) {
                audioFile.close();
            }
            if (bufferedInputStream != null) {
                bufferedInputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            player = null;
            audioFile = null;
            bufferedInputStream = null;
            playThread = null;
            // pausedFrame NOT reset here! Important to remember where we paused.
            isPaused = false;
            isPlaying = false;
        }
    }
}
