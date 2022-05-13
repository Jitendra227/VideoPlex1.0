package com.jitendra.videoplex10.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jitendra.videoplex10.Model.DeviceMediaFiles;
import com.jitendra.videoplex10.R;
import com.jitendra.videoplex10.VideoPlayerActivity;

import java.io.File;
import java.util.ArrayList;

public class DeviceMFilesAdapter extends RecyclerView.Adapter<DeviceMFilesAdapter.ViewHolder> {
    private Context context;
    private ArrayList<DeviceMediaFiles> mediaFilesList;

    public DeviceMFilesAdapter(Context context, ArrayList<DeviceMediaFiles> mediaFilesList) {
        this.context = context;
        this.mediaFilesList = mediaFilesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.device_video_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.vid_name.setText(mediaFilesList.get(position).getvDisplayName());
        String size = mediaFilesList.get(position).getvSize();
        holder.vid_size.setText(android.text.format.Formatter.formatFileSize(context,
                Long.parseLong(size)));

        double milliSec = Double.parseDouble(mediaFilesList.get(position).getvDuration());
        holder.vid_duration.setText(timeConverter((long) milliSec));

        Glide.with(context).load(new File(mediaFilesList.get(position).getvPath()))
                .into(holder.vid_thumb);

        holder.vid_menuMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "show more menu", Toast.LENGTH_SHORT).show();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoPlayerActivity.class);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mediaFilesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView vid_thumb, vid_menuMore;
        TextView vid_name, vid_size, vid_duration;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            vid_thumb    = itemView.findViewById(R.id.vid_lst_thumbnail);
            vid_menuMore = itemView.findViewById(R.id.vid_lst_menu_more);
            vid_name     = itemView.findViewById(R.id.vid_lst_videoName);
            vid_size     = itemView.findViewById(R.id.vid_lst_videoSize);
            vid_duration = itemView.findViewById(R.id.vid_lst_duration);
        }
    }

    @SuppressLint("DefaultLocale")
    public String timeConverter(long time){
        String videoTime;
        int duration = (int)time;
        int sec = (duration / 1000) % 60;
        int min = (duration / (1000 * 60)) % 60;
        int hrs = duration  / (1000 * 60 * 60);

        if(hrs>0){
            videoTime = String.format("%02d:%02d:%02d", hrs, min, sec);
        }
        else{
            videoTime = String.format("%02d:%02d", min, sec);
        }

        return videoTime;
    }
}
