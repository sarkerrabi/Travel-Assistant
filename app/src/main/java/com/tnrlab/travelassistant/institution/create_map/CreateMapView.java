package com.tnrlab.travelassistant.institution.create_map;

public interface CreateMapView {


    void onFailedToGetCurrentUser();

    void onCreateMapSaveSuccessful(String successMessage);

    void onCreateMapSaveFailed(String failMessage);

    void onCreateBlockNameSet(String name);
}
