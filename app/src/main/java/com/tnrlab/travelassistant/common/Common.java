package com.tnrlab.travelassistant.common;

import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Patterns;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Common {
    public static String sharedPrefName = "SensorData";

    public static String getCurrentTimeAndDate() {
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateTime = dateFormat.format(new Date()); // Find todays date

            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public static long getUniqueRoutePathID() {
        return System.currentTimeMillis();
    }


    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }


    public static boolean hasPermission(Context context, String permission) {

        int res = context.checkCallingOrSelfPermission(permission);

        return res == PackageManager.PERMISSION_GRANTED;

    }

    /** Determines if the context calling has the required permissions
     * @param context - the IPC context
     * @param permissions - The permissions to check
     * @return true if the IPC has the granted permission
     */
    public static boolean hasPermissions(Context context, String... permissions) {

        boolean hasAllPermissions = true;

        for(String permission : permissions) {
            //you can return false instead of assigning, but by assigning you can log all permission values
            if (! hasPermission(context, permission)) {hasAllPermissions = false; }
        }

        return hasAllPermissions;

    }
}
