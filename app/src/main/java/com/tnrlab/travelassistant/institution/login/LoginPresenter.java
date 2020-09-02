package com.tnrlab.travelassistant.institution.login;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tnrlab.travelassistant.models.institute.Institution;
import com.tnrlab.travelassistant.models.user.User;

public class LoginPresenter {
    Activity activity;
    LoginView loginView;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public LoginPresenter(Activity activity, LoginView loginView) {
        this.loginView = loginView;
        this.activity = activity;
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void loginNow(String email, String password, int loginType) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            checkIsUserFound(user, loginType);


                        } else {
                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(activity, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            loginView.onLoginFailed("Authentication failed.");
                        }
                    }
                });
    }

    private void checkIsUserFound(FirebaseUser user, int loginType) {
        if (loginType == 1) {
            insCheck(user.getUid());
        } else if (loginType == 2) {
            userCheck(user.getUid());
        }


    }


    public void userCheck(String uid) {
        mDatabase.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    loginView.signInUserSuccessful(user);
                } else {
                    loginView.onLoginFailed("User is not registered. Sign up please.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                loginView.onLoginFailed("Please check your internet connection and try again.");
            }
        });
    }

    public void insCheck(String uid) {
        mDatabase.child("institutions").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Institution institution = dataSnapshot.getValue(Institution.class);
                    loginView.signInInstituteSuccessful(institution);
                } else {
                    loginView.onLoginFailed("Institution is not registered. Please sign up first.");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                loginView.onLoginFailed("Please check your internet connection and try again.");
            }
        });
    }
}
