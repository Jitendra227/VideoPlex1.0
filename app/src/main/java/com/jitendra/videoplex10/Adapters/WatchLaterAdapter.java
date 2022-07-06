package com.jitendra.videoplex10.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jitendra.videoplex10.R;
import com.jitendra.videoplex10.VidDatabase.WchVideos;

import java.util.ArrayList;
import java.util.List;

public class WatchLaterAdapter extends RecyclerView.Adapter<WatchLaterAdapter.ViewHolder> {

    private Context context;
    private List<WchVideos> videosList;

    public WatchLaterAdapter(Context context, List<WchVideos> videosList) {
        this.context = context;
        this.videosList = videosList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.watch_later_videos_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WchVideos wch = videosList.get(position);
        holder.wch_channelName.setText(wch.vChannelName);
        holder.wch_duration.setText(wch.vDuration);
        holder.wch_vidName.setText(wch.vTitle);
        Glide.with(context).load(wch.vThumbnail)
                .into(holder.wch_thumb);
    }

    @Override
    public int getItemCount() {
        return videosList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView wch_thumb, wch_menu_more;
        TextView wch_vidName, wch_channelName,wch_duration;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            wch_thumb    = itemView.findViewById(R.id.watch_later_thumbnail);
            wch_menu_more = itemView.findViewById(R.id.watch_later_menu_more);
            wch_vidName     = itemView.findViewById(R.id.watch_later_videoName);
            wch_channelName     = itemView.findViewById(R.id.watch_later_channel_name);
            wch_duration = itemView.findViewById(R.id.watch_later_duration);
        }
    }
}
