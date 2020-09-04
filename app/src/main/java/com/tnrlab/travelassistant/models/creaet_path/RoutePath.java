package com.tnrlab.travelassistant.models.creaet_path;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Route_Path")
public class RoutePath {
    @PrimaryKey(autoGenerate = true)
    int id;
    long routePathID;
    double latitude;
    double longitude;
    double altitude;
    float speed;
    String timestmp;
    String timeDetails;
    String latLan;

    @Embedded
    SensorData sensorData;

    //user
    String uid;

    String startAddress;
    String endAddress;
    String descriptions;

    @Ignore
    public RoutePath() {
    }

    public RoutePath(int id, long routePathID, double latitude, double longitude, double altitude, float speed, String timestmp, String timeDetails, String latLan, SensorData sensorData, String uid, String startAddress, String endAddress, String descriptions) {
        this.id = id;
        this.routePathID = routePathID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.speed = speed;
        this.timestmp = timestmp;
        this.timeDetails = timeDetails;
        this.latLan = latLan;
        this.sensorData = sensorData;
        this.uid = uid;
        this.startAddress = startAddress;
        this.endAddress = endAddress;
        this.descriptions = descriptions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public String getTimestmp() {
        return timestmp;
    }

    public void setTimestmp(String timestmp) {
        this.timestmp = timestmp;
    }

    public String getTimeDetails() {
        return timeDetails;
    }

    public void setTimeDetails(String timeDetails) {
        this.timeDetails = timeDetails;
    }

    public String getLatLan() {
        return latLan;
    }

    public void setLatLan(String latLan) {
        this.latLan = latLan;
    }

    public SensorData getSensorData() {
        return sensorData;
    }

    public void setSensorData(SensorData sensorData) {
        this.sensorData = sensorData;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public long getRoutePathID() {
        return routePathID;
    }

    public void setRoutePathID(long routePathID) {
        this.routePathID = routePathID;
    }
}
