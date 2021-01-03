package com.example.statify.Util;

import android.content.Context;
import android.util.Log;

import com.example.statify.Connectors.SongService;
import com.example.statify.MainActivity;
import com.example.statify.Model.SpotifySong;
import com.example.statify.Model.Track;
import com.example.statify.Sqlite.SQLiteDatabaseAdapter;
import com.example.statify.VolleyCallBack;

import java.util.ArrayList;
import java.util.Collections;

public class SpotifyFunctions {
    private static SQLiteDatabaseAdapter dbAdapter;
    private static SongService songService;
    private static ArrayList<SpotifySong> recentlyPlayedTracks;

    private static String spotifySongArtUrl;
    private static String spotifySongAlbumName;
    private static String spotifySongArtists;

    public static void getSpotifySongs(Context context) {
        dbAdapter = new SQLiteDatabaseAdapter(context);
        songService = new SongService(context);

        songService.getRecentlyPlayedSongs(new VolleyCallBack() {
            @Override
            public void onSuccess() {
                recentlyPlayedTracks = songService.getSpotifySongs();
                Collections.reverse(recentlyPlayedTracks);

                for (int i = 0; i < recentlyPlayedTracks.size(); ++i) {
                    System.out.println(recentlyPlayedTracks.get(i).getSongName());

                    if (i == recentlyPlayedTracks.size() - 1) {
                        getSpotifySongUrl(recentlyPlayedTracks.get(i), true);
                    } else {
                        getSpotifySongUrl(recentlyPlayedTracks.get(i), false);
                    }
                }
            }
        });
    }

    public static void getSpotifySongUrl(final SpotifySong s, final Boolean process){
        songService.getSpotifySongInfo(new VolleyCallBack() {
            @Override
            public void onSuccess() {
                spotifySongArtUrl = songService.getSpotifySongArtUrl();
                spotifySongAlbumName = songService.getSpotifySongAlbumName();
                spotifySongArtists = songService.getSpotifySongArtists();

                s.setSongArtUrl(spotifySongArtUrl);
                s.setSongAlbumName(spotifySongAlbumName);
                s.setSongArtists(spotifySongArtists);

                if (process) {
                    processRecentSongs();
                }
            }
        }, s.getSongID());
    }

    public static void processRecentSongs(){
        Log.d("processRecentSongs","Processing Songs");

        boolean newTrack = true;
        for (SpotifySong s : recentlyPlayedTracks) {
            getDBTracks();
            for (Track t : MainActivity.mDBTrackList) {
                if (s.getSongName().equalsIgnoreCase(t.getTrackName())) {
                    newTrack = false;

                    int compareResult = t.getTrackTimestamp().compareToIgnoreCase(s.getTimeStamp());
                    if(compareResult < 0){
                        Log.d("DB","Updating DB track play count (" + t.getTrackName() + ")");
                        dbAdapter.updateTrack(t.getTrackName(), t.getPlayCount() + 1, s.getTimeStamp());
                    }
                    break;
                }
                newTrack = true;
            }
            if(newTrack){
                Log.d("DB","Adding new Spotify song into db (" + s.getSongName() + ")");
                dbAdapter.addSpotifySong(s);
            }
        }
    }

    private static void getDBTracks(){
        MainActivity.mDBTrackList = dbAdapter.getAllTracks();
    }
}
