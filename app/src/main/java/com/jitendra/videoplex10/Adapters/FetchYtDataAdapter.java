package com.jitendra.videoplex10.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.annotations.SerializedName;
import com.jitendra.videoplex10.Model.YtMediaFiles;
import com.jitendra.videoplex10.R;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;

public class FetchYtDataAdapter extends RecyclerView.Adapter<FetchYtDataAdapter.ViewHolder> {
    private Context context;
    private ArrayList<YtMediaFiles> ytMediaFilesList;

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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.vid_title.setText(ytMediaFilesList.get(position).getYtVidTitle());
        holder.vid_channel_name.setText(ytMediaFilesList.get(position).getYtChannel());
        holder.vid_duration.setText("10:00");
//        Glide.with(context).load(Uri.parse(ytMediaFilesList.get(position).getYtChannelIcon()))
//                .into(holder.vid_channel_icon);
        Glide.with(context).load(R.drawable.ic_exo_play_icon)
                .into(holder.vid_channel_icon);
        Glide.with(context).load(R.drawable.one)
                .into(holder.vid_thumb);

        holder.vid_menu_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "show more menu", Toast.LENGTH_SHORT).show();
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
        }
    }
}
