package com.jitendra.videoplex10.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.youtube.player.FragmentYtubePlayer;
import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.jitendra.videoplex10.Adapters.WatchLaterAdapter;
import com.jitendra.videoplex10.R;
import com.jitendra.videoplex10.VidDatabase.WchLaterDatabase;
import com.jitendra.videoplex10.VidDatabase.WchVideos;

import java.util.ArrayList;
import java.util.List;

public class WatchLaterActivity extends AppCompatActivity {

    public static final String TAG = "WatchLaterVideos";

    BottomNavigationView bottomNavigationView;
    private WatchLaterAdapter watchLaterAdapter;
    private RecyclerView recyclerView;
    private ArrayList<WchVideos> videosArrayList;

    String videoId,videoName, channelName,videoDuration, videoThumb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_later);

        recyclerView = findViewById(R.id.wch_later_rv_videos);

        getVideosToSave();

        getSavedVideos();


    }

    public void getVideosToSave() {
        Intent intent = getIntent();
        videoId = intent.getStringExtra("vidId");
        videoName = intent.getStringExtra("videoName");
        channelName = intent.getStringExtra("vidChannel");
        videoDuration = intent.getStringExtra("vidDuration");
        videoThumb = intent.getStringExtra("vidThumb");


        Log.d(TAG, "List---> " + videoId + "   " + videoName + "   " + channelName + "   "
                + "   " + videoDuration);


        class SaveVideos extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                WchVideos videos = new WchVideos();
                videos.setvId(videoId);
                videos.setvChannelName(channelName);
                videos.setvTitle(videoName);
                videos.setvDuration(videoDuration);
                videos.setvThumbnail(videoThumb);
                Log.d("WatchLaterVideos", "doInBackground: adding videos----------- ");

                WchLaterDatabase.getDbInstance(getApplicationContext())
                        .wchVideosDao()
                        .insertVideos(videos);
                Log.d(TAG, "doInBackground: done adding videos----------- ");
                Log.d(TAG, "doInBackground: "+videoId +"   "+ videoName+ "   "+ channelName+ "   "
                + videoThumb+ "   "+ videoDuration);
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                super.onPostExecute(unused);
                finish();
                Log.d("WatchLaterVideos", "doInBackground: videos in db successful----------- ");
                Toast.makeText(WatchLaterActivity.this, "Added to database", Toast.LENGTH_SHORT).show();
            }
        }

        SaveVideos sv = new SaveVideos();
        sv.execute();

    }


    private void getSavedVideos() {
        class GetAllWatchLaterVideos extends AsyncTask<Void, Void, List<WchVideos>> {

            @Override
            protected List<WchVideos> doInBackground(Void... voids) {
                List<WchVideos> wchVideosArrayList = WchLaterDatabase
                        .getDbInstance(getApplicationContext())
                        .wchVideosDao()
                        .getAll();

                return wchVideosArrayList;
            }

            @Override
            protected void onPostExecute(List<WchVideos> wchVideosList) {
                super.onPostExecute(wchVideosList);
                WatchLaterAdapter adapter = new WatchLaterAdapter(WatchLaterActivity.this, wchVideosList);
                recyclerView.setAdapter(adapter);
            }
        }

        GetAllWatchLaterVideos gv = new GetAllWatchLaterVideos();
        gv.execute();
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