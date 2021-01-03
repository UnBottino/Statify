package com.example.statify.Model;

import java.util.Comparator;

public class Track {
    private String trackName;
    private int playCount;
    private String trackTimestamp;
    private String trackArtUrl;
    private String trackAlbumName;
    private String trackArtists;

    public Track(String trackName, int playCount, String trackTimestamp, String trackArtUrl, String trackAlbumName, String trackArtists) {
        this.trackName = trackName;
        this.playCount = playCount;
        this.trackTimestamp = trackTimestamp;
        this.trackArtUrl = trackArtUrl;
        this.trackAlbumName = trackAlbumName;
        this.trackArtists = trackArtists;
    }

    public String getTrackName() {
        return trackName;
    }
    public int getPlayCount() {
        return playCount;
    }
    public String getTrackTimestamp() { return trackTimestamp; }
    public String getTrackArtUrl() { return trackArtUrl; }
    public String getTrackAlbumName() {
        return trackAlbumName;
    }
    public String getTrackArtists() {
        return trackArtists;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }
    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }
    public void setTrackTimestamp(String trackTimestamp) { this.trackTimestamp = trackTimestamp; }
    public void setTrackArtUrl(String trackArtUrl) { this.trackArtUrl = trackArtUrl; }
    public void setTrackAlbumName(String trackAlbumName) {
        this.trackAlbumName = trackAlbumName;
    }
    public void setTrackArtists(String trackArtists) {
        this.trackArtists = trackArtists;
    }

    public static class playCountComparator implements Comparator<Track>
    {
        public int compare(Track t1, Track t2)
        {
            int count1 = t1.getPlayCount();
            int count2 = t2.getPlayCount();

            if (count1 == count2)
                return 0;
            else if (count1 < count2)
                return 1;
            else
                return -1;
        }
    }
}
