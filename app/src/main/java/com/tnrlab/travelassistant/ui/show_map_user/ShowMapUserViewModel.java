package com.tnrlab.travelassistant.ui.show_map_user;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tnrlab.travelassistant.models.create_map.MapDataModel;
import com.tnrlab.travelassistant.models.institute.Institution;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class ShowMapUserViewModel extends ViewModel {
    private DatabaseReference mDatabase;
    private List<MapDataModel> mapDataModels;

    public ShowMapUserViewModel() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
        mapDataModels = new ArrayList<>();
    }

    public void getMapDetailsList(Institution institution, ShowMapUserView showMapUserView) {
        mDatabase.child("instituteMaps").child(institution.getFirebaseUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<MapDataModel> mapDataModels = new ArrayList<>();
                        for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                            MapDataModel mapDataModel = dataSnapshot1.getValue(MapDataModel.class);
                            mapDataModels.add(mapDataModel);
                        }
                        showMapUserView.onMapDataListReady(mapDataModels);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Timber.e("onCancelled: %s", error.getMessage());

                    }
                });
    }
}