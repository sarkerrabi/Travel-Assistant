package com.tnrlab.travelassistant.models.creaet_path;

import androidx.room.Ignore;

public class DeviceOrientation {
    float azimuthValue;
    float roll;
    float pitch;


    public DeviceOrientation() {
    }

    @Ignore
    public DeviceOrientation(float azimuthValue, float roll, float pitch) {
        this.azimuthValue = azimuthValue;
        this.roll = roll;
        this.pitch = pitch;
    }


    public float getAzimuthValue() {
        return azimuthValue;
    }

    public void setAzimuthValue(float azimuthValue) {
        this.azimuthValue = azimuthValue;
    }

    public float getRoll() {
        return roll;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }
}
