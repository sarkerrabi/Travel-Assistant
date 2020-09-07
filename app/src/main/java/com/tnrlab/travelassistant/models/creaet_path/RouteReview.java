package com.tnrlab.travelassistant.models.creaet_path;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Route_Review")
public class RouteReview {
    @PrimaryKey(autoGenerate = true)
    int id;
    long routePathID;

    String startPlaceInfo;
    String endPlaceInfo;
    String description;

    String uid;

    @Ignore
    public RouteReview() {
    }

    public RouteReview(int id, long routePathID, String startPlaceInfo, String endPlaceInfo, String description, String uid) {
        this.id = id;
        this.routePathID = routePathID;
        this.startPlaceInfo = startPlaceInfo;
        this.endPlaceInfo = endPlaceInfo;
        this.description = description;
        this.uid = uid;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getRoutePathID() {
        return routePathID;
    }

    public void setRoutePathID(long routePathID) {
        this.routePathID = routePathID;
    }

    public String getStartPlaceInfo() {
        return startPlaceInfo;
    }

    public void setStartPlaceInfo(String startPlaceInfo) {
        this.startPlaceInfo = startPlaceInfo;
    }

    public String getEndPlaceInfo() {
        return endPlaceInfo;
    }

    public void setEndPlaceInfo(String endPlaceInfo) {
        this.endPlaceInfo = endPlaceInfo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
