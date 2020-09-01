package com.tnrlab.travelassistant.institution.show_map;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tnrlab.travelassistant.models.create_map.MapDataModel;

import java.util.ArrayList;
import java.util.List;


public class ShowMapPresenter {
    ShowMapView showMapView;
    Activity activity;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    public ShowMapPresenter(ShowMapView showMapView, Activity activity) {
        this.showMapView = showMapView;
        this.activity = activity;
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }


    public void getInstMapDataList() {
        List<MapDataModel> mapDataModels = new ArrayList<>();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            showMapView.onFailedToGetCurrentUser();

        } else {


            mDatabase.child("myMaps").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null) {
                        for (DataSnapshot mydataSnapshot : dataSnapshot.getChildren()) {
//                            Log.d("TAG_SHOW_MAP", "onDataChange: " + dataSnapshot.getKey());
                            if (mydataSnapshot.getKey().equals(mAuth.getUid())) {

                                MapDataModel mapDataModel = mydataSnapshot.getValue(MapDataModel.class);
                                mapDataModels.add(mapDataModel);
                            }

                        }
                        showMapView.onMapDataListReady(mapDataModels);


                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    showMapView.onMapDataListFailed(databaseError.getMessage());

                }
            });
        }

    }

}
