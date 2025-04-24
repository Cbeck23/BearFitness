package org.example.bearfitness.trainerJuice;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class musicFileLoader {
    String filename = "src/main/resources/secret/songs.csv";

    public List<Song> LoadMusicFile() throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
        String line;
        List<Song> songs = new ArrayList<Song>();
        try {
            int i = 0;
            while ((line = br.readLine()) != null ) {
                int start = 1;
                if(i < start){
                    i++;
                    continue;
                }

                String[] data = line.split(",");
                Song song = new Song(data);
                songs.add(song);

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return songs;
    }
}
