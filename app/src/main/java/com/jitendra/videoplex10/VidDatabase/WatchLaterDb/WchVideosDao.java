package com.jitendra.videoplex10.VidDatabase.WatchLaterDb;

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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVideos(WchVideos... wchVideos);

    @Delete
    void deleteWchVideos(WchVideos wchVideos);

    @Query("DELETE FROM wchvideos")
    void deleteAll();

    @Query("select count(id) from wchvideos")
    int totalUnwatched();
}
