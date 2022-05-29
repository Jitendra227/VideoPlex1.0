package com.jitendra.videoplex10;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.jitendra.videoplex10.Model.DeviceMediaFiles;

import java.io.File;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener {

    PlayerView playerView;
    ExoPlayer player;
    int position;
    String videoTitle;
    ArrayList<DeviceMediaFiles> dmVideoFiles = new ArrayList<>();
    TextView title;
    ConcatenatingMediaSource concatenatingMediaSource;
    ImageView nextBtn, prevBtn, fullScreenBtn;
    boolean changeToFullScreen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTOFullScreen();
        setContentView(R.layout.activity_player);

        playerView = findViewById(R.id.exoplayer_View);
        position = getIntent().getIntExtra("position",1);
        videoTitle = getIntent().getStringExtra("video_title");
        dmVideoFiles = getIntent().getExtras().getParcelableArrayList("videoArrayList");

        fullScreenBtn = findViewById(R.id.exo_fullscreen_icon);
        nextBtn = findViewById(R.id.exo_next);
        prevBtn = findViewById(R.id.exo_prev);

        title = findViewById(R.id.video_title);
        title.setText(videoTitle);

        nextBtn.setOnClickListener(this);
        prevBtn.setOnClickListener(this);
        fullScreenBtn.setOnClickListener(this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        fullScreenBtn.setOnClickListener(new View.OnClickListener()) {
//            @Override
//            public void onClick(View v) {
//                if(changeToFullScreen){
//                    fullScreenBtn.setImageDrawable(ContextCompat
//                            .getDrawable(PlayerActivity.this, R.drawable.ic_exo_fullscreen_expand));
//                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                    changeToFullScreen = false;
//                }
//                else {
//                    fullScreenBtn.setImageDrawable(ContextCompat.getDrawable(PlayerActivity.this, R.drawable.ic_exo_fullscreen_exit));
//                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                    changeToFullScreen = true;
//                }
//            }
//        });

        playMyVideo();
    }

    private void playMyVideo() {
        String path = dmVideoFiles.get(position).getvPath();
        Uri uri = Uri.parse(path);
        player = new ExoPlayer.Builder(this)
                .setSeekForwardIncrementMs(10000)
                .setSeekBackIncrementMs(10000)
                .build();
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "app"));

        concatenatingMediaSource = new ConcatenatingMediaSource();
        for(int i=0;i<dmVideoFiles.size();i++) {
            new File(String.valueOf(dmVideoFiles.get(i)));
            MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(String.valueOf(uri)));

            concatenatingMediaSource.addMediaSource(mediaSource);
        }

        playerView.setPlayer(player);
        playerView.setKeepScreenOn(true);
        player.prepare(concatenatingMediaSource);
        player.seekTo(position, C.TIME_UNSET);
        playerError();

    }



    private void playerError() {
        player.addListener(new Player.Listener() {
            @Override
            public void onPlayerError(PlaybackException error) {
                Toast.makeText(PlayerActivity.this, "Video Playing Error", Toast.LENGTH_SHORT).show();
            }
        });

        player.setPlayWhenReady(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(player.isPlaying()) {
            player.stop();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        player.setPlayWhenReady(false);
        player.getPlaybackState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        player.setPlayWhenReady(true);
        player.getPlaybackState();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        player.setPlayWhenReady(true);
        player.getPlaybackState();
    }


    private void setTOFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            
            case R.id.exo_next:
                try {
                    player.stop();
                    position++;
                    playMyVideo();
                }catch (Exception e) {
                    if(position==dmVideoFiles.size())
                        Toast.makeText(this, "end of video list", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;

            case R.id.exo_prev:
                try {
                    player.stop();
                    position--;
                    playMyVideo();
                } catch (Exception e){
                    Toast.makeText(this, "end of video list", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;

            case R.id.exo_fullscreen_icon:
                if(changeToFullScreen){
                    fullScreenBtn.setImageDrawable(ContextCompat
                            .getDrawable(PlayerActivity.this, R.drawable.ic_exo_fullscreen_expand));
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    changeToFullScreen = false;
                }
                else {
                    fullScreenBtn.setImageDrawable(ContextCompat.getDrawable(PlayerActivity.this, R.drawable.ic_exo_fullscreen_exit));
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    changeToFullScreen = true;
                }
        }
    }
}