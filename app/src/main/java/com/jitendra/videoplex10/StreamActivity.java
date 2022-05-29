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
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.gson.Gson;
import com.jitendra.videoplex10.Adapters.FetchYtDataAdapter;
import com.jitendra.videoplex10.Config.YoutubeConfig;
import com.jitendra.videoplex10.Model.YtMediaFiles;
import com.jitendra.videoplex10.Network.ApiCallInterface;
import com.jitendra.videoplex10.Network.RetrofitClientInstance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StreamActivity extends YouTubeBaseActivity {

    private static final String TAG = "StreamActivity";

    BottomNavigationView bottomNavigationView;
    private ApiCallInterface apiCallInterface;
    private RecyclerView recyclerView;
    private ArrayList<YtMediaFiles> ytMediaFilesArrayList;
    private FetchYtDataAdapter fetchYtDataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream);
        Log.d(TAG, "\nonCreate: Starting Video\n");

        recyclerView  = findViewById(R.id.ac_stream_recyclerView);

        apiCallInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiCallInterface.class);
        ytMediaFilesArrayList= new ArrayList<>();
        //retrieveData();

        ytMediaFilesArrayList = loadDummy();
        fetchYtDataAdapter = new FetchYtDataAdapter(this, ytMediaFilesArrayList);
        recyclerView.setAdapter(fetchYtDataAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        //fetchYtDataAdapter.notifyDataSetChanged();

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

    private void retrieveData() {
        Toast.makeText(this,"Fetching Data...",Toast.LENGTH_SHORT).show();
        Call<List<YtMediaFiles>> call = apiCallInterface.getPopularVideos();

        call.enqueue(new Callback<List<YtMediaFiles>>() {
            @Override
            public void onResponse(Call<List<YtMediaFiles>> call, Response<List<YtMediaFiles>> response) {
                if(!response.isSuccessful()) {
                    Log.d(TAG, "onResponse: Youtube Data Failure!..");
                    return;
                }
                Log.d(TAG, "onResponse: Youtube Data Load Success..");

                Gson gson = new Gson();
                String data = gson.toJson(response.body());
                ytMediaFilesArrayList.clear();
                ytMediaFilesArrayList.addAll(response.body());
//
//                for(YtMediaFiles items: ytMediaFilesArrayList) {
//
//                }
            }

            @Override
            public void onFailure(Call<List<YtMediaFiles>> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
                t.printStackTrace();
                Toast.makeText(StreamActivity.this, "You are offline!", Toast.LENGTH_SHORT).show();
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