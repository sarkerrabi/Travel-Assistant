package com.tnrlab.travelassistant.ui.create_path;

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.tnrlab.travelassistant.R;
import com.tnrlab.travelassistant.common.Common;
import com.tnrlab.travelassistant.databases.AppDatabase;
import com.tnrlab.travelassistant.models.creaet_path.Accelerometer;
import com.tnrlab.travelassistant.models.creaet_path.DeviceOrientation;
import com.tnrlab.travelassistant.models.creaet_path.GravityData;
import com.tnrlab.travelassistant.models.creaet_path.LinearAcceleration;
import com.tnrlab.travelassistant.models.creaet_path.RoutePath;
import com.tnrlab.travelassistant.models.creaet_path.SensorData;
import com.tnrlab.travelassistant.shared_db.SharedDB;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;
import static com.tnrlab.travelassistant.common.Common.getCurrentTimeAndDate;

public class CreatePathFragment extends Fragment implements OnMapReadyCallback, PermissionsListener {
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private final static String KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates";
    private final static String KEY_LOCATION = "location";
    private final static String KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string";
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 9000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    @BindView(R.id.start_updates_button)
    Button mStartUpdatesButton;
    @BindView(R.id.stop_updates_button)
    Button mStopUpdatesButton;
    @BindView(R.id.tvData)
    TextView tvData;
    boolean isCalled = false;
    private Boolean mRequestingLocationUpdates;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private CreatePathViewModel mViewModel;
    private AppDatabase mDb;
    private RoutePath routePath;
    private MapboxMap mapboxMap;
    private MapView mapView;
    private PermissionsManager permissionsManager;
    private Location mCurrentLocation;
    private String mLastUpdateTime;
    private SharedDB sharedDB;

    public static CreatePathFragment newInstance() {
        return new CreatePathFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.create_path_fragment, container, false);
        Mapbox.getInstance(getContext(), getString(R.string.mapbox_key));
        ButterKnife.bind(this, root);
        mDb = AppDatabase.getInstance(getContext());
        sharedDB = new SharedDB(getContext());

        mRequestingLocationUpdates = false;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mSettingsClient = LocationServices.getSettingsClient(getActivity());

        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();
        updateValuesFromBundle(savedInstanceState);


        mapView = root.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


        return root;
    }

