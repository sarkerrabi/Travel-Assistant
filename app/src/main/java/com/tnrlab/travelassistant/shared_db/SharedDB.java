package com.tnrlab.travelassistant.shared_db;

import android.content.Context;
import android.content.SharedPreferences;

import com.tnrlab.travelassistant.common.Common;

import static android.content.Context.MODE_PRIVATE;

public class SharedDB {
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    private Context context;

    public SharedDB(Context context) {
        this.context = context;
        editor = context.getSharedPreferences(Common.sharedPrefName, MODE_PRIVATE).edit();
        sharedPreferences = context.getSharedPreferences(Common.sharedPrefName, MODE_PRIVATE);
    }

    public void saveUserTypeID(int userType) {
        editor.putInt("USER_TYPE", userType);
        editor.apply();
    }

    public void saveUserInfo(String userInfo) {
        editor.putString("USER_INFO", userInfo);
        editor.apply();
    }

    public String getUserInfo() {
        return sharedPreferences.getString("USER_INFO", null);
    }


    public void clearUserType() {
        editor.clear().apply();
    }

    public int getUserType() {
        return sharedPreferences.getInt("USER_TYPE", 0);
    }

    private void clearUser() {
        editor.clear().apply();
    }
}
