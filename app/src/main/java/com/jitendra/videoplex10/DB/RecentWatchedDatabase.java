package com.jitendra.videoplex10.DB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {UserEntity.class}, version = 1)
public abstract class RecentWatchedDatabase extends RoomDatabase {
    public abstract UserDao userDao();

    private static RecentWatchedDatabase INSTANCE;
    
    public static RecentWatchedDatabase getDbInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    RecentWatchedDatabase.class, "DB NAME")
            .allowMainThreadQueries()
            .build();
        }
        return INSTANCE;
    }
}
