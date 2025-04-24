package org.example.bearfitness.trainerJuice;

public class Song {
    String name;
    String artist;
    String file;

    Song(String[] split){
        this.name = split[0];
        this.artist = split[1];
        this.file = split[2];
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
