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
import com.jitendra.videoplex10.Model.YtMediaFiles;
import com.jitendra.videoplex10.R;

import java.util.ArrayList;

public class FetchYtDataAdapter extends RecyclerView.Adapter<FetchYtDataAdapter.ViewHolder> {
    private Context context;
    private ArrayList<YtMediaFiles> ytMediaFilesList;
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
