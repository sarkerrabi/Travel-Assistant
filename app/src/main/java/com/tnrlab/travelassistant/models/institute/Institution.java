package com.tnrlab.travelassistant.models.institute;


import java.util.List;

public class Institution {
    private String firebaseUid;
    private boolean active;
    private String institutionName;
    private String mapPickerInsAddress;

    private String userGivenInsAddress;
    private String emailAddress;

    private String userPassword;
    private List<Double> latLan;

    public Institution() {
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<Double> getLatLan() {
        return latLan;
    }

    public void setLatLan(List<Double> latLan) {
        this.latLan = latLan;
    }

    public String getFirebaseUid() {
        return firebaseUid;
    }

    public void setFirebaseUid(String firebaseUid) {
        this.firebaseUid = firebaseUid;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public String getMapPickerInsAddress() {
        return mapPickerInsAddress;
    }

    public void setMapPickerInsAddress(String mapPickerInsAddress) {
        this.mapPickerInsAddress = mapPickerInsAddress;
    }

    public String getUserGivenInsAddress() {
        return userGivenInsAddress;
    }

    public void setUserGivenInsAddress(String userGivenInsAddress) {
        this.userGivenInsAddress = userGivenInsAddress;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
