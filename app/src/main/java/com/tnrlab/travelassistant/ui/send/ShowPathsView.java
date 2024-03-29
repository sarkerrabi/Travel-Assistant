package com.tnrlab.travelassistant.ui.send;

import com.tnrlab.travelassistant.models.creaet_path.RouteDetails;

public interface ShowPathsView {
    void onFailed(String message);

    void onShowCreatedPathClicked(RouteDetails routeDetails);

    void onPathDeleteClicked(RouteDetails routeDetails, ShowPathsView showPathsView);

    void onPathDeletedSuccessfully();

    void onShareChanged(RouteDetails details, boolean isChecked);

    void onPathSharedSuccessfully(boolean isShared);
}
