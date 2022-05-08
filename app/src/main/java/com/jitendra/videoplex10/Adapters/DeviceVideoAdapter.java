package com.jitendra.videoplex10.Adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.ContentView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jitendra.videoplex10.Model.DeviceVideoItems;
import com.jitendra.videoplex10.R;

import java.util.ArrayList;

public class DeviceVideoAdapter extends RecyclerView.Adapter<DeviceVideoAdapter.VideoViewHolder> {

    private Context context;
    private ArrayList<DeviceVideoItems> videoItemList;
    private VideoItemClickListener clickListener;

    public DeviceVideoAdapter(Context context, ArrayList<DeviceVideoItems> videoItemList, VideoItemClickListener clickListener) {
        this.context = context;
        this.videoItemList = videoItemList;
        this.clickListener = clickListener;
    }


    @NonNull
    @Override
    public DeviceVideoAdapter.VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_video_list,parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceVideoAdapter.VideoViewHolder holder, int position) {
        DeviceVideoItems deviceVideoItems = videoItemList.get(position);
        Glide.with(context)
                .load("file://"+ videoItemList.get(position).getVidThumbNail())
                .skipMemoryCache(false)
                .into(holder.ivThumbnail);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onVideoClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoItemList.size();
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivThumbnail;
        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            ivThumbnail = itemView.findViewById(R.id.vid_lst_thumbnail);
        }
    }

    public interface VideoItemClickListener {
        void onVideoClick(int pos);
    }
}
