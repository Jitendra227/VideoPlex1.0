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
import com.jitendra.videoplex10.VidDatabase.Videos;

import java.util.ArrayList;
import java.util.List;

public class WatchLaterAdapter extends RecyclerView.Adapter<WatchLaterAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Videos> videosList;

    public WatchLaterAdapter(Context context, ArrayList<Videos> videosList) {
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
        holder.wch_channelName.setText(videosList.get(position).vChannelName);
        holder.wch_duration.setText(videosList.get(position).vDuration);
        holder.wch_vidName.setText(videosList.get(position).vTitle);
        Glide.with(context).load(videosList.get(position).vThumbnail)
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
