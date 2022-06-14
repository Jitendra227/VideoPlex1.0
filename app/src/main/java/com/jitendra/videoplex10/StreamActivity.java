package com.jitendra.videoplex10;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.gson.Gson;
import com.jitendra.videoplex10.Adapters.FetchYtDataAdapter;
import com.jitendra.videoplex10.Model.YoutubeModel.SearchResponse;
import com.jitendra.videoplex10.Model.YoutubeModel.YtMediaFiles;
import com.jitendra.videoplex10.Network.ApiCallInterface;
import com.jitendra.videoplex10.Network.RetrofitClientInstance;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    String ytUrl = "https://youtube.googleapis.com/youtube/v3/videos?part=snippet%2CcontentDetails%2Cstatistics&chart=mostPopular&maxResults=20&regionCode=US&key=AIzaSyDdcgZZhCL09EkHNh3JmPAsocL_CePOCCQ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream);
        Log.d(TAG, "\nonCreate: Starting Video\n");

        recyclerView  = findViewById(R.id.ac_stream_recyclerView);

        apiCallInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiCallInterface.class);
        ytMediaFilesArrayList= new ArrayList<>();
        //retrieveData();

        retrieveYtData();
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

    private void retrieveYtData(){
        Toast.makeText(this, "Loading videos", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "retrieveYtData: started retrieving");
        Call<SearchResponse> call = apiCallInterface.getAllVideos();
        call.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                SearchResponse searchResponse = response.body();
                if(searchResponse != null){
                    if(searchResponse.items.size()>0){
                        for(int i=0;i<4;i++){
                            ytMediaFilesArrayList.add(searchResponse.items.get(i));
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
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                Toast.makeText(StreamActivity.this, "An error has occurred!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: Failed to load youtube videos");
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
            //YtMediaFiles myList = new YtMediaFiles("1234", "hello", "Jit", "R.drawable.ic_home", "10:00", "R.drawable.ic_watch_later_icon");
            //randomListData.add(myList);
        }
        return randomListData;
    }
}