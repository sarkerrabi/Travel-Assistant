package com.tnrlab.travelassistant.ui.create_path;

import android.content.Context;
import android.hardware.Sensor;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.pwittchen.reactivesensors.library.ReactiveSensorEvent;
import com.github.pwittchen.reactivesensors.library.ReactiveSensors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class CreatePathViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    MutableLiveData<Float> sensorEventLiveData;

    public CreatePathViewModel() {
        sensorEventLiveData = new MutableLiveData<>();


    }

    public LiveData<Float> getSensorEventLiveData(Context context) {
        new ReactiveSensors(context).observeSensor(Sensor.TYPE_GYROSCOPE)
                .subscribeOn(Schedulers.computation())
                .filter(ReactiveSensorEvent::sensorChanged)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ReactiveSensorEvent>() {
                               @Override
                               public void accept(ReactiveSensorEvent event) throws Throwable {

                                   float x = event.sensorValues()[0];
                                   float y = event.sensorValues()[1];
                                   float z = event.sensorValues()[2];
                                   sensorEventLiveData.setValue(x);


                                   String message = String.format("x = %f, y = %f, z = %f", x, y, z);

                                   Log.d("gyroscope readings", message);
                               }
                           }

                );


        return sensorEventLiveData;
    }
}