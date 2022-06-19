package com.jitendra.videoplex10;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jitendra.videoplex10.Adapters.SearchYtDataAdapter;
import com.jitendra.videoplex10.Config.YoutubeConfig;
import com.jitendra.videoplex10.Model.YoutubeSearchModel.SearchResponse;
import com.jitendra.videoplex10.Model.YoutubeSearchModel.YtSearchedVideos;
import com.jitendra.videoplex10.Network.ApiCallInterface;
import com.jitendra.videoplex10.Network.RetrofitVidSearchInstance;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchYtVideosActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private ApiCallInterface apiCallInterface;
    private RecyclerView recyclerView;
    private ArrayList<YtSearchedVideos> ytSearchedVideosFiles;
    private SearchYtDataAdapter searchYtDataAdapter;
    EditText videoQuery;

    public static final String TAG = "Search Youtube Videos";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_yt_videos);
        Log.d(TAG, "onCreate: Searching videos");

        videoQuery = findViewById(R.id.yt_search_video);
        recyclerView = findViewById(R.id.rv_load_searched_video);

        apiCallInterface = RetrofitVidSearchInstance.getRetrofitVidSearchInstance().create(ApiCallInterface.class);
        ytSearchedVideosFiles = new ArrayList<>();

        searchYtDataAdapter = new SearchYtDataAdapter(this, ytSearchedVideosFiles);
        recyclerView.setAdapter(searchYtDataAdapter);
        videoQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(videoQuery.getText().toString())) {
                    getSearchedVideo(videoQuery.getText().toString());
                }
                else {
                    videoQuery.setText("");
                }
            }
        });
    }

    private void getSearchedVideo(String query) {
        String url = YoutubeConfig.SEARCH_BASE_URL + YoutubeConfig.sch + YoutubeConfig.mxResults +
                YoutubeConfig.QUERY + query + YoutubeConfig.Type + YoutubeConfig.KEY;
        Log.d(TAG, "getSearchedVideo: started searching");
        Call<SearchResponse> call = apiCallInterface.getSearchedVideos(url);
        call.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                ytSearchedVideosFiles.clear();
                SearchResponse searchResponse = response.body();
                if(searchResponse != null) {
                    if(searchResponse.items.size() > 0) {
                        for(int i=0;i<5;i++){
                            ytSearchedVideosFiles.add(searchResponse.items.get(i));
                        }
                        searchYtDataAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                Log.d(TAG, "onResponse: unable to Search video");
                Toast.makeText(SearchYtVideosActivity.this, "No video available", Toast.LENGTH_SHORT).show();
            }
        });
    }
}