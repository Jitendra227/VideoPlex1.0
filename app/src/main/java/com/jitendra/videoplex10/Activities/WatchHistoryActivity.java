package com.jitendra.videoplex10.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.jitendra.videoplex10.Adapters.WatchHistoryAdapter;
import com.jitendra.videoplex10.R;
import com.jitendra.videoplex10.VidDatabase.WatchHistoryDb.HistoryVids;
import com.jitendra.videoplex10.VidDatabase.WatchHistoryDb.HistoryVidsDatabase;

import java.security.PrivateKey;
import java.util.List;

public class WatchHistoryActivity extends AppCompatActivity {
    public static final String TAG = "WatchHistoryActivity";

    BottomNavigationView bottomNavigationView;
    ImageView goBackBtn, deleteBtn;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_history);

        goBackBtn = findViewById(R.id.wch_his_go_back);
        deleteBtn = findViewById(R.id.wch_his_delete_all);

        recyclerView = findViewById(R.id.wch_his_rv_videos);
        bottomNavigationView = findViewById(R.id.wch_his_nav_bar);

        getHistoryVideos();

        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WatchHistoryActivity.this.finish();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(WatchHistoryActivity.this);
                builder.setTitle("Your full history will be removed! ?");
                builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        DeleteHistoryVideos delHis = new DeleteHistoryVideos();
                        delHis.execute();
                    }
                });
                builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });

                AlertDialog ad = builder.create();
                ad.show();
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.library_icon);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.stream_icon:
                        startActivity(new Intent(getApplicationContext(), StreamActivity.class).addFlags(
                                Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.library_icon:
                        return true;

                    case R.id.home_icon:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class).addFlags(
                                Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

    }

    private void getHistoryVideos() {
        class MyHistoryVideos extends AsyncTask<Void, Void, List<HistoryVids>>{

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
                WatchHistoryAdapter hisAdapter = new WatchHistoryAdapter(WatchHistoryActivity.this, historyVids);
                recyclerView.setAdapter(hisAdapter);
            }
        }
        MyHistoryVideos myHist = new MyHistoryVideos();
        myHist.execute();
    }

    class DeleteHistoryVideos extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            HistoryVidsDatabase.getHistoryDbInstance(getApplicationContext())
                    .historyVidsDao()
                    .deleteFullHistory();
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            finish();
        }
    }
}