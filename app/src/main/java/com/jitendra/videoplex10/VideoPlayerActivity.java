package com.jitendra.videoplex10;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

public class VideoPlayerActivity extends AppCompatActivity {

    private TextView videoTitleCTV, videoTimeCTV;
    private ImageView backwardImg, playPauseImg, forwardImg;
    private SeekBar seekBar;
    private VideoView videoView;
    private RelativeLayout customRL, videoPyRL;
    boolean vidIsOpen = true;
    private String videoName, videoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        videoName = getIntent().getStringExtra("vidName");
        videoPath = getIntent().getStringExtra("vidPath");

        //init
        videoTitleCTV = findViewById(R.id.custom_tv_title);
        videoTimeCTV = findViewById(R.id.custom_tv_time);
        backwardImg = findViewById(R.id.custom_iv_backward);
        playPauseImg = findViewById(R.id.custom_iv_play);
        forwardImg = findViewById(R.id.custom_iv_forward);
        seekBar = findViewById(R.id.custom_seekBar);
        videoView = findViewById(R.id.vidPlayer_vv_video);
        customRL = findViewById(R.id.custom_control_layout_rl);
        videoPyRL = findViewById(R.id.vidPlayer_rl);


        videoView.setVideoURI(Uri.parse(videoPath));
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                seekBar.setMax(videoView.getDuration());
                videoView.start();
            }
        });

        videoTitleCTV.setText(videoPath);
        backwardImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.seekTo(videoView.getDuration()-1000);
            }
        });

        playPauseImg.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                if(videoView.isPlaying()){
                    videoView.pause();
                    playPauseImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_icon));
                }
                else{
                    videoView.start();
                    playPauseImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_icon));
                }
            }
        });

        forwardImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.seekTo(videoView.getDuration()+1000);
            }
        });


        videoPyRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vidIsOpen){
                    hideControls();
                    vidIsOpen = false;
                }
                else{
                    showControls();
                    vidIsOpen = true;
                }
            }
        });

    }

    private void hideControls() {
        customRL.setVisibility(View.GONE);

        final Window window = this.getWindow();
        if(window == null){
            return;
        }
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        View decorView = window.getDecorView();
        if(decorView!= null){
            int uiOption = decorView.getSystemUiVisibility();
            if(Build.VERSION.SDK_INT>=14){
                uiOption |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
            }
            if(Build.VERSION.SDK_INT>=16){
                uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            }
            if(Build.VERSION.SDK_INT>=19){
                uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }
            decorView.setSystemUiVisibility(uiOption);
        }
    }

    private void showControls() {
        customRL.setVisibility(View.VISIBLE);

        final Window window = this.getWindow();
        if(window == null){
            return;
        }
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        View decorView = window.getDecorView();
        if(decorView!= null){
            int uiOption = decorView.getSystemUiVisibility();
            if(Build.VERSION.SDK_INT>=14){
                uiOption &= ~View.SYSTEM_UI_FLAG_LOW_PROFILE;
            }
            if(Build.VERSION.SDK_INT>=16){
                uiOption &= ~View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            }
            if(Build.VERSION.SDK_INT>=19){
                uiOption &= ~ View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }
            decorView.setSystemUiVisibility(uiOption);
        }

    }
}