package com.z.newsleak.data.db;

import android.content.Context;

import com.z.newsleak.model.NewsItem;
import com.z.newsleak.utils.NewsTypeConverters;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {NewsItem.class}, version = 1, exportSchema = false)
@TypeConverters(NewsTypeConverters.class)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "newsleak.db";
    private static AppDatabase database;

    @NonNull
    public static AppDatabase getInstance(Context context) {
        if (database == null) {
            synchronized (AppDatabase.class) {
                if (database == null) {
                    database = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DATABASE_NAME)
                            .build();
                }
            }
        }
        return database;
    }

    public abstract NewsDao getNewsDao();
}