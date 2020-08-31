package com.tnrlab.travelassistant.common;

import android.text.TextUtils;
import android.util.Patterns;

public class Common {
    public static String sharedPrefName = "SensorData";


    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