/*    @OnClick(R.id.button)
    public void onViewClicked() {
        long result = mDb.routePathDao().insertRoutePath(routePath);

    }*/

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CreatePathViewModel.class);

        mViewModel.getSensorEventLiveData(getContext()).observe(this, data -> {
            tvData.setText("Sensor Data X=" + data.getX() + " Y=" + data.getY() + " Z=" + data.getZ());
        });
        routePath = new RoutePath();

        LinearAcceleration linearAcceleration = new LinearAcceleration(1, 2, 3);
        Accelerometer accelerometer = new Accelerometer(4, 5, 6);
        GravityData gravityData = new GravityData(7, 8, 9);
        DeviceOrientation deviceOrientation = new DeviceOrientation(10, 11, 12);


        float horizontalAngle = 11;

        double altitude = -36.70000076293945;
        double longitude = 22.3662237;
        double latitude = 91.8160706;
        float speed = 99;


        SensorData sensorData = new SensorData();
        sensorData.setAccelerometer(accelerometer);
        sensorData.setDeviceOrientation(deviceOrientation);
        sensorData.setGravity(gravityData);
        sensorData.setHorizontalAngle(horizontalAngle);
        sensorData.setLinearAcceleration(linearAcceleration);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(getContext(), "user_location_permission_explanation", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
            Toast.makeText(getContext(), "user_location_permission_not_granted", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        CreatePathFragment.this.mapboxMap = mapboxMap;
        mapboxMap.getUiSettings().setZoomGesturesEnabled(true);
        mapboxMap.getUiSettings().setScrollGesturesEnabled(true);
        mapboxMap.getUiSettings().setAllGesturesEnabled(true);

        mapboxMap.setStyle(Style.MAPBOX_STREETS,
                new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        enableLocationComponent(style);
                    }
                });
    }

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(getContext())) {

            // Get an instance of the component
            LocationComponent locationComponent = mapboxMap.getLocationComponent();

            // Activate with options
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.
                            builder(getContext(), loadedMapStyle)
                            .build());

            // Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);


            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING_GPS);


            // Set the component's render mode
            locationComponent.setRenderMode(RenderMode.GPS);


        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(getActivity());
        }
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void stopLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            //  Log.d(TAG, "stopLocationUpdates: updates never requested, no-op.");

            return;
        }
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mRequestingLocationUpdates = false;
                        setButtonsEnabledState();
                    }
                });

    }

    private void startLocationUpdates() {
        // Begin by checking if the device has the necessary location settings.
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");

                        //noinspection MissingPermission
                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                            return;
                        }
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());

                        updateUI();
                    }
                })
                .addOnFailureListener(getActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                mRequestingLocationUpdates = false;
                        }

                        updateUI();
                    }
                });
    }

    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    private void updateUI() {

        setButtonsEnabledState();
        updateLocationUI();

    }

    private void setButtonsEnabledState() {
        if (mRequestingLocationUpdates) {
            mStartUpdatesButton.setEnabled(false);
            mStopUpdatesButton.setEnabled(true);
        } else {
            mStartUpdatesButton.setEnabled(true);
            mStopUpdatesButton.setEnabled(false);
        }
    }

    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                mCurrentLocation = locationResult.getLastLocation();
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                updateLocationUI();
            }
        };
    }

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.keySet().contains(KEY_REQUESTING_LOCATION_UPDATES)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        KEY_REQUESTING_LOCATION_UPDATES);
            }

            if (savedInstanceState.keySet().contains(KEY_LOCATION)) {
                mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            }

            if (savedInstanceState.keySet().contains(KEY_LAST_UPDATED_TIME_STRING)) {
                mLastUpdateTime = savedInstanceState.getString(KEY_LAST_UPDATED_TIME_STRING);
            }
            updateUI();
        }
    }

    private void updateLocationUI() {
        if (mCurrentLocation != null) {
            Log.e(TAG, "updateLocationUI: " + mCurrentLocation.getLatitude());
            Log.e(TAG, "updateLocationUI: " + mCurrentLocation.getLongitude());
            Log.e(TAG, "updateLocationUI: " + mLastUpdateTime);
            Long tsLong = System.currentTimeMillis() / 1000;

            if (sharedDB.getStartPlaceInfo() == null && !isCalled) {
                savePlaceInfo(mCurrentLocation, 0);
                isCalled = true;
            }


            String timestamp = tsLong.toString();

            String timeDetails = getCurrentTimeAndDate() == null ? "" : getCurrentTimeAndDate();

            mViewModel.getSensorData(getContext()).observe(this, sensorData -> {

//                Log.e(TAG, "updateLocationUI:Gravity " + sensorData.getGravity().getX());

                routePath.setSensorData(sensorData);

                routePath.setAltitude(mCurrentLocation.getAltitude());
                routePath.setRoutePathID(sharedDB.getRouteID());
                routePath.setDescriptions("desc");
                routePath.setEndAddress("end address");
                routePath.setStartAddress("start address");
                routePath.setLatitude(mCurrentLocation.getLatitude());
                routePath.setLongitude(mCurrentLocation.getLongitude());
                routePath.setTimeDetails(timeDetails);
                routePath.setTimestmp(timestamp);
                routePath.setSpeed(mCurrentLocation.getSpeed());
                routePath.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
                routePath.setLatLan(mCurrentLocation.getLatitude() + "," + mCurrentLocation.getLongitude());
//                mDb.routePathDao().insertRoutePath(routePath);


            });

            mViewModel.getGravityLiveData(getContext()).observe(this, gravityData -> {

//                mDb.routePathDao().insertRoutePath(routePath);


            });
            mDb.routePathDao().insertRoutePath(routePath);

        }
    }

    private void savePlaceInfo(Location mCurrentLocation, int saveState) {
        MapboxGeocoding reverseGeocode = MapboxGeocoding.builder()
                .accessToken(getString(R.string.mapbox_key))
                .query(Point.fromLngLat(mCurrentLocation.getLongitude(), mCurrentLocation.getLatitude()))
                .geocodingTypes(GeocodingCriteria.TYPE_LOCALITY)
                .build();

        reverseGeocode.enqueueCall(new Callback<GeocodingResponse>() {
            @Override
            public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
                if (response.isSuccessful()) {
                    List<CarmenFeature> results = response.body().features();

                    if (results.size() > 0) {

                        // Log the first results Point.
                        Point firstResultPoint = results.get(0).center();
                        if (saveState == 0) {

                            sharedDB.saveStartPlaceInfo(response.body().features().get(0).placeName());
                            sharedDB.saveRouteID(Common.getUniqueRoutePathID());

                        } else if (saveState == 1) {
                            sharedDB.saveEndPlaceInfo(response.body().features().get(0).placeName());
                            Navigation.findNavController(getView()).navigate(R.id.action_nav_create_path_to_routeReviewFragment);

                        }


                    } else {

                        // No result for your request were found.
                        Log.d(TAG, "onResponse: No result found");

                    }
                }

            }

            @Override
            public void onFailure(Call<GeocodingResponse> call, Throwable t) {

            }
        });

    }


    @OnClick({R.id.start_updates_button, R.id.stop_updates_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.start_updates_button:
                if (!mRequestingLocationUpdates) {
                    mRequestingLocationUpdates = true;
                    setButtonsEnabledState();
                    startLocationUpdates();
                }
                break;
            case R.id.stop_updates_button:
                stopLocationUpdates();
                savePlaceInfo(mCurrentLocation, 1);
                break;
        }
    }

    @Override
    @SuppressWarnings({"MissingPermission"})
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}