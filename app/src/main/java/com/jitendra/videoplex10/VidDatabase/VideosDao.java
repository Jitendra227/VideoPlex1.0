package com.jitendra.videoplex10.VidDatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Index;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface VideosDao {

    @Query("SELECT * FROM videos")
    List<Videos> getAllVideos();

    @Insert
    void insertVideos(Videos... videos);

    @Delete
    void deleteVideos(Videos videos);
}
