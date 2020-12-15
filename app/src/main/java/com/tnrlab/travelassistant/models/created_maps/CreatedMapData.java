package com.tnrlab.travelassistant.models.created_maps;

import com.tnrlab.travelassistant.models.create_map.MapDataModel;
import com.tnrlab.travelassistant.models.institute.Institution;

import java.util.List;

public class CreatedMapData {
    Institution institution;
    List<MapDataModel> mapDataModels;


    public CreatedMapData() {
    }

    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    public List<MapDataModel> getMapDataModels() {
        return mapDataModels;
    }

    public void setMapDataModels(List<MapDataModel> mapDataModels) {
        this.mapDataModels = mapDataModels;
    }
}
