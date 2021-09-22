package com.tnrlab.travelassistant.applications;

import androidx.annotation.NonNull;

import com.google.firebase.database.FirebaseDatabase;
import com.mapbox.mapboxsdk.offline.OfflineManager;

public class MyFirebaseApp extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        OfflineManager fileSource = OfflineManager.getInstance(getApplicationContext());

        fileSource.invalidateAmbientCache(new OfflineManager.FileSourceCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(@NonNull String message) {

            }
        });
    }
}
