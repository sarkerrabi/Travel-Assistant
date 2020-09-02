package com.tnrlab.travelassistant.institution.institute_signup;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tnrlab.travelassistant.models.institute.Institution;

public class SignUpInstitutePresenter {
    SignUpInstituteView signUpForInstitutionView;
    Activity activity;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public SignUpInstitutePresenter(SignUpInstituteView signUpForInstitutionView, Activity activity) {
        this.signUpForInstitutionView = signUpForInstitutionView;
        this.activity = activity;
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void signUpForInstitutionNow(final Institution institution) {
        mAuth.createUserWithEmailAndPassword(institution.getEmailAddress(), institution.getUserPassword())
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //   Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            signUpForInstitutionView.onSignUpSuccessful(firebaseUser, institution);
                        } else {
                            // If sign in fails, display a message to the user.
                            //  Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            /*Toast.makeText(activity, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();*/
                            signUpForInstitutionView.onSignUpFailed("Authentication failed.");
                        }

                        // ...
                    }
                });


    }


    public void saveForInstitutionIntoDB(Institution institution) {
        mDatabase.child("institutions").child(institution.getFirebaseUid()).setValue(institution).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                signUpForInstitutionView.onSaveUserDBSuccessful();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                signUpForInstitutionView.onSaveUserDBFailed();

            }
        });

    }


}
