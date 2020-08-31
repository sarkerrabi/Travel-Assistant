package com.tnrlab.travelassistant.show_map;


import com.tnrlab.travelassistant.models.create_map.MapDataModel;

import java.util.List;

public interface ShowMapView {
    void onFailedToGetCurrentUser();

    void onMapDataListReady(List<MapDataModel> mapDataModels);

    void onMapDataListFailed(String message);
}
