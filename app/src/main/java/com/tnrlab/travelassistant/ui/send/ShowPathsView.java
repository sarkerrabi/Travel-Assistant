package com.tnrlab.travelassistant.ui.send;

import com.tnrlab.travelassistant.models.creaet_path.RouteDetails;

public interface ShowPathsView {
    void onFailed(String message);

    void onShowCreatedPathClicked(RouteDetails routeDetails);
}
