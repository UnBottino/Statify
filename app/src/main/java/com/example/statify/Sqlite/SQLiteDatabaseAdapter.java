package com.example.statify.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.example.statify.Model.SpotifySong;
import com.example.statify.Model.Track;

import java.util.ArrayList;
import java.util.List;

public class SQLiteDatabaseAdapter {

    SQLiteDatabaseHelper dbHelper;
    public SQLiteDatabaseAdapter(Context context)
    {
        dbHelper = new SQLiteDatabaseHelper(context);
    }

    public void addSpotifySong(SpotifySong spotifySong) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("track_name", spotifySong.getSongName());
        values.put("play_count", 1);
        values.put("track_timeStamp", spotifySong.getTimeStamp());
        values.put("track_art_url", spotifySong.getSongArtUrl());
        values.put("track_album_name", spotifySong.getSongAlbumName());
        values.put("track_artists", spotifySong.getSongArtists());

        db.insert("tracks", null, values);
        db.close();
    }

    public Track getTrackInfo(String trackName) {
        String query = SQLiteQueries.getTrackInfo(trackName);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.getCount() != 0){
            cursor.moveToNext();
            db.close();
            return new Track(cursor.getString(0),
                    cursor.getInt(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5));

        }
        else{
            db.close();
            return null;
        }
    }

    public List<Track> getAllTracks() {
        String query = SQLiteQueries.getAllTracks();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        List<Track> tracks = new ArrayList<>();

        if(cursor != null){
            cursor.moveToNext();
            for(int i = 0; i < cursor.getCount(); i++){
                Track track = new Track(cursor.getString(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5));
                tracks.add(track);
                cursor.moveToNext();
            }
            cursor.close();
            db.close();
            return tracks;
        }
        else{
            cursor.close();
            db.close();
            return null;
        }
    }

    public void updateTrack(String trackName, int playCount, String timestamp){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("play_count", playCount);
        cv.put("track_timeStamp", timestamp);
        db.update("tracks", cv, "track_name = ?", new String[] {trackName});

        db.close();
    }

    public void dropTrack(String trackName) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("tracks", "track_name = ?", new String[] {trackName});
    }

    static class SQLiteDatabaseHelper  extends SQLiteOpenHelper {
        private static final int DATABASE_VERSION = 45;
        private static final String DATABASE_NAME = "trackCounts";

        private static final String TABLE_TRACKS = "tracks";

        public SQLiteDatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQLiteQueries.createTracksTable());
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRACKS);
            onCreate(db);
            db.close();
        }
    }
}
