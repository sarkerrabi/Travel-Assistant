package com.tnrlab.travelassistant.models.create_map;

import com.google.firebase.database.Exclude;
import com.mapbox.geojson.FeatureCollection;


public class MapDataModel {
    private String name;
    private String mapData;

    @Exclude
    private FeatureCollection featureCollection;

    public MapDataModel() {
    }


    public MapDataModel(FeatureCollection featureCollection, String name) {
        this.mapData = featureCollection.toJson();
        this.name = name;

    }

    public String getMapData() {
        return mapData;
    }

    public void setMapData(String mapData) {
        this.mapData = mapData;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
