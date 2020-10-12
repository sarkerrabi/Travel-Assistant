package com.tnrlab.travelassistant.ui.created_maps;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tnrlab.travelassistant.models.created_maps.CreatedMapData;
import com.tnrlab.travelassistant.models.institute.Institution;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class CreatedMapsViewModel extends ViewModel {
    private DatabaseReference mDatabase;
    private List<Institution> institutionList;
    private List<CreatedMapData> createdMapDataList;


    public CreatedMapsViewModel() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
        institutionList = new ArrayList<>();
        createdMapDataList = new ArrayList<>();
    }


    public void getAllCreatedMaps(CreatedMapsView createdMapsView) {
        mDatabase.child("institutions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                institutionList.clear();
                for (DataSnapshot mySnapshot : snapshot.getChildren()) {
                    Institution institution = mySnapshot.getValue(Institution.class);
                    assert institution != null;
                    if (institution.isActive()) {
                        institutionList.add(institution);
                    }
                }

                createdMapsView.onInstituteListReady(institutionList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Timber.e("onCancelled: %s", error.getMessage());
            }
        });

    }

    private void findMaps(CreatedMapsView createdMapsView) {
        for (int i = 0; i < institutionList.size(); i++) {
            int finalI = i;

        }


    }


}