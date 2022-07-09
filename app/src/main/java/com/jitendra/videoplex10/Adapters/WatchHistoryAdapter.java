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
import com.jitendra.videoplex10.VidDatabase.WatchHistoryDb.HistoryVids;
import com.jitendra.videoplex10.VidDatabase.WatchHistoryDb.HistoryVidsDatabase;
import com.jitendra.videoplex10.VidDatabase.WatchLaterDb.WchLaterDatabase;

import java.util.List;

public class WatchHistoryAdapter extends RecyclerView.Adapter<WatchHistoryAdapter.ViewHolder> {

    private Context context;
    private List<HistoryVids> historyVidsList;
    BottomSheetDialog bottomSheetDialog;

    public WatchHistoryAdapter(Context context, List<HistoryVids> historyVidsList) {
        this.context = context;
        this.historyVidsList = historyVidsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.watch_history_videos_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        HistoryVids hist = historyVidsList.get(position);
        holder.his_title.setText(hist.hvTitle);
        holder.his_channel.setText(hist.hvChannelName);
        holder.his_duration.setText(hist.hvDuration);
        Glide.with(context).load(hist.hvThumbnail)
                .into(holder.his_thumb);

        holder.his_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetTheme);
                View bsView = LayoutInflater.from(context).inflate(R.layout.watch_history_bottom_sheet,
                        v.findViewById(R.id.wch_his_btm_sheet_layout));

                bsView.findViewById(R.id.wch_his_btm_play_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.itemView.performClick();
                        bottomSheetDialog.dismiss();
                    }
                });

                bsView.findViewById(R.id.wch_his_btm_remove_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Are you sure?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteVideo(hist);
                                historyVidsList.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(holder.getAbsoluteAdapterPosition(), historyVidsList.size());
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

                bsView.findViewById(R.id.wch_his_btm_later_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                    }
                });

                bsView.findViewById(R.id.wch_his_btm_share_layout).setOnClickListener(new View.OnClickListener() {
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
        return historyVidsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView his_thumb, his_menu;
        TextView his_channel, his_title, his_duration;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            his_thumb = itemView.findViewById(R.id.wch_his_lst_thumbnail);
            his_channel = itemView.findViewById(R.id.wch_his_lst_channel_name);
            his_title = itemView.findViewById(R.id.wch_his_lst_videoName);
            his_duration = itemView.findViewById(R.id.wch_his_lst_duration);
            his_menu = itemView.findViewById(R.id.wch_his_lst_menu_more);
        }
    }

    private void deleteVideo(HistoryVids hist) {
        class RemoveFromHistoryList extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                HistoryVidsDatabase.getHistoryDbInstance(context.getApplicationContext())
                        .historyVidsDao()
                        .deleteFromHistory(hist);
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                super.onPostExecute(unused);
                Toast.makeText(context, "Removed from history", Toast.LENGTH_SHORT).show();
            }
        }

        RemoveFromHistoryList rm = new RemoveFromHistoryList();
        rm.execute();
    }
}
