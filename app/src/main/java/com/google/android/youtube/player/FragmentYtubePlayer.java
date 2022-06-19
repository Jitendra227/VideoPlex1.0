package com.google.android.youtube.player;

import static com.jitendra.videoplex10.Config.YoutubeConfig.KEY;

import android.os.Bundle;
import android.util.Log;

public class FragmentYtubePlayer extends YouTubePlayerSupportFragmentX
        implements YouTubePlayer.OnInitializedListener {

    private static final String TAG = "FragmentYtubePlayer";
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    public static final String KEY_VIDEO_ID = "KEY_VIDEO_ID";

    private OnVideoPlayListener onVideoPlayListener;

    public interface OnVideoPlayListener {
        void onPlaying(String vidId);
    }

    public void setOnVideoPlayListener(OnVideoPlayListener onVideoPlaying) {
        this.onVideoPlayListener = onVideoPlaying;
    }

    String vidId;

    public FragmentYtubePlayer() {}

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        final Bundle arguments = getArguments();

        if(bundle!=null && bundle.containsKey(KEY_VIDEO_ID.toString())){
            vidId = bundle.getString(KEY_VIDEO_ID);
        }
        else {
            try {
                vidId = arguments.getString(KEY_VIDEO_ID);
            }
            catch (Exception e){
                Log.d(TAG, "onCreate: "+e);
            }
        }
        initialize(KEY,this);
    }

    public void setVideoId(final String mVidId) {
        vidId = mVidId;
        initialize(KEY,this);
        Log.d(TAG, "setVideoId: "+ KEY);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer youTubePlayer, boolean b) {
        Log.d(TAG, "onInitializationSuccess: playing");
        if(vidId!=null){
            if(b){
                youTubePlayer.play();
            }
            else{
                youTubePlayer.loadVideo(vidId);
            }

            if(onVideoPlayListener!=null) {
                onVideoPlayListener.onPlaying(vidId);
            }
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

        if(youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(getActivity(), 1).show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle = new Bundle();
        super.onSaveInstanceState(bundle);
        bundle.putString(KEY_VIDEO_ID,vidId);
    }
}
