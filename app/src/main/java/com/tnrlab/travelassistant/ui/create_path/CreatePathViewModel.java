package com.tnrlab.travelassistant.ui.create_path;

import android.content.Context;
import android.hardware.Sensor;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.pwittchen.reactivesensors.library.ReactiveSensorEvent;
import com.github.pwittchen.reactivesensors.library.ReactiveSensors;
import com.tnrlab.travelassistant.models.creaet_path.Accelerometer;
import com.tnrlab.travelassistant.models.creaet_path.DeviceOrientation;
import com.tnrlab.travelassistant.models.creaet_path.GravityData;
import com.tnrlab.travelassistant.models.creaet_path.LinearAcceleration;
import com.tnrlab.travelassistant.models.creaet_path.SensorData;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

import static android.content.ContentValues.TAG;


public class CreatePathViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    MutableLiveData<Accelerometer> sensorEventLiveData;
    MutableLiveData<GravityData> gravityDataMutableLiveData;
    MutableLiveData<SensorData> sensorDataMutableLiveData;
    private ReactiveSensors reactiveSensors;
    private ReactiveSensors reactiveSensors2;

    public CreatePathViewModel() {
        sensorEventLiveData = new MutableLiveData<>();
        gravityDataMutableLiveData = new MutableLiveData<>();
        sensorDataMutableLiveData = new MutableLiveData<>();


    }

    public LiveData<Accelerometer> getSensorEventLiveData(Context context) {
        Accelerometer accelerometer = new Accelerometer();
        reactiveSensors = new ReactiveSensors(context);
        reactiveSensors.observeSensor(Sensor.TYPE_GRAVITY)
                .subscribeOn(Schedulers.computation())
                .filter(ReactiveSensorEvent::sensorChanged)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ReactiveSensorEvent>() {
                               @Override
                               public void accept(ReactiveSensorEvent event) throws Throwable {
                                   Log.e(TAG, "accept: " + event.sensorValues().toString());

                               }
                           }

                );

        reactiveSensors.observeSensor(Sensor.TYPE_GRAVITY)
                .subscribeOn(Schedulers.computation())
                .filter(ReactiveSensorEvent::sensorChanged)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ReactiveSensorEvent>() {
                               @Override
                               public void accept(ReactiveSensorEvent event) throws Throwable {

                                   float x = event.sensorValues()[0];
                                   float y = event.sensorValues()[1];
                                   float z = event.sensorValues()[2];
                                   //accelerometer.setZ(z);

                                   // sensorEventLiveData.setValue(accelerometer);

                               }
                           }

                );


        return sensorEventLiveData;
    }

    public LiveData<Accelerometer> getGravityLiveData(Context context) {

        return sensorEventLiveData;
    }


    public LiveData<SensorData> getSensorData(Context context) {
        reactiveSensors = new ReactiveSensors(context);

        SensorData sensorData = new SensorData();

        sensorData.setHorizontalAngle(70);

        Accelerometer accelerometer = new Accelerometer();
        if (reactiveSensors.hasSensor(Sensor.TYPE_ACCELEROMETER)) {
            reactiveSensors.observeSensor(Sensor.TYPE_ACCELEROMETER)
                    .subscribeOn(Schedulers.computation())
                    .filter(ReactiveSensorEvent::sensorChanged)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ReactiveSensorEvent>() {
                                   @Override
                                   public void accept(ReactiveSensorEvent event) throws Throwable {

                                       float x = event.sensorValues()[0];
                                       float y = event.sensorValues()[1];
                                       float z = event.sensorValues()[2];

                                       accelerometer.setX(x);
                                       accelerometer.setY(y);
                                       accelerometer.setZ(z);

                                       sensorData.setAccelerometer(accelerometer);

                                       sensorDataMutableLiveData.setValue(sensorData);


                                   }
                               }

                    );
        }

        GravityData gravityData = new GravityData();
        if (reactiveSensors.hasSensor(Sensor.TYPE_GRAVITY)) {
            reactiveSensors.observeSensor(Sensor.TYPE_GRAVITY)
                    .subscribeOn(Schedulers.computation())
                    .filter(ReactiveSensorEvent::sensorChanged)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ReactiveSensorEvent>() {
                                   @Override
                                   public void accept(ReactiveSensorEvent event) throws Throwable {

                                       float x = event.sensorValues()[0];
                                       float y = event.sensorValues()[1];
                                       float z = event.sensorValues()[2];

                                       gravityData.setX(x);
                                       gravityData.setY(y);
                                       gravityData.setZ(z);
                                       sensorData.setGravity(gravityData);

                                       sensorDataMutableLiveData.setValue(sensorData);


                                   }
                               }

                    );
        }

        LinearAcceleration linearAcceleration = new LinearAcceleration();
        if (reactiveSensors.hasSensor(Sensor.TYPE_LINEAR_ACCELERATION)) {
            reactiveSensors.observeSensor(Sensor.TYPE_LINEAR_ACCELERATION)
                    .subscribeOn(Schedulers.computation())
                    .filter(ReactiveSensorEvent::sensorChanged)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ReactiveSensorEvent>() {
                                   @Override
                                   public void accept(ReactiveSensorEvent event) throws Throwable {

                                       float x = event.sensorValues()[0];
                                       float y = event.sensorValues()[1];
                                       float z = event.sensorValues()[2];

                                       linearAcceleration.setX(x);
                                       linearAcceleration.setY(y);
                                       linearAcceleration.setZ(z);
                                       sensorData.setLinearAcceleration(linearAcceleration);


                                       sensorDataMutableLiveData.setValue(sensorData);


                                   }
                               }

                    );
        }

        DeviceOrientation deviceOrientation = new DeviceOrientation();
        if (reactiveSensors.hasSensor(Sensor.TYPE_ORIENTATION)) {
            reactiveSensors.observeSensor(Sensor.TYPE_ORIENTATION)
                    .subscribeOn(Schedulers.computation())
                    .filter(ReactiveSensorEvent::sensorChanged)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ReactiveSensorEvent>() {
                                   @Override
                                   public void accept(ReactiveSensorEvent event) throws Throwable {

                                       float x = event.sensorValues()[0];
                                       float y = event.sensorValues()[1];
                                       float z = event.sensorValues()[2];

                                       deviceOrientation.setAzimuthValue(x);
                                       deviceOrientation.setPitch(y);
                                       deviceOrientation.setRoll(z);
                                       sensorData.setDeviceOrientation(deviceOrientation);


                                       sensorDataMutableLiveData.setValue(sensorData);


                                   }
                               }

                    );
        }

        return sensorDataMutableLiveData;
    }
}