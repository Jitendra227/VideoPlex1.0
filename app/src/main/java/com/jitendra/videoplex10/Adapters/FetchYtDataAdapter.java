package com.jitendra.videoplex10.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.jitendra.videoplex10.Config.YoutubeConfig;
import com.jitendra.videoplex10.Model.YoutubeModel.PopularResponse;
import com.jitendra.videoplex10.Model.YoutubeModel.ThumbnailType;
import com.jitendra.videoplex10.Model.YoutubeModel.YtMediaFiles;
import com.jitendra.videoplex10.Network.ApiCallInterface;
import com.jitendra.videoplex10.Network.RetrofitClientInstance;
import com.jitendra.videoplex10.R;
import com.jitendra.videoplex10.Activities.YtVidDetailActivity;
import com.jitendra.videoplex10.VidDatabase.WatchHistoryDb.HistoryVids;
import com.jitendra.videoplex10.VidDatabase.WatchHistoryDb.HistoryVidsDatabase;
import com.jitendra.videoplex10.VidDatabase.WatchLaterDb.WchLaterDatabase;
import com.jitendra.videoplex10.VidDatabase.WatchLaterDb.WchVideos;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FetchYtDataAdapter extends RecyclerView.Adapter<FetchYtDataAdapter.ViewHolder> {
    Context context;
    //ArrayList<String> logos;
    ArrayList<YtMediaFiles> ytMediaFilesList;
    BottomSheetDialog bottomSheetDialog;

    public static final String TAG = "FetchYtDataAdapter";

    public FetchYtDataAdapter(Context context, ArrayList<YtMediaFiles> ytMediaFilesList) {
        this.context = context;
        this.ytMediaFilesList = ytMediaFilesList;
        //this.logos = logos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.yt_stream_data, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        YtMediaFiles videos = ytMediaFilesList.get(position);
//        String logo_url = logos.get(position);
        if(videos!=null) {
            String clickedId = videos.id;
            String vid_channel_id = videos.snippet.channelId;
            holder.vid_title.setText(videos.snippet.title);
            if(videos.snippet.thumbnails != null){
                ThumbnailType thType = videos.snippet.thumbnails.high;
                if (thType == null)
                    thType = videos.snippet.thumbnails.medium;
                if(thType == null)
                    thType = videos.snippet.thumbnails.standard;

                Glide.with(context).load(thType.url)
                        .into(holder.vid_thumb);

            }
            //String logo_url = getChannelLogo(vid_channel_id);
//            Log.d(TAG, "sent logo url --------\n"+ logo_url);
//
//            Glide.with(context).load(logo_url)
//                    .into(holder.vid_channel_icon);


            String fetchedTime = videos.contentDetails.duration;
            fetchedTime = fetchedTime.substring(2);
            char[] ch = fetchedTime.toCharArray();
            for(int i=0;i<ch.length;i++){
                if(ch[i]=='H' || ch[i]=='M')
                    ch[i] = ':';
            }
            ch = Arrays.copyOfRange(ch,0,ch.length-1);
            fetchedTime = new String(ch);

            holder.vid_duration.setText(fetchedTime);
            holder.vid_channel_name.setText(videos.snippet.channelTitle);
        }


        holder.vid_menu_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetTheme);
                View bsView = LayoutInflater.from(context).inflate(R.layout.yt_bottom_sheet,
                        v.findViewById(R.id.yt_bottom_sheet_layout));

                bsView.findViewById(R.id.yt_ll_play_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        YtMediaFiles ytMediaFilesObj = ytMediaFilesList.get(position);
                        String hisId, hisTitle, hisChannel, hisDuration, hisThumb;
                        hisId = ytMediaFilesObj.id;
                        hisTitle = ytMediaFilesObj.snippet.title;
                        hisChannel = ytMediaFilesObj.snippet.channelTitle;
                        hisDuration = ytMediaFilesObj.contentDetails.duration;
                        hisThumb = ytMediaFilesObj.snippet.thumbnails.high.url;

                        class SaveToHistory extends AsyncTask<Void, Void, Void> {
                            @Override
                            protected Void doInBackground(Void... voids) {

                                HistoryVids his = new HistoryVids();
                                his.setHvId(hisId);
                                his.setHvChannelName(hisChannel);
                                his.setHvTitle(hisTitle);
                                his.setHvDuration(hisDuration);
                                his.setHvThumbnail(hisThumb);

                                HistoryVidsDatabase.getHistoryDbInstance(context.getApplicationContext())
                                        .historyVidsDao()
                                        .insertToHistory(his);
                                Log.d(TAG, "doInBackground: added to history");

                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void unused) {
                                super.onPostExecute(unused);

                                Log.d(TAG, "doInBackground: videos in db successful");
                            }
                        }

                        SaveToHistory sth = new SaveToHistory();
                        sth.execute();

                        holder.itemView.performClick();
                        bottomSheetDialog.dismiss();
                    }
                });

                bsView.findViewById(R.id.yt_ll_share_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri = Uri.parse(ytMediaFilesList.get(position).id);
                        Intent shIntent = new Intent(Intent.ACTION_SEND);
                        shIntent.setType("text/plain");
                        //shIntent.setType("https://www.youtube.com/*");
                        shIntent.putExtra(Intent.EXTRA_STREAM,uri);
                        context.startActivity(Intent.createChooser(shIntent,"Share Video via"));
                        bottomSheetDialog.dismiss();
                    }
                });

                bsView.findViewById(R.id.yt_ll_watchLater_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Log.d(TAG, "onClick: sending data to save later");
                        YtMediaFiles ytMediaFilesObj = ytMediaFilesList.get(position);

                        String id, title, channel, duration, thumb ;
                        id = ytMediaFilesObj.id;
                        title = ytMediaFilesObj.snippet.title;
                        channel = ytMediaFilesObj.snippet.channelTitle;
                        duration = ytMediaFilesObj.contentDetails.duration;

                        thumb = ytMediaFilesObj.snippet.thumbnails.high.url;

                        class SaveVideos extends AsyncTask<Void, Void, Void> {

                            @Override
                            protected Void doInBackground(Void... voids) {

                                Log.d(TAG, "doInBackground: adding \n"+id+" "+title+" "+channel+" "+duration+" "+thumb);

                                WchVideos wcVid = new WchVideos();
                                wcVid.setvId(id);
                                wcVid.setvChannelName(channel);
                                wcVid.setvTitle(title);
                                wcVid.setvDuration(duration);
                                wcVid.setvThumbnail(thumb);
                                WchLaterDatabase.getDbInstance(context.getApplicationContext())
                                        .wchVideosDao()
                                        .insertVideos(wcVid);
                                Log.d(TAG, "doInBackground: done adding videos details");

                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void unused) {
                                super.onPostExecute(unused);

                                Log.d(TAG, "doInBackground: videos in db successful");
                            }
                        }

                        SaveVideos sv = new SaveVideos();
                        sv.execute();

                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetDialog.setContentView(bsView);
                bottomSheetDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return ytMediaFilesList.size() ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView vid_thumb, vid_channel_icon, vid_menu_more;
        TextView vid_title, vid_channel_name, vid_duration;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            vid_thumb        = itemView.findViewById(R.id.yt_vid_thumbnail);
            vid_channel_icon = itemView.findViewById(R.id.yt_channel_icon);
            vid_menu_more    = itemView.findViewById(R.id.yt_more_menu);
            vid_title        = itemView.findViewById(R.id.yt_vid_name);
            vid_channel_name = itemView.findViewById(R.id.yt_channel_name);
            vid_duration     = itemView.findViewById(R.id.yt_vid_time);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: sending data to save later");

                    YtMediaFiles ytMediaFilesObj = ytMediaFilesList.get(getAbsoluteAdapterPosition());
                    String hisId, hisTitle, hisChannel, hisDuration, hisThumb;
                    hisId = ytMediaFilesObj.id;
                    hisTitle = ytMediaFilesObj.snippet.title;
                    hisChannel = ytMediaFilesObj.snippet.channelTitle;
                    hisDuration = ytMediaFilesObj.contentDetails.duration;
                    hisThumb = ytMediaFilesObj.snippet.thumbnails.medium.url;

                    class SaveToHistory extends AsyncTask<Void, Void, Void> {
                        @Override
                        protected Void doInBackground(Void... voids) {

                            HistoryVids his = new HistoryVids();
                            his.setHvId(hisId);
                            his.setHvChannelName(hisChannel);
                            his.setHvTitle(hisTitle);
                            his.setHvDuration(hisDuration);
                            his.setHvThumbnail(hisThumb);

                            HistoryVidsDatabase.getHistoryDbInstance(context.getApplicationContext())
                                    .historyVidsDao()
                                    .insertToHistory(his);
                            Log.d(TAG, "doInBackground: added to history");

                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void unused) {
                            super.onPostExecute(unused);

                            Log.d(TAG, "doInBackground: videos in db successful");
                        }
                    }

                    SaveToHistory sth = new SaveToHistory();
                    sth.execute();

                    Intent intent = new Intent(context, YtVidDetailActivity.class);
                    intent.putExtra("videoId", ytMediaFilesObj.id);
                    intent.putExtra("vidTitle", ytMediaFilesObj.snippet.title);
                    intent.putExtra("channelName", ytMediaFilesObj.snippet.channelTitle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                }
            });
        }
    }
    public String getChannelLogo(String chnlId) {
        String chUrl =  YoutubeConfig.CH_Middle
                + YoutubeConfig.ch
                + chnlId + YoutubeConfig.KEY;
        String[] logoUrl=new String[1];

        Log.d(TAG, "getChannelLogo: searching logo for"+ chnlId);
        Call<PopularResponse> call = RetrofitClientInstance.getRetrofitInstance().
                create(ApiCallInterface.class).getChannelLogo(chUrl);
        call.enqueue(new Callback<PopularResponse>() {
            @Override
            public void onResponse(Call<PopularResponse> call, Response<PopularResponse> response) {
                PopularResponse popularResponse = response.body();

                if(popularResponse != null){

                    logoUrl[0] = popularResponse.items.get(0).snippet.thumbnails.dft.url;
                    Log.d(TAG, "found_logo----->\n "+logoUrl[0]);

                    if(popularResponse.items.get(0).snippet.thumbnails != null) {
                        ThumbnailType thType = popularResponse.items.get(0).snippet.thumbnails.dft;
                        if (thType == null)
                            thType = popularResponse.items.get(0).snippet.thumbnails.medium;
                        if (thType == null)
                            thType = popularResponse.items.get(0).snippet.thumbnails.high;


                        logoUrl[0] = thType.url;
                    }
                }
            }

            @Override
            public void onFailure(Call<PopularResponse> call, Throwable t) {
                Log.d(TAG, "onResponse: unable to Search video");
            }
        });
        Log.d(TAG, "found_logo----->\n "+logoUrl[0]);
        return logoUrl[0];
    }
}
