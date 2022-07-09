package com.jitendra.videoplex10.VidDatabase.WatchHistoryDb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.jitendra.videoplex10.VidDatabase.WatchLaterDb.WchVideos;

import java.util.List;

@Dao
public interface HistoryVidsDao {

    @Query("SELECT * FROM historyvids ORDER BY seq DESC")
    List<HistoryVids> getFullHistory();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertToHistory(HistoryVids historyVids);

    @Delete
    void deleteFromHistory(HistoryVids historyVids);

    @Query("DELETE FROM historyvids")
    void deleteFullHistory();
}
