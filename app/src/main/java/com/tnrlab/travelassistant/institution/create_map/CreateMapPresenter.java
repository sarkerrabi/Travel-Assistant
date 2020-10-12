package com.tnrlab.travelassistant.institution.create_map;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mapbox.geojson.FeatureCollection;
import com.tnrlab.travelassistant.models.create_map.MapDataModel;

public class CreateMapPresenter {

    private static final String TAG = "MAP_CREATOR";
    CreateMapView createMapView;
    Activity activity;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;


    public CreateMapPresenter(CreateMapView createMapView, Activity activity) {
        this.createMapView = createMapView;
        this.activity = activity;
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }


    public void saveMapDataIntoDB(FeatureCollection featureCollection) {

        MapDataModel mapDataModel = new MapDataModel(featureCollection, "test");
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            createMapView.onFailedToGetCurrentUser();

        } else {


            mDatabase.child("instituteMaps").child(currentUser.getUid()).push().setValue(mapDataModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    createMapView.onCreateMapSaveSuccessful("Map saved successfully");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    createMapView.onCreateMapSaveFailed("Failed to save map data, check your internet connection and try again");
                    Log.e(TAG, "onFailure: " + e.getMessage());

                }
            });
        }

    }


}
