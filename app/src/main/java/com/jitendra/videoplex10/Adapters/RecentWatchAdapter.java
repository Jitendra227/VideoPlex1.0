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
import com.jitendra.videoplex10.VidDatabase.WatchHistoryDb.HistoryVids;

import java.util.ArrayList;
import java.util.List;

public class RecentWatchAdapter extends RecyclerView.Adapter<RecentWatchAdapter.ViewHolder> {

    Context context;
    List<HistoryVids> historyVidsLists;

    public RecentWatchAdapter(Context context, List<HistoryVids> historyVidsLists) {
        this.context = context;
        this.historyVidsLists = historyVidsLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recent,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HistoryVids hv = historyVidsLists.get(position);
        holder.recTitle.setText(hv.hvTitle);
        holder.recChannel.setText(hv.hvChannelName);
        Glide.with(context).load(hv.hvThumbnail).into(holder.recThumb);
    }

    @Override
    public int getItemCount() {
        return historyVidsLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView recThumb;
        TextView recTitle, recChannel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            recThumb = itemView.findViewById(R.id.recent_thumb);
            recChannel = itemView.findViewById(R.id.recent_channel);
            recTitle = itemView.findViewById(R.id.recent_videoName);
        }
    }
}
