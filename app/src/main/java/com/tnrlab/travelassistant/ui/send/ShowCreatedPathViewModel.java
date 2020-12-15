package com.tnrlab.travelassistant.ui.send;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tnrlab.travelassistant.models.creaet_path.RouteDetails;

public class ShowCreatedPathViewModel extends ViewModel {

    MutableLiveData<DataSnapshot> snapshotMutableLiveData;
    private DatabaseReference mDatabase;


    public ShowCreatedPathViewModel() {
        snapshotMutableLiveData = new MutableLiveData<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
    }

    public LiveData<DataSnapshot> getAllMyRoutePaths(ShowPathsView showPathsView) {
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

    public void removePathFromDB(RouteDetails routeDetails, ShowPathsView showPathsView) {
        mDatabase.child("route_paths").child(routeDetails.getFireDBRouteKey()).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                showPathsView.onPathDeletedSuccessfully();
            }
        });

    }

    public void shareWithOthers(RouteDetails details, boolean isChecked, ShowPathsView showPathsView) {
        mDatabase.child("route_paths").child(details.getFireDBRouteKey()).child("isShared").setValue(isChecked).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                showPathsView.onPathSharedSuccessfully(isChecked);
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showPathsView.onFailed(e.getMessage());
            }
        });
    }
}