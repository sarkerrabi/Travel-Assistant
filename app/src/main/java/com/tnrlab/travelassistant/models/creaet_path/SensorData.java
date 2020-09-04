package com.tnrlab.travelassistant.models.creaet_path;

import androidx.room.Embedded;
import androidx.room.Ignore;

public class SensorData {
    @Embedded
    Accelerometer accelerometer;

    @Embedded
    GravityData gravity;

    float horizontalAngle;

    @Embedded
    LinearAcceleration linearAcceleration;

    @Embedded
    DeviceOrientation deviceOrientation;


    public SensorData() {
    }


    @Ignore
    public SensorData(Accelerometer accelerometer, GravityData gravity, float horizontalAngle, LinearAcceleration linearAcceleration, DeviceOrientation deviceOrientation) {
        this.accelerometer = accelerometer;
        this.gravity = gravity;
        this.horizontalAngle = horizontalAngle;
        this.linearAcceleration = linearAcceleration;
        this.deviceOrientation = deviceOrientation;
    }

    public DeviceOrientation getDeviceOrientation() {
        return deviceOrientation;
    }

    public void setDeviceOrientation(DeviceOrientation deviceOrientation) {
        this.deviceOrientation = deviceOrientation;
    }

    public Accelerometer getAccelerometer() {
        return accelerometer;
    }

    public void setAccelerometer(Accelerometer accelerometer) {
        this.accelerometer = accelerometer;
    }


    public GravityData getGravity() {
        return gravity;
    }

    public void setGravity(GravityData gravity) {
        this.gravity = gravity;
    }

    public float getHorizontalAngle() {
        return horizontalAngle;
    }

    public void setHorizontalAngle(float horizontalAngle) {
        this.horizontalAngle = horizontalAngle;
    }

    public LinearAcceleration getLinearAcceleration() {
        return linearAcceleration;
    }

    public void setLinearAcceleration(LinearAcceleration linearAcceleration) {
        this.linearAcceleration = linearAcceleration;
    }


}
