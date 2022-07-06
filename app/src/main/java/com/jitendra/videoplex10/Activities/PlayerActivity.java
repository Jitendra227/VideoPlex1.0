package com.jitendra.videoplex10.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.ArraySet;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.audiofx.LoudnessEnhancer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Pair;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.jitendra.videoplex10.Model.DeviceMediaFiles;
import com.jitendra.videoplex10.R;

import java.io.File;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener, AudioManager.OnAudioFocusChangeListener, GestureDetector.OnGestureListener {

    PlayerView playerView;
    ExoPlayer player;
    TrackSelector trackSelector;
    int position;
    String videoTitle;
    ArrayList<DeviceMediaFiles> dmVideoFiles = new ArrayList<>();
    TextView title;
    ConcatenatingMediaSource concatenatingMediaSource;
    ImageView nextBtn, prevBtn, fullScreenBtn, menu_more, lock, unlock, goBack;
    private ControlsMode controlsMode;
    public enum ControlsMode{
        LOCK,FULLSCREEN;
    }
    RelativeLayout root;
    boolean changeToFullScreen = false;
    AudioManager audioManager;
    LoudnessEnhancer loudnessEnhancer;
    int volume = 0;
    int brightness = 0;
    ArraySet<Pair<String, String>> audioLanguages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTOFullScreen();
        setContentView(R.layout.activity_player);

        playerView = findViewById(R.id.exoplayer_View);
        position = getIntent().getIntExtra("position",1);
        videoTitle = getIntent().getStringExtra("video_title");
        dmVideoFiles = getIntent().getExtras().getParcelableArrayList("videoArrayList");
        lock = findViewById(R.id.vid_lock);
        unlock = findViewById(R.id.exo_unlock);
        root = findViewById(R.id.cstm_pb_rl_layout);
        goBack = findViewById(R.id.video_back);



        fullScreenBtn = findViewById(R.id.exo_fullscreen_icon);
        nextBtn = findViewById(R.id.exo_next);
        prevBtn = findViewById(R.id.exo_prev);
        menu_more = findViewById(R.id.video_more);



        trackSelector = new DefaultTrackSelector();
        trackSelector.setParameters(
                trackSelector.getParameters().buildUpon().setMaxVideoSizeSd()
                        .setPreferredTextLanguage("en")
                        .setPreferredAudioLanguage("en").build());


        menu_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(PlayerActivity.this, menu_more);

                popupMenu.getMenuInflater().inflate(R.menu.video_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        Toast.makeText(PlayerActivity.this, "You Clicked " + menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

        title = findViewById(R.id.video_title);
        title.setText(videoTitle);
        goBack.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        prevBtn.setOnClickListener(this);
        fullScreenBtn.setOnClickListener(this);
        lock.setOnClickListener(this);
        unlock.setOnClickListener(this);

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
        for(int i = 0; i < player.getCurrentTrackGroups().length; i++){
            String format = player.getCurrentTrackGroups().get(i).getFormat(0).sampleMimeType;
            String lang = player.getCurrentTrackGroups().get(i).getFormat(0).language;
            String id = player.getCurrentTrackGroups().get(i).getFormat(0).id;

            System.out.println(player.getCurrentTrackGroups().get(i).getFormat(0));
            if(format.contains("audio") && id != null && lang != null){
                //System.out.println(lang + " " + id);

                audioLanguages.add(new Pair<>(id, lang));
            }
        }
        trackSelector.setParameters(trackSelector.getParameters().buildUpon().setPreferredAudioLanguage("eng").build());

        playerView.setPlayer(player);
        trackSelector.setParameters(trackSelector.getParameters().buildUpon().setPreferredAudioLanguage("en").build());
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

            case R.id.video_back:
                if(player!=null) {
                    player.pause();
                    player.release();
                }
                finish();
                break;
            case R.id.vid_lock:
                controlsMode = ControlsMode.FULLSCREEN;
                root.setVisibility(View.VISIBLE);
                lock.setVisibility(View.INVISIBLE);
                Toast.makeText(this, "View is unlocked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.exo_unlock:
                controlsMode = ControlsMode.LOCK;
                root.setVisibility(View.INVISIBLE);
                lock.setVisibility(View.VISIBLE);
                Toast.makeText(this, "View is locked", Toast.LENGTH_SHORT).show();
                break;
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

    @Override
    public void onAudioFocusChange(int focusChange) {

    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

}