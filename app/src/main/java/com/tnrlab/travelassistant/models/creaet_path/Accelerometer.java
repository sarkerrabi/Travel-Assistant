package com.tnrlab.travelassistant.models.creaet_path;

import androidx.room.ColumnInfo;
import androidx.room.Ignore;

public class Accelerometer {

    @ColumnInfo(name = "Accelerometer_X")
    float x;
    @ColumnInfo(name = "Accelerometer_Y")
    float y;
    @ColumnInfo(name = "Accelerometer_Z")
    float z;

    @Ignore
    public Accelerometer() {
    }

    public Accelerometer(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }
}
