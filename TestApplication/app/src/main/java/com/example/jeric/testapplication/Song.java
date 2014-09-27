package com.example.jeric.testapplication;

/**
 * Created by Jeric on 26/09/2014.
 */
public class Song {

    private long id;
    private String title;
    private String artist;

    public Song(long songId, String songTitle, String songArtist) {
        this.id = songId;
        this.title = songTitle;
        this.artist = songArtist;
    }

    public long getID() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getArtist() {
        return this.artist;
    }
}
