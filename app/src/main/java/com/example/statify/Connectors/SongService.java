package com.example.statify.Connectors;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.statify.Model.SpotifySong;
import com.example.statify.VolleyCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SongService {
    private ArrayList<SpotifySong> spotifySongs = new ArrayList<>();
    private String spotifySongArtUrl;
    private String spotifySongAlbumName;
    private String spotifySongArtists = "";
    private SharedPreferences sharedPreferences;
    private RequestQueue queue;

    public SongService(Context context) {
        sharedPreferences = context.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(context);
    }

    public ArrayList<SpotifySong> getSpotifySongs() {
        return spotifySongs;
    }
    public String getSpotifySongArtUrl(){
        return spotifySongArtUrl;
    }
    public String getSpotifySongAlbumName(){ return  spotifySongAlbumName;}
    public String getSpotifySongArtists(){
        return spotifySongArtists;
    }

    public void getRecentlyPlayedSongs(final VolleyCallBack callBack) {
        String endpoint = "https://api.spotify.com/v1/me/player/recently-played?limit=50";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray jsonArray = response.optJSONArray("items");
                        for (int n = 0; n < jsonArray.length(); n++) {
                            try {
                                String songName = jsonArray.getJSONObject(n).optJSONObject("track").get("name").toString();
                                String timeStamp = jsonArray.getJSONObject(n).get("played_at").toString();
                                String songID = jsonArray.getJSONObject(n).optJSONObject("track").get("id").toString();

                                SpotifySong spotifySong = new SpotifySong(songName, timeStamp, songID);
                                spotifySongs.add(spotifySong);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        callBack.onSuccess();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", null);
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }

    public void getSpotifySongInfo(final VolleyCallBack callBack, String id) {
        String endpoint = "https://api.spotify.com/v1/tracks/" + id + "/";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Getting track art
                            JSONObject jsonObjectAlbum = response.optJSONObject("album");
                            JSONArray jsonArrayImages = jsonObjectAlbum.getJSONArray("images");
                            spotifySongArtUrl = jsonArrayImages.getJSONObject(1).get("url").toString();

                            //Getting track album name
                            spotifySongAlbumName = jsonObjectAlbum.get("name").toString();

                            //Getting track artists
                            spotifySongArtists = "";
                            JSONArray jsonArrayArtists = response.getJSONArray("artists");
                            for (int i = 0; i < jsonArrayArtists.length(); i++) {
                                if (jsonArrayArtists.length() > 1) {
                                    if (i == 0) {
                                        spotifySongArtists = spotifySongArtists + jsonArrayArtists.getJSONObject(i).get("name").toString();
                                    } else {
                                        spotifySongArtists = spotifySongArtists + ", " + jsonArrayArtists.getJSONObject(i).get("name").toString();
                                    }
                                } else {
                                    spotifySongArtists = spotifySongArtists + jsonArrayArtists.getJSONObject(i).get("name").toString();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        callBack.onSuccess();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", null);
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }
}
