package com.tnrlab.travelassistant.models.creaet_path;

import androidx.room.ColumnInfo;
import androidx.room.Ignore;

public class LinearAcceleration {
    @ColumnInfo(name = "LinearAcceleration_X")
    float x;
    @ColumnInfo(name = "LinearAcceleration_Y")
    float y;
    @ColumnInfo(name = "LinearAcceleration_Z")
    float z;

    @Ignore
    public LinearAcceleration() {
    }

    public LinearAcceleration(float x, float y, float z) {
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
