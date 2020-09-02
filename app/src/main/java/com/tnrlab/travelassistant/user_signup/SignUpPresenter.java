package com.tnrlab.travelassistant.user_signup;

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
import com.tnrlab.travelassistant.models.user.User;

public class SignUpPresenter {
    SignUpView signUpView;
    Activity activity;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public SignUpPresenter(SignUpView signUpView, Activity activity) {
        this.signUpView = signUpView;
        this.activity = activity;
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void signUpNow(final User user) {
        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            saveUserIntoDB(firebaseUser, user);

                        } else {
                            // If sign in fails, display a message to the user.
                            // Log.w("USER_SIGN_UP", "createUserWithEmail:failure", task.getException());
                            signUpView.onSignUpFailed("Authentication failed.");
                        }

                        // ...
                    }
                });


    }


    public void saveUserIntoDB(FirebaseUser firebaseUser, User user) {
        mDatabase.child("users").child(firebaseUser.getUid()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                signUpView.onSignUpSuccessful(firebaseUser, user);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                signUpView.onSignUpFailed("Authentication failed.");


            }
        });

    }
}
