package com.jitendra.videoplex10.VidDatabase.WatchHistoryDb;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {HistoryVids.class}, version = 1)
public abstract class HistoryVidsDatabase extends RoomDatabase {

    public abstract HistoryVidsDao historyVidsDao();

    private static HistoryVidsDatabase hInstance;

    public static synchronized HistoryVidsDatabase getHistoryDbInstance(Context context) {

        if(hInstance == null) {
            hInstance = Room.databaseBuilder(context.getApplicationContext(),
                    HistoryVidsDatabase.class, "HISTORY_DB")
                    .fallbackToDestructiveMigration()
                    .addCallback(historyRoomCallback)
                    .build();
        }
        return hInstance;
    }

    private static RoomDatabase.Callback historyRoomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(hInstance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        PopulateDbAsyncTask(HistoryVidsDatabase instance) {
            HistoryVidsDao hisDao = instance.historyVidsDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}

