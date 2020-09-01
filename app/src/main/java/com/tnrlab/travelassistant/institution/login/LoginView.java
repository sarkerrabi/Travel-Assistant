package com.tnrlab.travelassistant.institution.login;

import com.tnrlab.travelassistant.models.institute.Institution;

public interface LoginView {

    void signInInstituteSuccessful(Institution institution);

    void onLoginFailed(String s);
}
