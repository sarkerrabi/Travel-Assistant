package com.tnrlab.travelassistant.ui.show_map_user;

import com.tnrlab.travelassistant.models.create_map.MapDataModel;

import java.util.List;

public interface ShowMapUserView {

    void onMapDataListReady(List<MapDataModel> mapDataModels);
}
