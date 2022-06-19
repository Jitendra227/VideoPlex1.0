package com.jitendra.videoplex10;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.youtube.player.FragmentYtubePlayer;
import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.jitendra.videoplex10.Adapters.WatchLaterAdapter;
import com.jitendra.videoplex10.VidDatabase.Videos;
import com.jitendra.videoplex10.VidDatabase.WatchLaterDb;

import java.util.ArrayList;
import java.util.List;

public class WatchLaterActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private WatchLaterAdapter watchLaterAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Videos> videosArrayList;

    String videoId,videoName, channelName,videoDuration, videoThumb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_later);

        recyclerView = findViewById(R.id.wch_later_rv_videos);

        videosArrayList = getSavedVideos();
        saveVideos();
        watchLaterAdapter = new WatchLaterAdapter(this, videosArrayList);
        recyclerView.setAdapter(watchLaterAdapter);


        //playSavedVideos();
    }

    public ArrayList<Videos> getSavedVideos() {
        ArrayList<Videos> videoFiles = new ArrayList<>();
        Intent intent = getIntent();
        videoId = intent.getStringExtra("vidId");
        videoName = intent.getStringExtra("videoName");
        channelName = intent.getStringExtra("vidChannel");
        videoDuration = intent.getStringExtra("vidDuration");
        videoThumb = intent.getStringExtra("vidThumb");

        Videos videosObj = new Videos(videoId,videoName,channelName,videoThumb,videoDuration);
        videoFiles.add(videosObj);

        return videoFiles;
    }

    public void saveVideos(){
        WatchLaterDb db = WatchLaterDb.getDbInstance(this.getApplicationContext());

        Videos videos = new Videos();
        videos.vId = videoId;
        videos.vTitle = videoName;
        videos.vChannelName = channelName;
        videos.vThumbnail = videoThumb;
        videos.vDuration = videoDuration;
        db.videosDao().insertVideos(videos);
        finish();
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
        fragmentYtubePlayer.setVideoId(videoId);
    }
}