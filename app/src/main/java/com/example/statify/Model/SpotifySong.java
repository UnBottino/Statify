package com.example.statify.Model;

public class SpotifySong {

    private String songName;
    private String timeStamp;
    private String songID;
    private String songArtUrl;
    private String songAlbumName;
    private String songArtists;

    public SpotifySong(String songName, String timeStamp, String songID) {
        this.songName = songName;
        this.timeStamp = timeStamp;
        this.songID = songID;
    }

    public String getSongName() { return songName; }
    public String getTimeStamp() {
        return timeStamp;
    }
    public String getSongID() { return songID; }
    public String getSongArtUrl() {
        return songArtUrl;
    }
    public String getSongAlbumName() {
        return songAlbumName;
    }
    public String getSongArtists() {
        return songArtists;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }
    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
    public void setSongID(String songID) { this.songID = songID; }
    public void setSongArtUrl(String songArtUrl) {
        this.songArtUrl = songArtUrl;
    }
    public void setSongAlbumName(String songAlbumName) {
        this.songAlbumName = songAlbumName;
    }
    public void setSongArtists(String songArtists) {
        this.songArtists = songArtists;
    }
}
