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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.jitendra.videoplex10.Model.YoutubeModel.ThumbnailType;
import com.jitendra.videoplex10.Model.YoutubeModel.YtMediaFiles;
import com.jitendra.videoplex10.R;

import java.util.ArrayList;
import java.util.Arrays;

public class FetchYtDataAdapter extends RecyclerView.Adapter<FetchYtDataAdapter.ViewHolder> {
    Context context;
    ArrayList<YtMediaFiles> ytMediaFilesList;
    BottomSheetDialog bottomSheetDialog;

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

        YtMediaFiles videos = ytMediaFilesList.get(position);
        if(videos!=null) {
            holder.vid_title.setText(videos.snippet.title);
            if(videos.snippet.thumbnail != null){
                ThumbnailType thType = videos.snippet.thumbnail.high;
                if (thType == null)
                    thType = videos.snippet.thumbnail.medium;
                if(thType == null)
                    thType = videos.snippet.thumbnail.standard;

                Glide.with(context).load(thType.thUrl)
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

                bsView.findViewById(R.id.ll_play_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.itemView.performClick();
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
        }
    }
}
