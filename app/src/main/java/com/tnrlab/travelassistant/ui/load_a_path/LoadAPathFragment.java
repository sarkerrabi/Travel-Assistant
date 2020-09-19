package com.tnrlab.travelassistant.ui.load_a_path;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.gson.Gson;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.tnrlab.travelassistant.R;
import com.tnrlab.travelassistant.models.creaet_path.RouteDetails;
import com.tnrlab.travelassistant.models.creaet_path.RoutePath;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.expressions.Expression.match;
import static com.mapbox.mapboxsdk.style.expressions.Expression.stop;

public class LoadAPathFragment extends Fragment implements
        OnMapReadyCallback, PermissionsListener {
    @BindView(R.id.tvType)
    TextView tvType;
    @BindView(R.id.btAcclerator)
    Button btAcclerator;
    private int selectedColorView = 0;
    private LoadAPathViewModel mViewModel;
    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;
    private MapView mapView;
    private RouteDetails routeDetails;

    public static LoadAPathFragment newInstance() {
        return new LoadAPathFragment();
    }


    @OnClick(R.id.btAcclerator)
    public void onClickAcclerate() {
        if (selectedColorView == 0) {
            selectedColorView = 1;
            btAcclerator.setText("ACCELEROMETER");

        } else if (selectedColorView == 1) {
            selectedColorView = 0;
            btAcclerator.setText("SPEED");
        }

        Toast.makeText(getContext(), "acc " + selectedColorView, Toast.LENGTH_SHORT).show();
        onMapReady(mapboxMap);


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.load_a_path_fragment, container, false);
        ButterKnife.bind(this, root);

        mapView = root.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(LoadAPathViewModel.class);
        Gson gson = new Gson();
        String routeData = getArguments().getString("route_path");

        btAcclerator.setText("SPEED");
        if (routeData != null) {
            routeDetails = gson.fromJson(routeData, RouteDetails.class);
            Toast.makeText(getContext(), String.valueOf(routeDetails.getRouteReview().getRoutePathID()), Toast.LENGTH_SHORT).show();


        }


    }

    private List<Point> getRouteCoordinatesFromDataList(List<RoutePath> routePathList) {

        List<Point> myRouteCoordinates = new ArrayList<>();
        for (RoutePath routePath : routePathList) {

            myRouteCoordinates.add(Point.fromLngLat(routePath.getLongitude(), routePath.getLatitude()));

        }
        return myRouteCoordinates;


    }

    private Feature[] getFeaturesFromDataList(List<RoutePath> routePathList) {

        Feature[] mFeatures = new Feature[routePathList.size()];


        for (int iK = 0; iK < routePathList.size(); iK++) {

            List<Point> myRouteCoordinates = new ArrayList<>();

            int max = iK + 1;

            for (int i = iK; i < routePathList.size(); i++) {

                RoutePath routePath = routePathList.get(i);
                myRouteCoordinates.add(Point.fromLngLat(routePath.getLongitude(), routePath.getLatitude()));
                if (i == max) {
                    break;
                }
            }


            mFeatures[iK] = Feature.fromGeometry(LineString.fromLngLats(myRouteCoordinates));
            int finalSpeed = (int) routePathList.get(iK).getSpeed();
            mFeatures[iK].addNumberProperty("speed", finalSpeed);

            int finalAccelerometer = (int) routePathList.get(iK).getSensorData().getAccelerometer().getTotalAcceleration();
            mFeatures[iK].addNumberProperty("accelerometer", finalAccelerometer);


        }
        return mFeatures;


    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        LoadAPathFragment.this.mapboxMap = mapboxMap;
        mapboxMap.getUiSettings().setZoomGesturesEnabled(true);
        mapboxMap.getUiSettings().setScrollGesturesEnabled(true);
        mapboxMap.getUiSettings().setAllGesturesEnabled(true);
        mapboxMap.setStyle(Style.OUTDOORS,
                new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        enableLocationComponent(style);

                        setCamerpostion(routeDetails.getRoutePathList().get(0).getLatitude(), routeDetails.getRoutePathList().get(0).getLongitude());

                        Feature[] mFeatures = getFeaturesFromDataList(routeDetails.getRoutePathList());

                        FeatureCollection featureCollection = FeatureCollection.fromFeatures(mFeatures);

                        style.addSource(new GeoJsonSource("line-source", featureCollection
                        ));

                        if (selectedColorView == 0) {
                            tvType.setText("speed");
                            style.addLayer(new LineLayer("linelayer", "line-source").withProperties(
                                    PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                                    PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
                                    PropertyFactory.lineWidth(14f),
                                    PropertyFactory.lineColor(
                                            match(get("speed"), Expression.rgb(41, 0, 0),
                                                    stop(0, Expression.rgb(255, 237, 237)),
                                                    stop(1, Expression.rgb(255, 217, 217)),
                                                    stop(2, Expression.rgb(255, 198, 198)),
                                                    stop(3, Expression.rgb(255, 178, 178)),
                                                    stop(4, Expression.rgb(255, 159, 159)),
                                                    stop(5, Expression.rgb(255, 139, 139)),
                                                    stop(6, Expression.rgb(255, 119, 119)),
                                                    stop(7, Expression.rgb(255, 100, 100)),
                                                    stop(8, Expression.rgb(255, 80, 80)),
                                                    stop(9, Expression.rgb(255, 61, 61)),
                                                    stop(10, Expression.rgb(255, 41, 41)),
                                                    stop(11, Expression.rgb(255, 21, 21)),
                                                    stop(12, Expression.rgb(255, 2, 2)),
                                                    stop(13, Expression.rgb(237, 0, 0)),
                                                    stop(14, Expression.rgb(217, 0, 0)),
                                                    stop(15, Expression.rgb(198, 0, 0)),
                                                    stop(16, Expression.rgb(178, 0, 0)),
                                                    stop(17, Expression.rgb(159, 0, 0)),
                                                    stop(18, Expression.rgb(139, 0, 0)),
                                                    stop(19, Expression.rgb(119, 0, 0)),
                                                    stop(20, Expression.rgb(100, 0, 0)),
                                                    stop(21, Expression.rgb(80, 0, 0)),
                                                    stop(22, Expression.rgb(61, 0, 0))
                                            )
                                    )
                                    )
                            );

                        } else if (selectedColorView == 1) {
                            tvType.setText("Accelerometer");
                            style.addLayer(new LineLayer("linelayer", "line-source").withProperties(
                                    PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                                    PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
                                    PropertyFactory.lineWidth(14f),
                                    PropertyFactory.lineColor(
                                            match(get("accelerometer"), Expression.rgb(41, 0, 0),
                                                    stop(0, Expression.rgb(255, 237, 237)),
                                                    stop(1, Expression.rgb(255, 217, 217)),
                                                    stop(2, Expression.rgb(255, 198, 198)),
                                                    stop(3, Expression.rgb(255, 178, 178)),
                                                    stop(4, Expression.rgb(255, 159, 159)),
                                                    stop(5, Expression.rgb(255, 139, 139)),
                                                    stop(6, Expression.rgb(255, 119, 119)),
                                                    stop(7, Expression.rgb(255, 100, 100)),
                                                    stop(8, Expression.rgb(255, 80, 80)),
                                                    stop(9, Expression.rgb(255, 61, 61)),
                                                    stop(10, Expression.rgb(255, 41, 41)),
                                                    stop(11, Expression.rgb(255, 21, 21)),
                                                    stop(12, Expression.rgb(255, 2, 2)),
                                                    stop(13, Expression.rgb(237, 0, 0)),
                                                    stop(14, Expression.rgb(217, 0, 0)),
                                                    stop(15, Expression.rgb(198, 0, 0)),
                                                    stop(16, Expression.rgb(178, 0, 0)),
                                                    stop(17, Expression.rgb(159, 0, 0)),
                                                    stop(18, Expression.rgb(139, 0, 0)),
                                                    stop(19, Expression.rgb(119, 0, 0)),
                                                    stop(20, Expression.rgb(100, 0, 0)),
                                                    stop(21, Expression.rgb(80, 0, 0)),
                                                    stop(22, Expression.rgb(61, 0, 0))
                                            )
                                    )
                                    )
                            );
                        }
                    }
                });

    }


    void setCamerpostion(double latitude, double longtitude) {
        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longtitude), 13.0));
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