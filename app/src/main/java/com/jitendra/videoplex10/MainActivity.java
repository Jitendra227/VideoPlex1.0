package com.jitendra.videoplex10;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import com.jitendra.videoplex10.Adapters.DeviceVideoAdapter;
import com.jitendra.videoplex10.Model.DeviceVideoItems;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DeviceVideoAdapter.VideoItemClickListener {

    private RecyclerView rvDeviceVideo;
    private ArrayList<DeviceVideoItems> videoItemsArrayList;
    private DeviceVideoAdapter deviceVideoAdapter;
    private static final int STORAGE_PERMISSION = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvDeviceVideo = findViewById(R.id.ac_main_recycler_view);
        videoItemsArrayList = new ArrayList<>();
        deviceVideoAdapter = new DeviceVideoAdapter(this, videoItemsArrayList, this::onVideoClick);

        rvDeviceVideo.setAdapter(deviceVideoAdapter);

        if (checkAndRequestPermission()) {
            loadDeviceVideos();
        }

    }

    private void loadDeviceVideos() {
        ContentResolver contentResolver = getContentResolver();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri,null, null,null, null);

        if(cursor!=null && cursor.moveToFirst()){
            do{
                String videoTitle = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                String videoPath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                String videoThumb = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA));

                videoItemsArrayList.add(new DeviceVideoItems(videoTitle, videoPath, videoThumb));
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
        intent.putExtra("vidName", videoItemsArrayList.get(pos).getVidName());
        intent.putExtra("vidPath", videoItemsArrayList.get(pos).getVidPath());
        startActivity(intent);
    }
}