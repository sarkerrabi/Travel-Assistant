package com.tnrlab.travelassistant.institution.institute_profile;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tnrlab.travelassistant.models.institute.Institution;

public class InstituteProfilePresenter {
    InstituteProfileView instituteProfileView;
    Activity activity;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public InstituteProfilePresenter(InstituteProfileView instituteProfileView, Activity activity) {
        this.instituteProfileView = instituteProfileView;
        this.activity = activity;
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("institutions");
    }

    public void getMyInstituteProfile() {
        if (mAuth.getUid() != null) {
            mDatabase.child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Institution institution = snapshot.getValue(Institution.class);
                    if (institution != null) {
//                        Toast.makeText(activity, "" + institution.getInstitutionName(), Toast.LENGTH_SHORT).show();
                        instituteProfileView.onInstituteProfileReady(institution);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    instituteProfileView.onInstituteProfileFailed(error.getMessage());
                }
            });

        }


    }


}
