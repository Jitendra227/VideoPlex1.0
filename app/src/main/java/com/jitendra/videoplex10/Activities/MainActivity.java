package com.jitendra.videoplex10.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.jitendra.videoplex10.Adapters.DeviceMFilesAdapter;
import com.jitendra.videoplex10.Model.DeviceMediaFiles;
import com.jitendra.videoplex10.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    private RecyclerView recyclerView;

    private ArrayList<DeviceMediaFiles> deviceMediaFilesArrayList;
    private DeviceMFilesAdapter deviceMFilesAdapter;
    private ImageView menuMore;

    private static final int STORAGE_PERMISSION = 123;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menuMore = findViewById(R.id.ac_main_menu_more);
        bottomNavigationView = findViewById(R.id.bottom_nav_bar);
        bottomNavigationView.setSelectedItemId(R.id.home_icon);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.stream_icon:
                        startActivity(new Intent(getApplicationContext(), StreamActivity.class).addFlags(
                                Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;

                    case R.id.library_icon:
                        startActivity(new Intent(getApplicationContext(), LibraryActivity.class).addFlags(
                                Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;

                    case R.id.home_icon:
                        return true;
                }
                return false;
            }
        });
        recyclerView = findViewById(R.id.ac_main_recycler_view);
        recyclerView.setAdapter(deviceMFilesAdapter);


        if (checkAndRequestPermission()) {
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
//        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        deviceMFilesAdapter.notifyDataSetChanged();

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private ArrayList<DeviceMediaFiles> loadDeviceMedia() {
        ContentResolver contentResolver = getContentResolver();
        ArrayList<DeviceMediaFiles> videoFiles = new ArrayList<>();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        if (cursor != null && cursor.moveToNext()) {
            do {
                String id = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                String displayName = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
                String size = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
                String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                String dateAdded = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED));

                DeviceMediaFiles deviceMediaFiles = new DeviceMediaFiles(id, title, displayName,
                        size, duration, path, dateAdded);

                videoFiles.add(deviceMediaFiles);
            } while (cursor.moveToNext());
        }
        return videoFiles;
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

}