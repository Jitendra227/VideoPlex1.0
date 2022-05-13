package com.jitendra.videoplex10;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import com.jitendra.videoplex10.Adapters.DeviceMFilesAdapter;
import com.jitendra.videoplex10.Adapters.DeviceVideoAdapter;
import com.jitendra.videoplex10.Model.DeviceMediaFiles;
import com.jitendra.videoplex10.Model.DeviceVideoItems;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DeviceVideoAdapter.VideoItemClickListener {

    private RecyclerView recyclerView;
//    private ArrayList<DeviceVideoItems> videoItemsArrayList;

   //newly created adapter
    private ArrayList<DeviceMediaFiles> deviceMediaFilesArrayList;
    private DeviceMFilesAdapter deviceMFilesAdapter;
    //------------------------------------------------

    private DeviceVideoAdapter deviceVideoAdapter;
    private static final int STORAGE_PERMISSION = 123;
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.ac_main_recycler_view);
        //videoItemsArrayList = new ArrayList<>();
        //deviceVideoAdapter = new DeviceVideoAdapter(this, videoItemsArrayList, this::onVideoClick);

        recyclerView.setAdapter(deviceVideoAdapter);

        if (checkAndRequestPermission()) {
            //showVideos();
            showDeviceVideos();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void showDeviceVideos() {
        deviceMediaFilesArrayList = loadDeviceMedia();
        deviceMFilesAdapter = new DeviceMFilesAdapter(this, deviceMediaFilesArrayList);
        recyclerView.setAdapter(deviceMFilesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                RecyclerView.VERTICAL, false));
        deviceMFilesAdapter.notifyDataSetChanged();

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private ArrayList<DeviceMediaFiles> loadDeviceMedia() {
        ContentResolver contentResolver = getContentResolver();
        ArrayList<DeviceMediaFiles> videoFiles = new ArrayList<>();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        if(cursor!= null && cursor.moveToNext()) {
            do {
                String id = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                String displayName = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
                String size = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
                String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                String dateAdded = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED));

                DeviceMediaFiles deviceMediaFiles = new DeviceMediaFiles(id, title, displayName,
                        size, duration,path,dateAdded);

                videoFiles.add(deviceMediaFiles);
            }while(cursor.moveToNext());
        }
        return videoFiles;
    }

    private void showVideos() {
        ContentResolver contentResolver = getContentResolver();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri,null, null,null, null);

        if(cursor!=null && cursor.moveToNext()){
            do{

                String videoTitle = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                String videoPath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                String videoThumb = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA));

                //videoItemsArrayList.add(new DeviceVideoItems(videoTitle, videoPath, videoThumb));
            }while(cursor.moveToNext());
        }
        deviceVideoAdapter.notifyDataSetChanged();
    }

    private boolean checkAndRequestPermission() {
        int storageWrite = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int storageRead = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (storageWrite != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (storageRead != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]), STORAGE_PERMISSION);
            return false;
        }
        return true;
    }

    @Override
    public void onVideoClick(int pos) {
        Intent intent = new Intent(MainActivity.this, VideoPlayerActivity.class);
//        intent.putExtra("vidName", videoItemsArrayList.get(pos).getVidName());
//        intent.putExtra("vidPath", videoItemsArrayList.get(pos).getVidPath());
        startActivity(intent);
    }
}