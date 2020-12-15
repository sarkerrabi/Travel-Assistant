package com.tnrlab.travelassistant.ui.profile;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.tnrlab.travelassistant.models.user.User;
import com.tnrlab.travelassistant.shared_db.SharedDB;

public class UserProfileViewModel extends ViewModel {
    private MutableLiveData<User> userMutableLiveData;


    public UserProfileViewModel() {
        userMutableLiveData = new MutableLiveData<>();


    }

    public LiveData<User> getUserProfileData(Context context) {
        Gson gson = new Gson();
        User user = gson.fromJson(new SharedDB(context).getUserInfo(), User.class);

        userMutableLiveData.setValue(user);

        return userMutableLiveData;
    }
}