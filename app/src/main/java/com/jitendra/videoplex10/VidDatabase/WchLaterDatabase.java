package com.jitendra.videoplex10.VidDatabase;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {WchVideos.class}, version = 1)
public abstract class WchLaterDatabase extends RoomDatabase {

    public abstract WchVideosDao wchVideosDao();

    private static WchLaterDatabase INSTANCE;

    public static synchronized WchLaterDatabase getDbInstance(Context context) {

        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    WchLaterDatabase.class, "DB_NAME")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(INSTANCE).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        PopulateDbAsyncTask(WchLaterDatabase instance) {
            WchVideosDao dao = instance.wchVideosDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
