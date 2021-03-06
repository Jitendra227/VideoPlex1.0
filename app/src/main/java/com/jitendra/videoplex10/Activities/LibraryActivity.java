package com.jitendra.videoplex10.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.jitendra.videoplex10.Adapters.RecentWatchAdapter;
import com.jitendra.videoplex10.Adapters.WatchHistoryAdapter;
import com.jitendra.videoplex10.R;
import com.jitendra.videoplex10.VidDatabase.WatchHistoryDb.HistoryVids;
import com.jitendra.videoplex10.VidDatabase.WatchHistoryDb.HistoryVidsDatabase;
import com.jitendra.videoplex10.VidDatabase.WatchLaterDb.WchLaterDatabase;
import com.jitendra.videoplex10.VidDatabase.WatchLaterDb.WchVideos;

import java.util.ArrayList;
import java.util.List;

public class LibraryActivity extends AppCompatActivity {

    LinearLayout watch_later_layout, history_layout;
    RecyclerView recyclerView;
    TextView unwatched;

    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        unwatched = findViewById(R.id.tv_unwatched);
        recyclerView = findViewById(R.id.ac_lib_recycler_view);
        history_layout = findViewById(R.id.history_box_layout);
        watch_later_layout = findViewById(R.id.watch_later_box_layout);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

       // getTotalUnWatch();
        getRecentWatch();

        watch_later_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LibraryActivity.this, WatchLaterActivity.class);
                startActivity(intent);
            }
        });

        history_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent hIntent = new Intent(LibraryActivity.this, WatchHistoryActivity.class);
                startActivity(hIntent);
            }
        });

        bottomNavigationView = findViewById(R.id.bottom_nav_bar);
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
    private void getRecentWatch() {
        class MyHistoryVideos extends AsyncTask<Void, Void, List<HistoryVids>> {

            @Override
            protected List<HistoryVids> doInBackground(Void... voids) {
                List<HistoryVids> hVidList = HistoryVidsDatabase
                        .getHistoryDbInstance(getApplicationContext())
                        .historyVidsDao()
                        .getFullHistory();

                return hVidList;
            }

            @Override
            protected void onPostExecute(List<HistoryVids> historyVids) {
                super.onPostExecute(historyVids);
                RecentWatchAdapter recentWatchAdapter = new RecentWatchAdapter(LibraryActivity.this, historyVids);
                recyclerView.setAdapter(recentWatchAdapter);
            }
        }
        MyHistoryVideos myHist = new MyHistoryVideos();
        myHist.execute();
    }

    private void getTotalUnWatch() {
//        class WatchListCount extends AsyncTask<Void, Void, List<WchVideos>> {
//
//            @Override
//            protected List<WchVideos> doInBackground(Void... voids) {
//                List<WchVideos> wchLst = WchLaterDatabase
//                        .getDbInstance(getApplicationContext())
//                        .wchVideosDao()
//                        .getAll();
//                int n = wchLst.size();
//                unwatched.setText(n+" unwatched videos");
//                return wchLst;
//            }
//
//            @Override
//            protected void onPostExecute(List<WchVideos> wchVideos) {
//                super.onPostExecute(wchVideos);
//                finish();
//            }
//        }
//        WatchListCount wl = new WatchListCount();
//        wl.execute();
    }

}