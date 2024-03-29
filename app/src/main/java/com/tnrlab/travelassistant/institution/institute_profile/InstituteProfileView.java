package com.tnrlab.travelassistant.institution.institute_profile;

import com.tnrlab.travelassistant.models.institute.Institution;

public interface InstituteProfileView {

    void onInstituteProfileReady(Institution institution);

    void onInstituteProfileFailed(String message);


}
