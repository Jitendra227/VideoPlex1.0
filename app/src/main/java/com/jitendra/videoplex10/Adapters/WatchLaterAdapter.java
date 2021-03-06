package com.jitendra.videoplex10.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.jitendra.videoplex10.R;
import com.jitendra.videoplex10.VidDatabase.WatchLaterDb.WchLaterDatabase;
import com.jitendra.videoplex10.VidDatabase.WatchLaterDb.WchVideos;

import java.util.List;

public class WatchLaterAdapter extends RecyclerView.Adapter<WatchLaterAdapter.ViewHolder> {

    private Context context;
    private List<WchVideos> videosList;
    BottomSheetDialog bottomSheetDialog;

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
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        WchVideos wch = videosList.get(position);
        holder.wch_channelName.setText(wch.vChannelName);
        holder.wch_duration.setText(wch.vDuration);
        holder.wch_vidName.setText(wch.vTitle);
        Glide.with(context).load(wch.vThumbnail)
                .into(holder.wch_thumb);

        holder.wch_menu_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetTheme);
                View bsView = LayoutInflater.from(context).inflate(R.layout.watch_later_bottom_sheet,
                        v.findViewById(R.id.watch_later_bottom_sheet_layout));

                bsView.findViewById(R.id.watch_later_play_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.itemView.performClick();
                        bottomSheetDialog.dismiss();
                    }
                });

                bsView.findViewById(R.id.watch_later_remove_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Are you sure?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteTask(wch);
                                videosList.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(holder.getAbsoluteAdapterPosition(), videosList.size());
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });

                        AlertDialog ad = builder.create();
                        ad.show();
                        bottomSheetDialog.dismiss();
                    }
                });

                bsView.findViewById(R.id.watch_later_share_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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

    private void deleteTask(WchVideos wch) {
        class RemoveFromWatchList extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                WchLaterDatabase.getDbInstance(context.getApplicationContext())
                        .wchVideosDao()
                        .deleteWchVideos(wch);
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                super.onPostExecute(unused);
                Toast.makeText(context, "Removed from watchlist", Toast.LENGTH_SHORT).show();
            }
        }

        RemoveFromWatchList rm = new RemoveFromWatchList();
        rm.execute();
    }
}
