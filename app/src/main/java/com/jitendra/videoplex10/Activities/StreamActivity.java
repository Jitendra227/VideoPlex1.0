package com.jitendra.videoplex10.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.jitendra.videoplex10.Adapters.FetchYtDataAdapter;
import com.jitendra.videoplex10.Config.YoutubeConfig;
import com.jitendra.videoplex10.Model.YoutubeModel.PopularResponse;
import com.jitendra.videoplex10.Model.YoutubeModel.ThumbnailType;
import com.jitendra.videoplex10.Model.YoutubeModel.YtMediaFiles;
import com.jitendra.videoplex10.Network.ApiCallInterface;
import com.jitendra.videoplex10.Network.RetrofitClientInstance;
import com.jitendra.videoplex10.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StreamActivity extends AppCompatActivity {

    private static final String TAG = "StreamActivity";

    BottomNavigationView bottomNavigationView;
    private ApiCallInterface apiCallInterface;
    private RecyclerView recyclerView;
    private ArrayList<YtMediaFiles> ytMediaFilesArrayList;
    private FetchYtDataAdapter fetchYtDataAdapter;
    public ArrayList<String> logoArrayList;

    ImageView searchBtn, settingBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream);
        Log.d(TAG, "\nonCreate: Starting Video\n");

        recyclerView  = findViewById(R.id.ac_stream_recyclerView);

        apiCallInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiCallInterface.class);
        ytMediaFilesArrayList = new ArrayList<>();
        //logoArrayList = new ArrayList<>();

        retrieveYtData();
        fetchYtDataAdapter = new FetchYtDataAdapter(this, ytMediaFilesArrayList);
        recyclerView.setAdapter(fetchYtDataAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        searchBtn = findViewById(R.id.ac_main_toolbar_search);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StreamActivity.this, SearchYtVideosActivity.class);
                startActivity(intent);
            }
        });

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

    private void retrieveYtData(){
        Toast.makeText(this, "Loading videos", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "retrieveYtData: started retrieving");
        Call<PopularResponse> call = apiCallInterface.getAllPopularVideos();
        call.enqueue(new Callback<PopularResponse>() {
            @Override
            public void onResponse(Call<PopularResponse> call, Response<PopularResponse> response) {
                PopularResponse popularResponse = response.body();
                if(popularResponse != null){
                    if(popularResponse.items.size()>0){
                        for(int i=0;i<20;i++){
                            ytMediaFilesArrayList.add(popularResponse.items.get(i));
//                            String url = popularResponse.items.get(i).snippet.channelId;
//
//                            String chUrl =  YoutubeConfig.CHANNEL_URL+YoutubeConfig.CH_Middle
//                                    + YoutubeConfig.ch
//                                    + url + YoutubeConfig.KEY;
//
//                            Log.d(TAG, "getChannelLogo: searching logo for"+ chUrl);
//                            Call<PopularResponse> call2 = RetrofitClientInstance.getRetrofitInstance().
//                                    create(ApiCallInterface.class).getChannelLogo(chUrl);
//                            int finalI = i;
//                            call2.enqueue(new Callback<PopularResponse>() {
//                                @Override
//                                public void onResponse(Call<PopularResponse> call2, Response<PopularResponse> response1) {
//                                    PopularResponse popularResponse1 = response1.body();
//
//                                    if(popularResponse1 != null){
//                                            logoArrayList.add(popularResponse1.items.get(finalI).snippet
//                                            .thumbnails.dft.url);
//                                        }
//                                    return;
//                                }
//                                @Override
//                                public void onFailure(Call<PopularResponse> call2, Throwable t1) {
//                                    Log.d(TAG, "onResponse: unable to Search video");
//                                }
//                            });

                        }
                        fetchYtDataAdapter.notifyDataSetChanged();
                    }
                }
                else {
                    Log.d(TAG, "onResponse: unable to find video");
                    Toast.makeText(StreamActivity.this, "No video available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PopularResponse> call, Throwable t) {
                Toast.makeText(StreamActivity.this, "An error has occurred!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: Failed to load youtube videos");
            }
        });
    }
}