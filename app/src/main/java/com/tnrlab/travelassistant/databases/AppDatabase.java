package com.tnrlab.travelassistant.databases;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.tnrlab.travelassistant.models.creaet_path.RoutePath;
import com.tnrlab.travelassistant.models.creaet_path.RouteReview;

@Database(entities = {RoutePath.class, RouteReview.class}, version = 6, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "Travel_Assistant.db";
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                //Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .allowMainThreadQueries()
                        .build();
            }
        }
        // Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract RoutePathDao routePathDao();

    public abstract RouteReviewDao routeReviewDao();


}
