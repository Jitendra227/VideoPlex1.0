package com.jitendra.videoplex10.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.jitendra.videoplex10.Activities.WatchLaterActivity;
import com.jitendra.videoplex10.Model.YoutubeModel.ThumbnailType;
import com.jitendra.videoplex10.Model.YoutubeModel.YtMediaFiles;
import com.jitendra.videoplex10.R;
import com.jitendra.videoplex10.Activities.YtVidDetailActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class FetchYtDataAdapter extends RecyclerView.Adapter<FetchYtDataAdapter.ViewHolder> {
    Context context;
    ArrayList<YtMediaFiles> ytMediaFilesList;
    BottomSheetDialog bottomSheetDialog;

    public static final String TAG = "FetchYtDataAdapter";

    public FetchYtDataAdapter(Context context, ArrayList<YtMediaFiles> ytMediaFilesList) {
        this.context = context;
        this.ytMediaFilesList = ytMediaFilesList;
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
        if(videos!=null) {
            String clickedId = videos.id;
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

        Glide.with(context).load(R.drawable.ic_exo_play_icon)
                .into(holder.vid_channel_icon);

        holder.vid_menu_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetTheme);
                View bsView = LayoutInflater.from(context).inflate(R.layout.yt_bottom_sheet,
                        v.findViewById(R.id.yt_bottom_sheet_layout));

                bsView.findViewById(R.id.yt_ll_play_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
//                        YtMediaFiles ytMediaFilesObj = holder.itemView;
//                        Intent intent = new Intent(context, WatchLaterActivity.class);
//
//                        intent.putExtra("allinfo", holder.itemView.toString());
//
//                        intent.putExtra("vidId", "1234");
//                        intent.putExtra("videoName", holder.vid_title.toString());
//                        intent.putExtra("vidThumb", holder.vid_thumb.toString());
//                        intent.putExtra("vidChannel", holder.vid_channel_name.toString());
//                        intent.putExtra("vidDuration", holder.vid_duration.toString());
//
                        Log.d(TAG, "onClick: sending data to save later");
                        //holder.itemView.performClick();
                        YtMediaFiles ytMediaFilesObj = ytMediaFilesList.get(position);

                        Intent wchIntent = new Intent(context, WatchLaterActivity.class);
                        wchIntent.putExtra("vidId",  ytMediaFilesObj.id);
                        wchIntent.putExtra("videoName", ytMediaFilesObj.snippet.title);
                        wchIntent.putExtra("vidThumb", ytMediaFilesObj.snippet.thumbnails.standard);
                        wchIntent.putExtra("vidChannel", ytMediaFilesObj.snippet.channelTitle);
                        wchIntent.putExtra("vidDuration", ytMediaFilesObj.contentDetails.duration);
                        Log.d(TAG, "Data sent---->"+ytMediaFilesObj.id +"  "+
                                ytMediaFilesObj.snippet.title+"  "+ ytMediaFilesObj.snippet.thumbnails.standard+ "  "+
                                ytMediaFilesObj.snippet.channelTitle+"  "+ ytMediaFilesObj.contentDetails.duration);
                        context.startActivity(wchIntent);

                        //LocalBroadcastManager.getInstance(context).sendBroadcast(wchIntent);

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
        return ytMediaFilesList.size();
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

                    Log.d(TAG, "onClick: sending video id->");

                    YtMediaFiles ytMediaFilesObj = ytMediaFilesList.get(getAbsoluteAdapterPosition());

                    Intent intent = new Intent(context, YtVidDetailActivity.class);
                    intent.putExtra("videoId", ytMediaFilesObj.id);
                    intent.putExtra("vidTitle",ytMediaFilesObj.snippet.title);
                    intent.putExtra("channelName", ytMediaFilesObj.snippet.channelTitle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);




                }
            });
        }
    }
}
