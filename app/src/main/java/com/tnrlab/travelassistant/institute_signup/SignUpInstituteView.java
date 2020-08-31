package com.tnrlab.travelassistant.institute_signup;

import com.google.firebase.auth.FirebaseUser;
import com.tnrlab.travelassistant.models.institute.Institution;

public interface SignUpInstituteView {
    void onSignUpSuccessful(FirebaseUser firebaseUser, Institution institution);

    void onSignUpFailed(String s);

    void onSaveUserDBSuccessful();

    void onSaveUserDBFailed();
}
