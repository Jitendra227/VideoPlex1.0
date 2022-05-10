package com.jitendra.videoplex10.Adapters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jitendra.videoplex10.Model.DeviceMediaFiles;
import com.jitendra.videoplex10.PlayerActivity;
import com.jitendra.videoplex10.R;
import com.jitendra.videoplex10.VideoPlayerActivity;

import java.util.ArrayList;

public class DeviceMFilesAdapter extends RecyclerView.Adapter<DeviceMFilesAdapter.ViewHolder> {
    private Context context;
    private ArrayList<DeviceMediaFiles> mediaFiles;

    public DeviceMFilesAdapter(Context context, ArrayList<DeviceMediaFiles> mediaFiles) {
        this.context = context;
        this.mediaFiles = mediaFiles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.device_video_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.vid_name.setText(mediaFiles.get(position).getvDisplayName());
        String size = mediaFiles.get(position).getvSize();
        holder.vid_size.setText(android.text.format.Formatter.formatFileSize(context,
                Long.parseLong(size)));

        holder.vid_duration.setText("10:00");
        holder.vid_menuMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "menu more", Toast.LENGTH_SHORT).show();
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
        return mediaFiles.size();
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
}
