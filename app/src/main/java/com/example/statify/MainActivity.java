package com.example.statify;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import com.example.statify.Adapters.trackListAdapter;
import com.example.statify.Connectors.ForegroundService;
import com.example.statify.Model.Track;
import com.example.statify.Sqlite.SQLiteDatabaseAdapter;
import com.example.statify.Util.SpotifyFunctions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabaseAdapter dbAdapter;

    public SearchView mSearchBar;
    public RelativeLayout mRefreshBtn;
    public ImageView mDone;
    public AnimatedVectorDrawable avd;
    public AnimatedVectorDrawableCompat avd2;

    public static RecyclerView mRecyclerView;
    public static trackListAdapter trackListAdapter;
    public static List<Track> mDBTrackList = new ArrayList<>();

    private Context context;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Run Continuously
        startService(new Intent(this, ForegroundService.class));

        //General page setup
        context = getApplicationContext();
        dbAdapter = new SQLiteDatabaseAdapter(this);
        trackListAdapter = new trackListAdapter(getApplicationContext(), mDBTrackList);

        setupSearchBar();
        setupRecyclerView();
        setupRefreshBtn();

        //Getting DB Tracks
        getDBTracks();
        updateTrackList();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getDBTracks();
        updateTrackList();
    }

    @SuppressLint("StaticFieldLeak")
    public class TaskExample extends AsyncTask<String, String, String> {

        private Context context;

        public TaskExample(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            SpotifyFunctions.getSpotifySongs(context);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            getDBTracks();
            updateTrackList();

            Drawable drawable = mDone.getDrawable();

            if(drawable instanceof AnimatedVectorDrawable){
                avd = (AnimatedVectorDrawable) drawable;
                avd.start();
            }
            else if(drawable instanceof AnimatedVectorDrawableCompat){
                avd2 = (AnimatedVectorDrawableCompat) drawable;
                avd2.start();
            }
        }
    }

    private void setupSearchBar(){
        mSearchBar = findViewById(R.id.searchBar);
        mSearchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchBar.setIconified(false);
            }
        });

        mSearchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                trackListAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                trackListAdapter.getFilter().filter(query);
                return false;
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupRecyclerView(){
        mRecyclerView = findViewById(R.id.trackList);
        mRecyclerView.setOnTouchListener(new hideKeyboardListener());
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupRefreshBtn(){
        mRefreshBtn = findViewById(R.id.refreshBtn);
        mDone = findViewById(R.id.done);

        mRefreshBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    v.animate().scaleX(0.9f).setDuration(100).start();
                    v.animate().scaleY(0.9f).setDuration(100).start();
                    v.animate().alpha(0.5f).setDuration(100).start();
                    return true;
                } else if (action == MotionEvent.ACTION_UP) {
                    v.animate().cancel();
                    v.animate().scaleX(1f).setDuration(100).start();
                    v.animate().scaleY(1f).setDuration(100).start();
                    v.animate().alpha(1f).setDuration(100).start();

                    TaskExample task = new TaskExample(MainActivity.this);
                    task.execute();

                    return true;
                }

                return false;
            }
        });
    }

    class hideKeyboardListener implements View.OnTouchListener {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            getWindow().getDecorView().clearFocus();

            return false;
        }
    }

    private void getDBTracks(){
        Log.d("DB","Getting all tracks from DB");
        mDBTrackList = dbAdapter.getAllTracks();
        Collections.sort(mDBTrackList, new Track.playCountComparator());
    }

    public void updateTrackList(){
        runOnUiThread(new Thread() {
            @Override
            public void run() {
                trackListAdapter = new trackListAdapter(getApplicationContext(), mDBTrackList);
                mRecyclerView.setAdapter(trackListAdapter);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            }
        });
    }
}

