package com.jitendra.videoplex10.VidDatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WchVideosDao {

    @Query("SELECT * FROM wchvideos")
    List<WchVideos> getAll();

    @Insert()
    void insertVideos(WchVideos... wchVideos);

    @Delete
    void deleteWchVideos(WchVideos wchVideos);
}
