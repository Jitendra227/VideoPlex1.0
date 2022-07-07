package com.jitendra.videoplex10.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.youtube.player.FragmentYtubePlayer;
import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.jitendra.videoplex10.Adapters.WatchLaterAdapter;
import com.jitendra.videoplex10.Model.YoutubeModel.ThumbnailType;
import com.jitendra.videoplex10.R;
import com.jitendra.videoplex10.VidDatabase.WchLaterDatabase;
import com.jitendra.videoplex10.VidDatabase.WchVideos;

import java.util.ArrayList;
import java.util.List;

public class WatchLaterActivity extends AppCompatActivity {

    public static final String TAG = "WatchLaterVideos";

    BottomNavigationView bottomNavigationView;
    private WatchLaterAdapter watchLaterAdapter;
    private RecyclerView recyclerView;
    private ArrayList<WchVideos> videosArrayList;

    ImageView backBtn, sortBtn, deleteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_later);

        backBtn = findViewById(R.id.watch_later_go_back);
        sortBtn = findViewById(R.id.watch_later_sort);
        deleteBtn = findViewById(R.id.watch_later_delete_all);

        recyclerView = findViewById(R.id.wch_later_rv_videos);
        bottomNavigationView = findViewById(R.id.wch_later_bottom_nav_bar);

        getSavedVideos();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WatchLaterActivity.this.finish();
            }
        });

        sortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(WatchLaterActivity.this);
                builder.setTitle("Do you want to Remove all?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        DeleteSavedVideos dt = new DeleteSavedVideos();
                        dt.execute();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });

                AlertDialog ad = builder.create();
                ad.show();
            }
        });

       // bottomNavigationView = findViewById(R.id.bottom_nav_bar);
        bottomNavigationView.setSelectedItemId(R.id.library_icon);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.stream_icon:
                        startActivity(new Intent(getApplicationContext(), StreamActivity.class).addFlags(
                                Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;

                    case R.id.library_icon:
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


    class DeleteSavedVideos extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            WchLaterDatabase.getDbInstance(getApplicationContext())
                    .wchVideosDao()
                    .deleteAll();
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            finish();
        }
    }


    private void getSavedVideos() {
        class GetAllWatchLaterVideos extends AsyncTask<Void, Void, List<WchVideos>> {

            @Override
            protected List<WchVideos> doInBackground(Void... voids) {
                List<WchVideos> wchVideosList = WchLaterDatabase
                        .getDbInstance(getApplicationContext())
                        .wchVideosDao()
                        .getAll();

                return wchVideosList;
            }

            @Override
            protected void onPostExecute(List<WchVideos> wchVideosList) {
                super.onPostExecute(wchVideosList);
                WatchLaterAdapter adapter = new WatchLaterAdapter(WatchLaterActivity.this, wchVideosList);
                recyclerView.setAdapter(adapter);
            }
        }

        GetAllWatchLaterVideos gv = new GetAllWatchLaterVideos();
        gv.execute();
    }


    private void playSavedVideos() {
        final YouTubeInitializationResult result = YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(this);
        if(result != YouTubeInitializationResult.SUCCESS) {
            result.getErrorDialog(this, 0).show();
            return;
        }
        final FragmentYtubePlayer fragmentYtubePlayer =
                (FragmentYtubePlayer)getSupportFragmentManager()
                        .findFragmentById(R.id.fragment_youtube);
        //fragmentYtubePlayer.setVideoId(videoId);
    }
}