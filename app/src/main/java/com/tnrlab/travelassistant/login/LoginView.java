package com.tnrlab.travelassistant.login;

import com.tnrlab.travelassistant.models.institute.Institution;

public interface LoginView {

    void signInInstituteSuccessful(Institution institution);

    void onLoginFailed(String s);
}
