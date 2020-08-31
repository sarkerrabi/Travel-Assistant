package com.tnrlab.travelassistant.create_map;

public interface CreateMapView {


    void onFailedToGetCurrentUser();

    void onCreateMapSaveSuccessful(String successMessage);

    void onCreateMapSaveFailed(String failMessage);
}
