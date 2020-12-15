package com.tnrlab.travelassistant.user_signup;

import com.google.firebase.auth.FirebaseUser;
import com.tnrlab.travelassistant.models.user.User;

public interface SignUpView {
    void onSignUpSuccessful(FirebaseUser firebaseUser, User user);

    void onSignUpFailed(String message);
}
