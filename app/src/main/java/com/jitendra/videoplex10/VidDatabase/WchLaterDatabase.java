package com.jitendra.videoplex10.VidDatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {WchVideos.class}, version = 1)
public abstract class WchLaterDatabase extends RoomDatabase {

    public abstract WchVideosDao wchVideosDao();

    private static WchLaterDatabase INSTANCE;

    public static synchronized WchLaterDatabase getDbInstance(Context context) {

        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    WchLaterDatabase.class, "DB_NAME")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}
