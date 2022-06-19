package com.jitendra.videoplex10.VidDatabase;


import android.content.Context;

import androidx.room.ColumnInfo;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Videos.class}, version =  1)
public abstract class WatchLaterDb extends RoomDatabase {

    public abstract VideosDao videosDao();

    private static WatchLaterDb INSTANCE;

    public static WatchLaterDb getDbInstance(Context context) {
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    WatchLaterDb.class, "DB_NAME")
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
}
