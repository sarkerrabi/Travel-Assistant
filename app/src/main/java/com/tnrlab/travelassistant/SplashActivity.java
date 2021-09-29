package com.tnrlab.travelassistant;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.google.firebase.auth.FirebaseAuth;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.tnrlab.travelassistant.institution.institute.InstituteMainActivity;
import com.tnrlab.travelassistant.institution.login.LoginActivity;
import com.tnrlab.travelassistant.shared_db.SharedDB;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 4000; //4 seconds
    @BindView(R.id.iv_splash)
    ImageView ivSplash;
    SharedDB sharedDB;
    // used to know if the back button was pressed in the splash screen activity and avoid opening the next activity
    private boolean mIsBackButtonPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        getSupportActionBar().hide();

        sharedDB = new SharedDB(SplashActivity.this);
        DrawableImageViewTarget drawableImageViewTarget = new DrawableImageViewTarget(ivSplash);


        Glide.with(this)
                .load(R.raw.splash_animation)
                .into(drawableImageViewTarget);

        if (!sharedDB.isLocationPermissionAgreed()) {
            showAlert();
        } else {
            startApp();
        }


//        boolean hasAndroidPermissions = Common.hasPermissions(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION,
//                Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
//                Manifest.permission.INTERNET);
//
//        if (hasAndroidPermissions){
//            startApp();
//        }else {
//            checkPermission();
//        }

    }

    private void showAlert() {
        new AlertDialog.Builder(this)
                .setTitle("Travel Assistant")
                .setCancelable(false)
                .setMessage("Travel Assistant app collects location data to track your traveled path & visualized data even when the app is closed or not in use.")

                .setPositiveButton(R.string.agree, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        sharedDB.saveLocationPermissionAgreed(true);
                        startApp();
                        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        startActivity(intent);

                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(R.string.disagree, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onBackPressed() {

        // set the flag to true so the next activity won't start up
        mIsBackButtonPressed = true;
        super.onBackPressed();

    }

    private void checkPermission() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                if (multiplePermissionsReport.areAllPermissionsGranted()) {
                    //Toast.makeText(getContext(), "Thank you", Toast.LENGTH_SHORT).show();


                } else {
                    checkPermission();
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();

            }
        }).check();
    }

    public void startApp() {

        Handler handler = new Handler();


        // run a thread after 2 seconds to start the home screen
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {

                // make sure we close the splash screen so the user won't come back when it presses back key

                finish();

                if (!mIsBackButtonPressed) {
                    // start the home screen if the back button wasn't pressed already

                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                        if (sharedDB.getUserType() == 1 && sharedDB.getInstituteUserInfo() != null) {
                            Intent intent = new Intent(SplashActivity.this, InstituteMainActivity.class);
                            intent.putExtra("institute", sharedDB.getInstituteUserInfo());
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else if (sharedDB.getUserType() == 2 && sharedDB.getUserInfo() != null) {
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            intent.putExtra("user", sharedDB.getUserInfo());
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }

                    } else {

                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }

                }

            }

        }, SPLASH_DURATION); // time in milliseconds (1 second = 1000 milliseconds) until the run() method will be called

    }


}
