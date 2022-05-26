package com.jitendra.videoplex10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.jitendra.videoplex10.Adapters.FetchYtDataAdapter;
import com.jitendra.videoplex10.Config.YoutubeConfig;
import com.jitendra.videoplex10.Model.YtMediaFiles;

import java.util.ArrayList;

public class StreamActivity extends YouTubeBaseActivity {

    private static final String TAG = "StreamActivity";

    BottomNavigationView bottomNavigationView;
    private RecyclerView recyclerView;
    private ArrayList<YtMediaFiles> ytMediaFilesArrayList;
    private FetchYtDataAdapter fetchYtDataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream);
        Log.d(TAG, "onCreate: Starting Video");

        recyclerView  = findViewById(R.id.ac_stream_recyclerView);

        ytMediaFilesArrayList= new ArrayList<>();

        ytMediaFilesArrayList = loadDummy();
        fetchYtDataAdapter = new FetchYtDataAdapter(this, ytMediaFilesArrayList);
        recyclerView.setAdapter(fetchYtDataAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        fetchYtDataAdapter.notifyDataSetChanged();

        bottomNavigationView = findViewById(R.id.bottom_nav_bar);
        bottomNavigationView.setSelectedItemId(R.id.stream_icon);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.stream_icon:
                        return true;

                    case R.id.library_icon:

                        startActivity(new Intent(getApplicationContext(), LibraryActivity.class).addFlags(
                                Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;

                    case R.id.home_icon:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class).addFlags(
                                Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                }
                return false;
            }
        });
    }

    private ArrayList<YtMediaFiles> loadDummy() {

        ArrayList<YtMediaFiles> randomListData  = new ArrayList<>();
        for(int i=0;i<20;i++) {
            YtMediaFiles myList = new YtMediaFiles("1234", "hello", "Jit", "R.drawable.ic_home", "10:00", "R.drawable.ic_watch_later_icon");
            randomListData.add(myList);
        }
        return randomListData;
    }
}