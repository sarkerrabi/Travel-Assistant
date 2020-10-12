package com.tnrlab.travelassistant.ui.created_maps;

import com.tnrlab.travelassistant.models.created_maps.CreatedMapData;
import com.tnrlab.travelassistant.models.institute.Institution;

import java.util.List;

public interface CreatedMapsView {

    void onMapListReady(List<CreatedMapData> createdMapDataList);

    void showCreatedMapClicked(Institution institution);

    void onInstituteListReady(List<Institution> institutionList);
}
