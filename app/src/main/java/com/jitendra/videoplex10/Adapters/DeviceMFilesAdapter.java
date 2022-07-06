package com.jitendra.videoplex10.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.jitendra.videoplex10.Model.DeviceMediaFiles;
import com.jitendra.videoplex10.Activities.PlayerActivity;
import com.jitendra.videoplex10.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class DeviceMFilesAdapter extends RecyclerView.Adapter<DeviceMFilesAdapter.ViewHolder> {
    private Context context;
    private ArrayList<DeviceMediaFiles> mediaFilesList;
    BottomSheetDialog bottomSheetDialog;

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
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
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
                bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetTheme);
                View bottomLatView = LayoutInflater.from(context).inflate(R.layout.device_bottom_sheet,
                        v.findViewById(R.id.bottom_sheet_layout));

                bottomLatView.findViewById(R.id.ll_play_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.itemView.performClick();
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomLatView.findViewById(R.id.ll_rename_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder alertDiag = new AlertDialog.Builder(context);
                        alertDiag.setTitle("             Rename ");
                        EditText editText = new EditText(context);
                        String path = mediaFilesList.get(position).getvPath();
                        final File file = new File(path);
                        String videoName = file.getName();
                        videoName = videoName.substring(0,videoName.lastIndexOf("."));
                        editText.setText(videoName);
                        alertDiag.setView(editText);
                        editText.requestFocus();

                        alertDiag.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String onlyPath = Objects.requireNonNull(file.getParentFile()).getAbsolutePath();
                                String ext = file.getAbsolutePath();
                                ext = ext.substring(ext.lastIndexOf("."));
                                String newPath = onlyPath+"/"+editText.getText().toString()+ext;
                                File newFile = new File(newPath);
                                boolean rename = file.renameTo(newFile);
                                if(rename){
                                    ContentResolver contentResolver = context.getApplicationContext().getContentResolver();
                                    contentResolver.delete(MediaStore.Files.getContentUri("external"),
                                            MediaStore.MediaColumns.DATA+"=?",
                                            new String[]
                                                    {file.getAbsolutePath()});
                                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                                    intent.setData(Uri.fromFile(newFile));
                                    context.getApplicationContext().sendBroadcast(intent);

                                    notifyDataSetChanged();
                                    Toast.makeText(context, "video renamed", Toast.LENGTH_SHORT).show();

                                    //to refresh app and load changed data
                                    SystemClock.sleep(100);
                                    ((Activity) context).recreate();
                                }
                                else {
                                    Toast.makeText(context, "failed attempt", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        alertDiag.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDiag.create().show();
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomLatView.findViewById(R.id.ll_share_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri = Uri.parse(mediaFilesList.get(position).getvPath());
                        Intent shIntent = new Intent(Intent.ACTION_SEND);
                        shIntent.setType("video/*");
                        shIntent.putExtra(Intent.EXTRA_STREAM,uri);
                        context.startActivity(Intent.createChooser(shIntent,"Share Video via"));
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomLatView.findViewById(R.id.ll_delete_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder alertDiag = new AlertDialog.Builder(context);
                        alertDiag.setTitle("Delete");
                        alertDiag.setMessage("Do u want to delete this video?");
                        alertDiag.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Uri contentUri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                                        Long.parseLong(mediaFilesList.get(position).getvId()));
                                File file = new File(mediaFilesList.get(position).getvPath());
                                boolean delete = file.delete();

                                if(delete){
                                    context.getContentResolver().delete(contentUri,null,null);
                                    mediaFilesList.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, mediaFilesList.size());
                                    Toast.makeText(context, "Video Deleted", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(context, "Failed attempt", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        alertDiag.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDiag.show();
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomLatView.findViewById(R.id.ll_details_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder alertDiag = new AlertDialog.Builder(context);
                        alertDiag.setTitle("Details");
                        String one = "File: " + mediaFilesList.get(position).getvDisplayName();
                        String path = mediaFilesList.get(position).getvPath();
                        int indexOfPath = path.lastIndexOf("/");
                        String two = "Path: " + path.substring(0,indexOfPath);
                        String three = "Size: " + android.text.format.Formatter
                                .formatFileSize(context, Long.parseLong(mediaFilesList.get(position).getvSize()));
                        String four = "Length: " + timeConverter((long)milliSec);
                        String vidFormat = mediaFilesList.get(position).getvDisplayName();
                        int index = vidFormat.lastIndexOf(".");
                        String five = "Format: " + vidFormat.substring(index+1) + " video";

                        MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
                        metadataRetriever.setDataSource(mediaFilesList.get(position).getvPath());
                        String height = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
                        String width = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
                        String six = "Resolution: " + width+"x"+height;

                        alertDiag.setMessage(one+"\n\n"+two+"\n\n"+three+"\n"+four+"\n"+five+"\n"+six+"\n");

                        AlertDialog alert = alertDiag.create();
                        alert.show();
                        alert.getWindow().setGravity(Gravity.BOTTOM);
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetDialog.setContentView(bottomLatView);
                bottomSheetDialog.show();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlayerActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("video_title", mediaFilesList.get(position).getvDisplayName());
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("videoArrayList", mediaFilesList);
                intent.putExtras(bundle);
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
