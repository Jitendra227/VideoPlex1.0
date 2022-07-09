package com.jitendra.videoplex10.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.jitendra.videoplex10.Model.YoutubeSearchModel.ThumnailsType;
import com.jitendra.videoplex10.Model.YoutubeSearchModel.YtSearchedVideos;
import com.jitendra.videoplex10.R;
import com.jitendra.videoplex10.Activities.YtVidDetailActivity;

import java.util.ArrayList;

public class SearchYtDataAdapter extends RecyclerView.Adapter<SearchYtDataAdapter.ViewHolder> {

    Context context;
    ArrayList<YtSearchedVideos> ytSearchedVideosList;
    BottomSheetDialog bottomSheetDialog;

    public static final String TAG = "SearchedYtDataAdapter";

    public SearchYtDataAdapter(Context context, ArrayList<YtSearchedVideos> ytSearchedVideosList) {
        this.context = context;
        this.ytSearchedVideosList = ytSearchedVideosList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.yt_searched_video_data, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.setIsRecyclable(false);
        YtSearchedVideos videos = ytSearchedVideosList.get(position);
        if(videos!=null) {
            String clickedVidId = videos.id.videoId;
            String sch_channel_id = videos.snippet.channelId;
            holder.sch_vid_title.setText(videos.snippet.title);
            if(videos.snippet.thumbnails != null) {
                ThumnailsType thTypes = videos.snippet.thumbnails.high;
                if(thTypes == null)
                    thTypes = videos.snippet.thumbnails.medium;
                if(thTypes == null)
                    thTypes = videos.snippet.thumbnails.standard;

                Glide.with(context).load(thTypes.url)
                        .into(holder.sch_vid_thumb);
            }
            holder.sch_vid_channel_name.setText(videos.snippet.channelTitle);
            holder.sch_vid_menu_more.setOnClickListener(new View.OnClickListener() {
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

//                    bsView.findViewById(R.id.yt_ll_watchLater_layout).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Log.d(TAG, "onClick: adding to watch later->");
//                            Toast.makeText(context, "adding to watch later", Toast.LENGTH_SHORT).show();
//                            YtSearchedVideos ytSearchedVideosObj2 = ytSearchedVideosList.get(position);
//                            Intent intent = new Intent(context, WatchLaterActivity.class);
//                            intent.putExtra("videoId", ytSearchedVideosObj2.id.videoId);
//                            intent.putExtra("vidTitle",ytSearchedVideosObj2.snippet.title);
//                            intent.putExtra("channelName", ytSearchedVideosObj2.snippet.channelTitle);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        }
//                    });



                    bottomSheetDialog.setContentView(bsView);
                    bottomSheetDialog.show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return ytSearchedVideosList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView sch_vid_thumb, sch_vid_menu_more;
        TextView sch_vid_title, sch_vid_channel_name;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            sch_vid_thumb = itemView.findViewById(R.id.yt_search_thumbnail);
            sch_vid_title = itemView.findViewById(R.id.yt_search_vid_name);
            sch_vid_channel_name = itemView.findViewById(R.id.yt_search_channel_name);
            sch_vid_menu_more = itemView.findViewById(R.id.yt_search_more_menu);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: sending video id->");
                    YtSearchedVideos ytSearchedVideosObj = ytSearchedVideosList.get(getAbsoluteAdapterPosition());
                    Intent intent = new Intent(context, YtVidDetailActivity.class);
                    intent.putExtra("videoId", ytSearchedVideosObj.id.videoId);
                    intent.putExtra("vidTitle",ytSearchedVideosObj.snippet.title);
                    intent.putExtra("channelName", ytSearchedVideosObj.snippet.channelTitle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                }
            });
        }
    }
}
