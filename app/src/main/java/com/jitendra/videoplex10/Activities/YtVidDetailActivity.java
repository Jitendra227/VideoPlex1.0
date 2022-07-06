package com.jitendra.videoplex10.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.FragmentYtubePlayer;
import com.jitendra.videoplex10.R;

public class YtVidDetailActivity extends AppCompatActivity {

    TextView ytVideoTitle, ytChannelName;
    String vidId,vidTitle,chName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yt_vid_detail);

        ytVideoTitle = findViewById(R.id.yt_video_title_det);
        ytChannelName = findViewById(R.id.yt_channel_name_det);
        
        getVideo();
        playVideo();
    }

    private void getVideo() {
        Intent intent = getIntent();
        vidId = intent.getStringExtra("videoId");
        vidTitle = intent.getStringExtra("vidTitle");
        chName = intent.getStringExtra("channelName");

        ytVideoTitle.setText(vidTitle);
        ytChannelName.setText(chName);
    }

    private void playVideo() {
        final YouTubeInitializationResult result = YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(this);
        if(result != YouTubeInitializationResult.SUCCESS) {
            result.getErrorDialog(this, 0).show();
            return;
        }
        final FragmentYtubePlayer fragmentYtubePlayer =
                (FragmentYtubePlayer)getSupportFragmentManager()
                .findFragmentById(R.id.fragment_youtube);
        fragmentYtubePlayer.setVideoId(vidId);
    }
}





