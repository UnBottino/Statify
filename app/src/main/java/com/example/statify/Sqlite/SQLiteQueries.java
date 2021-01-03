package com.example.statify.Sqlite;

public class SQLiteQueries {

    public static String createTracksTable(){
        return "CREATE TABLE tracks" +
                "(track_name TEXT NOT NULL UNIQUE, " +
                "play_count INTEGER NOT NULL, " +
                "track_timestamp TEXT NOT NULL UNIQUE, " +
                "track_art_url TEXT NOT NULL, " +
                "track_album_name TEXT NOT NULL, " +
                "track_artists TEXT NOT NULL)";
    }

    public static String getTrackInfo(String trackName){
        return "SELECT * FROM tracks WHERE track_name = '" + trackName + "'";
    }

    public static String getAllTracks(){
        return "SELECT * FROM tracks";
    }
}
