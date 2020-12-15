package com.tnrlab.travelassistant.institution.login;

import com.tnrlab.travelassistant.models.institute.Institution;
import com.tnrlab.travelassistant.models.user.User;

public interface LoginView {

    void signInInstituteSuccessful(Institution institution);

    void signInUserSuccessful(User user);

    void onLoginFailed(String s);
}
