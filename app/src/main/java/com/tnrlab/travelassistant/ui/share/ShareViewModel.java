package com.tnrlab.travelassistant.ui.share;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tnrlab.travelassistant.ui.send.ShowPathsView;

public class ShareViewModel extends ViewModel {

    MutableLiveData<DataSnapshot> snapshotMutableLiveData;
    private DatabaseReference mDatabase;

    public ShareViewModel() {
        snapshotMutableLiveData = new MutableLiveData<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
    }

    public LiveData<DataSnapshot> getAllSharedRoutePaths(ShowPathsView showPathsView) {
        mDatabase.child("route_paths").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshotMutableLiveData.setValue(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showPathsView.onFailed(error.getMessage());

            }
        });

        return snapshotMutableLiveData;
    }


}